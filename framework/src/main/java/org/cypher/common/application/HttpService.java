package org.tron.common.application;

import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;

@Slf4j(topic = "rpc")
public abstract class HttpService implements Service {

  protected Server apiServer;
  protected int port;

  @Override
  public void blockUntilShutdown() {
    if (apiServer != null) {
      try {
        apiServer.join();
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
      } catch (Exception e) {
        logger.error("{}", this.getClass().getSimpleName(), e);
      }
    }
  }

  @Override
  public void stop() {
    if (apiServer != null) {
      logger.info("{} shutdown...", this.getClass().getSimpleName());
      try {
        apiServer.stop();
      } catch (Exception e) {
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
    HttpService that = (HttpService) o;
    return port == that.port;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getClass().getSimpleName(), port);
  }
}
