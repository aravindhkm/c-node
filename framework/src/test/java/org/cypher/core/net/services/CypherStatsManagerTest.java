package org.tron.core.net.services;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;
import org.tron.core.net.service.statistics.NodeStatistics;
import org.tron.core.net.service.statistics.CypherStatsManager;
import org.tron.protos.Protocol;

public class CypherStatsManagerTest {

  @Test
  public void testOnDisconnect() {
    InetSocketAddress inetSocketAddress =
        new InetSocketAddress("127.0.0.2", 10001);

    InetAddress inetAddress = inetSocketAddress.getAddress();

    NodeStatistics statistics = CypherStatsManager.getNodeStatistics(inetAddress);

    Assert.assertTrue(null != statistics);
    Assert.assertEquals(Protocol.ReasonCode.UNKNOWN, statistics.getDisconnectReason());
  }

}
