package org.tron.core.net.services;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.tron.common.application.CypherApplicationContext;
import org.tron.common.utils.ReflectUtils;
import org.tron.core.Constant;
import org.tron.core.config.DefaultConfig;
import org.tron.core.config.args.Args;
import org.tron.core.net.CypherNetService;
import org.tron.core.net.service.effective.EffectiveCheckService;
import org.tron.p2p.P2pConfig;

public class EffectiveCheckServiceTest {

  protected CypherApplicationContext context;
  private EffectiveCheckService service;

  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  public void init() throws IOException {
    Args.setParam(new String[] {"--output-directory",
        temporaryFolder.newFolder().toString(), "--debug"}, Constant.TEST_CONF);
    context = new CypherApplicationContext(DefaultConfig.class);
    service = context.getBean(EffectiveCheckService.class);
  }

  @After
  public void destroy() {
    Args.clearParam();
    context.destroy();
  }

  @Test
  public void testNoIpv4() throws Exception {
    CypherNetService tronNetService = context.getBean(CypherNetService.class);
    Method privateMethod = tronNetService.getClass()
        .getDeclaredMethod("updateConfig", P2pConfig.class);
    privateMethod.setAccessible(true);
    P2pConfig config = new P2pConfig();
    config.setIp(null);
    P2pConfig newConfig = (P2pConfig) privateMethod.invoke(tronNetService, config);
    Assert.assertNotNull(newConfig.getIp());
  }

  @Test
  public void testFind() {
    CypherNetService tronNetService = context.getBean(CypherNetService.class);
    P2pConfig p2pConfig = new P2pConfig();
    p2pConfig.setIp("127.0.0.1");
    p2pConfig.setPort(34567);
    ReflectUtils.setFieldValue(tronNetService, "p2pConfig", p2pConfig);
    CypherNetService.getP2pService().start(p2pConfig);

    service.triggerNext();
    Assert.assertNull(service.getCur());

    ReflectUtils.invokeMethod(service, "resetCount");
    InetSocketAddress cur = new InetSocketAddress("192.168.0.1", 34567);
    service.setCur(cur);
    service.onDisconnect(cur);
  }
}
