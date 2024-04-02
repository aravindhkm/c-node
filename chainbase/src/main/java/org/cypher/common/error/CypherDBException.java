package org.tron.common.error;

public class CypherDBException extends RuntimeException {
    public CypherDBException() {
  }

    public CypherDBException(String s) {
    super(s);
  }

    public CypherDBException(String s, Throwable throwable) {
    super(s, throwable);
  }

    public CypherDBException(Throwable throwable) {
    super(throwable);
  }
}
