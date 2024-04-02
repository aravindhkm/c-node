package org.tron.core.net.messagehandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.es.ExecutorServiceManager;
import org.tron.core.config.args.Args;
import org.tron.core.exception.P2pException;
import org.tron.core.exception.P2pException.TypeEnum;
import org.tron.core.net.CypherNetDelegate;
import org.tron.core.net.message.CypherMessage;
import org.tron.core.net.message.adv.TransactionMessage;
import org.tron.core.net.message.adv.TransactionsMessage;
import org.tron.core.net.peer.Item;
import org.tron.core.net.peer.PeerConnection;
import org.tron.core.net.service.adv.AdvService;
import org.tron.protos.Protocol.Inventory.InventoryType;
import org.tron.protos.Protocol.ReasonCode;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Transaction.Contract.ContractType;

@Slf4j(topic = "net")
@Component
public class TransactionsMsgHandler implements CypherMsgHandler {

  private static int MAX_CYP_SIZE = 50_000;
  private static int MAX_SMART_CONTRACT_SUBMIT_SIZE = 100;
  @Autowired
  private CypherNetDelegate tronNetDelegate;
  @Autowired
  private AdvService advService;

  private BlockingQueue<CypEvent> smartContractQueue = new LinkedBlockingQueue(MAX_CYP_SIZE);

  private BlockingQueue<Runnable> queue = new LinkedBlockingQueue();

  private int threadNum = Args.getInstance().getValidateSignThreadNum();
  private final String cypEsName = "cyp-msg-handler";
  private ExecutorService cypHandlePool = ExecutorServiceManager.newThreadPoolExecutor(
      threadNum, threadNum, 0L,
      TimeUnit.MILLISECONDS, queue, cypEsName);
  private final String smartEsName = "contract-msg-handler";
  private final ScheduledExecutorService smartContractExecutor = ExecutorServiceManager
      .newSingleThreadScheduledExecutor(smartEsName);

  public void init() {
    handleSmartContract();
  }

  public void close() {
    ExecutorServiceManager.shutdownAndAwaitTermination(cypHandlePool, cypEsName);
    ExecutorServiceManager.shutdownAndAwaitTermination(smartContractExecutor, smartEsName);
  }

  public boolean isBusy() {
    return queue.size() + smartContractQueue.size() > MAX_CYP_SIZE;
  }

  @Override
  public void processMessage(PeerConnection peer, CypherMessage msg) throws P2pException {
    TransactionsMessage transactionsMessage = (TransactionsMessage) msg;
    check(peer, transactionsMessage);
    int smartContractQueueSize = 0;
    int cypHandlePoolQueueSize = 0;
    int dropSmartContractCount = 0;
    for (Transaction cyp : transactionsMessage.getTransactions().getTransactionsList()) {
      int type = cyp.getRawData().getContract(0).getType().getNumber();
      if (type == ContractType.TriggerSmartContract_VALUE
          || type == ContractType.CreateSmartContract_VALUE) {
        if (!smartContractQueue.offer(new CypEvent(peer, new TransactionMessage(cyp)))) {
          smartContractQueueSize = smartContractQueue.size();
          cypHandlePoolQueueSize = queue.size();
          dropSmartContractCount++;
        }
      } else {
        cypHandlePool.submit(() -> handleTransaction(peer, new TransactionMessage(cyp)));
      }
    }

    if (dropSmartContractCount > 0) {
      logger.warn("Add smart contract failed, drop count: {}, queueSize {}:{}",
          dropSmartContractCount, smartContractQueueSize, cypHandlePoolQueueSize);
    }
  }

  private void check(PeerConnection peer, TransactionsMessage msg) throws P2pException {
    for (Transaction cyp : msg.getTransactions().getTransactionsList()) {
      Item item = new Item(new TransactionMessage(cyp).getMessageId(), InventoryType.CYP);
      if (!peer.getAdvInvRequest().containsKey(item)) {
        throw new P2pException(TypeEnum.BAD_MESSAGE,
            "cyp: " + msg.getMessageId() + " without request.");
      }
      peer.getAdvInvRequest().remove(item);
    }
  }

  private void handleSmartContract() {
    smartContractExecutor.scheduleWithFixedDelay(() -> {
      try {
        while (queue.size() < MAX_SMART_CONTRACT_SUBMIT_SIZE && smartContractQueue.size() > 0) {
          CypEvent event = smartContractQueue.take();
          cypHandlePool.submit(() -> handleTransaction(event.getPeer(), event.getMsg()));
        }
      } catch (InterruptedException e) {
        logger.warn("Handle smart server interrupted");
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        logger.error("Handle smart contract exception", e);
      }
    }, 1000, 20, TimeUnit.MILLISECONDS);
  }

  private void handleTransaction(PeerConnection peer, TransactionMessage cyp) {
    if (peer.isBadPeer()) {
      logger.warn("Drop cyp {} from {}, peer is bad peer", cyp.getMessageId(),
          peer.getInetAddress());
      return;
    }

    if (advService.getMessage(new Item(cyp.getMessageId(), InventoryType.CYP)) != null) {
      return;
    }

    try {
      tronNetDelegate.pushTransaction(cyp.getTransactionCapsule());
      advService.broadcast(cyp);
    } catch (P2pException e) {
      logger.warn("Cyp {} from peer {} process failed. type: {}, reason: {}",
          cyp.getMessageId(), peer.getInetAddress(), e.getType(), e.getMessage());
      if (e.getType().equals(TypeEnum.BAD_CYP)) {
        peer.setBadPeer(true);
        peer.disconnect(ReasonCode.BAD_TX);
      }
    } catch (Exception e) {
      logger.error("Cyp {} from peer {} process failed", cyp.getMessageId(), peer.getInetAddress(),
          e);
    }
  }

  class CypEvent {

    @Getter
    private PeerConnection peer;
    @Getter
    private TransactionMessage msg;
    @Getter
    private long time;

    public CypEvent(PeerConnection peer, TransactionMessage msg) {
      this.peer = peer;
      this.msg = msg;
      this.time = System.currentTimeMillis();
    }
  }
}