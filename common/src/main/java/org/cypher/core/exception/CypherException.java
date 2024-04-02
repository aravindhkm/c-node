package org.tron.core.exception;

public class CypherException extends Exception {

  public CypherException() {
    super();
    report();
  }

  public CypherException(String message) {
    super(message);
    report();
  }

  public CypherException(String message, Throwable cause) {
    super(message, cause);
    report();
  }

  protected void report(){

  }

}
