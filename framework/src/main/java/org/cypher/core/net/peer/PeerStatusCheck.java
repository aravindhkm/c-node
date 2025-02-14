package org.tron.core.net.peer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.common.es.ExecutorServiceManager;
import org.tron.core.config.Parameter.NetConstants;
import org.tron.core.net.CypherNetDelegate;
import org.tron.protos.Protocol.ReasonCode;

@Slf4j(topic = "net")
@Component
public class PeerStatusCheck {

  @Autowired
  private CypherNetDelegate tronNetDelegate;

  private final String name = "peer-status-check";

  private ScheduledExecutorService peerStatusCheckExecutor =  ExecutorServiceManager
      .newSingleThreadScheduledExecutor(name);

  private int blockUpdateTimeout = 30_000;

  public void init() {
    peerStatusCheckExecutor.scheduleWithFixedDelay(() -> {
      try {
        statusCheck();
      } catch (Exception e) {
        logger.error("Check peers status processing failed", e);
      }
    }, 5, 2, TimeUnit.SECONDS);
  }

  public void close() {
    ExecutorServiceManager.shutdownAndAwaitTermination(peerStatusCheckExecutor, name);
  }

  public void statusCheck() {

    long now = System.currentTimeMillis();

    tronNetDelegate.getActivePeer().forEach(peer -> {

      boolean isDisconnected = false;

      if (peer.isNeedSyncFromPeer()
          && peer.getBlockBothHaveUpdateTime() < now - blockUpdateTimeout) {
        logger.warn("Peer {} not sync for a long time", peer.getInetAddress());
        isDisconnected = true;
      }

      if (!isDisconnected) {
        isDisconnected = peer.getAdvInvRequest().values().stream()
            .anyMatch(time -> time < now - NetConstants.ADV_TIME_OUT);
        if (isDisconnected) {
          logger.warn("Peer {} get avd message timeout", peer.getInetAddress());
        }
      }

      if (!isDisconnected) {
        isDisconnected = peer.getSyncBlockRequested().values().stream()
            .anyMatch(time -> time < now - NetConstants.SYNC_TIME_OUT);
        if (isDisconnected) {
          logger.warn("Peer {} get sync message timeout", peer.getInetAddress());
        }
      }

      if (isDisconnected) {
        peer.disconnect(ReasonCode.TIME_OUT);
      }
    });
  }

}
