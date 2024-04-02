package org.tron.common.application;

import com.google.common.base.Objects;
import io.grpc.Server;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "rpc")
public abstract class RpcService implements Service {

  protected Server apiServer;
  protected int port;

  @Override
  public void blockUntilShutdown() {
    if (apiServer != null) {
      try {
        apiServer.awaitTermination();
      } catch (InterruptedException e) {
        logger.warn("{}", e.getMessage());
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public void start() {
    if (apiServer != null) {
      try {
        apiServer.start();
        logger.info("{} started, listening on {}", this.getClass().getSimpleName(), port);
      } catch (IOException e) {
        logger.error("{}", this.getClass().getSimpleName(), e);
      }
    }
  }

  @Override
  public void stop() {
    if (apiServer != null) {
      logger.info("{} shutdown...", this.getClass().getSimpleName());
      try {
        apiServer.shutdown().awaitTermination(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.warn("{}", this.getClass().getSimpleName(), e);
      }
      logger.info("{} shutdown complete", this.getClass().getSimpleName());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RpcService that = (RpcService) o;
    return port == that.port;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getClass().getSimpleName(), port);
  }

}
