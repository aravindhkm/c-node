package org.tron.core.net.messagehandler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.Striped;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.consensus.base.Param;
import org.tron.consensus.pbft.PbftManager;
import org.tron.consensus.pbft.message.PbftBaseMessage;
import org.tron.consensus.pbft.message.PbftMessage;
import org.tron.core.config.args.Args;
import org.tron.core.exception.P2pException;
import org.tron.core.net.CypherNetDelegate;
import org.tron.core.net.CypherNetService;
import org.tron.core.net.peer.PeerConnection;
import org.tron.protos.Protocol.PBFTMessage.DataType;

@Component
public class PbftMsgHandler {

  private static final Striped<Lock> striped = Striped.lazyWeakLock(1024);

  private static final Cache<String, Boolean> msgCache = CacheBuilder.newBuilder()
      .initialCapacity(3000).maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES).build();

  @Autowired
  private PbftManager pbftManager;

  @Autowired
  private CypherNetDelegate tronNetDelegate;

  public void processMessage(PeerConnection peer, PbftMessage msg) throws Exception {
    if (!tronNetDelegate.allowPBFT()) {
      return;
    }
    if (Param.getInstance().getPbftInterface().isSyncing()) {
      return;
    }
    if (msg.getDataType().equals(DataType.BLOCK)
        && tronNetDelegate.getHeadBlockId().getNum() - msg.getNumber()
        > Args.getInstance().getPBFTExpireNum()) {
      return;
    }
    long currentEpoch = tronNetDelegate.getNextMaintenanceTime();
    long expireEpoch = 2 * tronNetDelegate.getMaintenanceTimeInterval();
    if (msg.getDataType().equals(DataType.SRL)
        && currentEpoch - msg.getEpoch() > expireEpoch) {
      return;
    }
    msg.analyzeSignature();
    String key = buildKey(msg);
    Lock lock = striped.get(key);
    try {
      lock.lock();
      if (msgCache.getIfPresent(key) != null) {
        return;
      }
      if (!pbftManager.verifyMsg(msg)) {
        throw new P2pException(P2pException.TypeEnum.BAD_MESSAGE, msg.toString());
      }
      msgCache.put(key, true);
      forwardMessage(peer, msg);
      pbftManager.doAction(msg);
    } finally {
      lock.unlock();
    }

  }

  public void forwardMessage(PeerConnection peer, PbftBaseMessage message) {
    CypherNetService.getPeers().stream().filter(peerConnection -> !peerConnection.equals(peer))
        .forEach(peerConnection -> peerConnection.sendMessage(message));
  }

  private String buildKey(PbftBaseMessage msg) {
    return msg.getKey() + msg.getPbftMessage().getRawData().getMsgType().toString();
  }

}