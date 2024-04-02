package org.tron.common.application;

import org.tron.common.parameter.CommonParameter;

public interface Service {

  void init();

  void init(CommonParameter parameter);

  /**
   * Start the service.
   * {@link Service#init(CommonParameter parameter) init(CommonParameter parameter)} must be called
   * before this method.
   */
  void start();

  void stop();

  void blockUntilShutdown();
}
