package org.tron.common.application;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.tron.core.config.CypherLogShutdownHook;

public class CypherApplicationContext extends AnnotationConfigApplicationContext {

  public CypherApplicationContext() {
  }

  public CypherApplicationContext(DefaultListableBeanFactory beanFactory) {
    super(beanFactory);
  }

  public CypherApplicationContext(Class<?>... annotatedClasses) {
    super(annotatedClasses);
  }

  public CypherApplicationContext(String... basePackages) {
    super(basePackages);
  }

  @Override
  public void doClose() {
    logger.info("******** start to close ********");
    Application appT = ApplicationFactory.create(this);
    appT.shutdown();
    super.doClose();
    logger.info("******** close end ********");
    CypherLogShutdownHook.shutDown = true;
  }

  @Override
  public void registerShutdownHook() {
    super.registerShutdownHook();
    CypherLogShutdownHook.shutDown = false;
  }
}
