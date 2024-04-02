package org.tron.common.application;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.tron.common.parameter.CommonParameter;

@Slf4j(topic = "app")
public class ServiceContainer {

  private final Set<Service> services;

  public ServiceContainer() {
    this.services = Collections.synchronizedSet(new LinkedHashSet<>());
  }

  public void add(Service service) {
    this.services.add(service);
  }


  public void init() {
    this.services.forEach(service -> {
      logger.debug("Initing {}.", service.getClass().getSimpleName());
      service.init();
    });
  }

  public void init(CommonParameter parameter) {
    this.services.forEach(service -> {
      logger.debug("Initing {}.", service.getClass().getSimpleName());
      service.init(parameter);
    });
  }

  public void start() {
    logger.info("Starting api services.");
    this.services.forEach(service -> {
      logger.debug("Starting {}.", service.getClass().getSimpleName());
      service.start();
    });
    logger.info("All api services started.");
  }

  public void stop() {
    logger.info("Stopping api services.");
    this.services.forEach(service -> {
      logger.debug("Stopping {}.", service.getClass().getSimpleName());
      service.stop();
    });
    logger.info("All api services stopped.");
  }

  public void blockUntilShutdown() {
    this.services.stream().findFirst().ifPresent(Service::blockUntilShutdown);
  }
}
