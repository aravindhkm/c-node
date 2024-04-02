package org.tron.core.exception;

public class CypherRuntimeException extends RuntimeException {

  public CypherRuntimeException() {
    super();
  }

  public CypherRuntimeException(String message) {
    super(message);
  }

  public CypherRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CypherRuntimeException(Throwable cause) {
    super(cause);
  }

  protected CypherRuntimeException(String message, Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }


}
