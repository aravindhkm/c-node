package org.tron.core.exception;

public class JsonRpcInvalidParamsException extends CypherException {

  public JsonRpcInvalidParamsException() {
    super();
  }

  public JsonRpcInvalidParamsException(String msg) {
    super(msg);
  }

  public JsonRpcInvalidParamsException(String message, Throwable cause) {
    super(message, cause);
  }
}