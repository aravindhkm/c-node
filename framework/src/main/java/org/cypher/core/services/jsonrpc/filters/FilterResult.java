package org.tron.core.services.jsonrpc.filters;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.Getter;
import org.tron.core.services.jsonrpc.CypherJsonRpcImpl;

public abstract class FilterResult<T> {

  private long expireTimeStamp;

  @Getter
  protected BlockingQueue<T> result;

  public void updateExpireTime() {
    expireTimeStamp = System.currentTimeMillis() + CypherJsonRpcImpl.EXPIRE_SECONDS * 1000;
  }

  public boolean isExpire() {
    return expireTimeStamp < System.currentTimeMillis();
  }

  public abstract void add(T t);

  public abstract List<T> popAll();
}
