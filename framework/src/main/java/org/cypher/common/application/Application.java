package org.tron.common.application;

import org.tron.common.parameter.CommonParameter;
import org.tron.core.ChainBaseManager;
import org.tron.core.config.args.Args;
import org.tron.core.db.Manager;

public interface Application {

  void setOptions(Args args);

  void init(CommonParameter parameter);

  void initServices(CommonParameter parameter);

  void startup();

  void shutdown();

  void startServices();

  // DO NOT USE THIS METHOD IN TEST CASES MAIN-THREAD
  default void blockUntilShutdown() {
  }

  void shutdownServices();

  void addService(Service service);

  Manager getDbManager();

  ChainBaseManager getChainBaseManager();

}
