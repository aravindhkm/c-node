package org.tron.core.net.services;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.tron.common.application.CypherApplicationContext;
import org.tron.common.parameter.CommonParameter;
import org.tron.common.utils.ReflectUtils;
import org.tron.common.utils.Sha256Hash;
import org.tron.core.Constant;
import org.tron.core.capsule.BlockCapsule;
import org.tron.core.config.DefaultConfig;
import org.tron.core.config.args.Args;
import org.tron.core.net.P2pEventHandlerImpl;
import org.tron.core.net.message.adv.BlockMessage;
import org.tron.core.net.message.adv.TransactionMessage;
import org.tron.core.net.peer.Item;
import org.tron.core.net.peer.PeerConnection;
import org.tron.core.net.peer.PeerManager;
import org.tron.core.net.service.adv.AdvService;
import org.tron.p2p.connection.Channel;
import org.tron.protos.Protocol;
import org.tron.protos.Protocol.Inventory.InventoryType;

public class AdvServiceTest {
  private static CypherApplicationContext context;
  private static AdvService service;
  private static P2pEventHandlerImpl p2pEventHandler;
  private static ApplicationContext ctx;

  @ClassRule
  public static final TemporaryFolder temporaryFolder = new TemporaryFolder();

  @BeforeClass
  public static void init() throws IOException {
    Args.setParam(new String[]{"--output-directory",
        temporaryFolder.newFolder().toString(), "--debug"}, Constant.TEST_CONF);
    context = new CypherApplicationContext(DefaultConfig.class);
    service = context.getBean(AdvService.class);
    p2pEventHandler = context.getBean(P2pEventHandlerImpl.class);
    ctx = (ApplicationContext) ReflectUtils.getFieldObject(p2pEventHandler, "ctx");
  }

  @AfterClass
  public static void after() {
    Args.clearParam();
    context.destroy();
  }

  @Test
  public void test() {
    testAddInv();
    testBroadcast();
    testCypBroadcast();
  }

  private void testAddInv() {
    boolean flag;
    Item itemCyp = new Item(Sha256Hash.ZERO_HASH, InventoryType.CYP);
    flag = service.addInv(itemCyp);
    Assert.assertTrue(flag);
    flag = service.addInv(itemCyp);
    Assert.assertFalse(flag);

    Item itemBlock = new Item(Sha256Hash.ZERO_HASH, InventoryType.BLOCK);
    flag = service.addInv(itemBlock);
    Assert.assertTrue(flag);
    flag = service.addInv(itemBlock);
    Assert.assertFalse(flag);

    service.addInvToCache(itemBlock);
    flag = service.addInv(itemBlock);
    Assert.assertFalse(flag);
  }

  private void testBroadcast() {
    InetSocketAddress inetSocketAddress =
            new InetSocketAddress("127.0.0.2", 10001);

    Channel c1 = mock(Channel.class);
    Mockito.when(c1.getInetSocketAddress()).thenReturn(inetSocketAddress);
    Mockito.when(c1.getInetAddress()).thenReturn(inetSocketAddress.getAddress());

    PeerConnection peer = PeerManager.add(ctx, c1);
    peer.setChannel(c1);
    peer.setNeedSyncFromUs(false);
    peer.setNeedSyncFromPeer(false);

    BlockCapsule blockCapsule = new BlockCapsule(1, Sha256Hash.ZERO_HASH,
            System.currentTimeMillis(), Sha256Hash.ZERO_HASH.getByteString());
    BlockMessage msg = new BlockMessage(blockCapsule);
    service.broadcast(msg);
    Item item = new Item(blockCapsule.getBlockId(), InventoryType.BLOCK);
    Assert.assertNotNull(service.getMessage(item));
    Assert.assertNotNull(peer.getAdvInvSpread().getIfPresent(item));
  }

  private void testCypBroadcast() {
    Protocol.Transaction cyp = Protocol.Transaction.newBuilder().build();
    CommonParameter.getInstance().setValidContractProtoThreadNum(1);
    TransactionMessage msg = new TransactionMessage(cyp);
    service.broadcast(msg);
    Item item = new Item(msg.getMessageId(), InventoryType.CYP);
    Assert.assertNotNull(service.getMessage(item));
  }

}
