package org.tron.core.exception;

import java.util.Locale;
import org.tron.common.prometheus.MetricKeys;
import org.tron.common.prometheus.Metrics;

public class P2pException extends Exception {

  private TypeEnum type;

  public P2pException(TypeEnum type, String errMsg) {
    super(errMsg);
    this.type = type;
    report();
  }

  public P2pException(TypeEnum type, Throwable throwable) {
    super(throwable);
    this.type = type;
    report();
  }

  public P2pException(TypeEnum type, String errMsg, Throwable throwable) {
    super(errMsg, throwable);
    this.type = type;
    report();
  }

  private void report () {
    Metrics.counterInc(MetricKeys.Counter.P2P_ERROR, 1,
        type.name().toLowerCase(Locale.ROOT));
  }

  public TypeEnum getType() {
    return type;
  }

  public enum TypeEnum {
    NO_SUCH_MESSAGE(1, "no such message"),
    PARSE_MESSAGE_FAILED(2, "parse message failed"),
    MESSAGE_WITH_WRONG_LENGTH(3, "message with wrong length"),
    BAD_MESSAGE(4, "bad message"),
    DIFF_GENESIS_BLOCK(5, "different genesis block"),
    HARD_FORKED(6, "hard forked"),
    SYNC_FAILED(7, "sync failed"),
    CHECK_FAILED(8, "check failed"),
    UNLINK_BLOCK(9, "unlink block"),
    BAD_BLOCK(10, "bad block"),
    BAD_CYP(11, "bad cyp"),
    CYP_EXE_FAILED(12, "cyp exe failed"),
    DB_ITEM_NOT_FOUND(13, "DB item not found"),
    PROTOBUF_ERROR(14, "protobuf inconsistent"),
    BLOCK_SIGN_ERROR(15, "block sign error"),
    BLOCK_MERKLE_ERROR(16, "block merkle error"),

    DEFAULT(100, "default exception");

    private Integer value;
    private String desc;

    TypeEnum(Integer value, String desc) {
      this.value = value;
      this.desc = desc;
    }

    public Integer getValue() {
      return value;
    }

    public String getDesc() {
      return desc;
    }

    @Override
    public String toString() {
      return value + ", " + desc;
    }
  }

}