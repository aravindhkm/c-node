package org.tron.core.capsule;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.tron.common.parameter.CommonParameter;
import org.tron.common.utils.Sha256Hash;

@Slf4j(topic = "capsule")
public class CodeCapsule implements ProtoCapsule<byte[]> {

  private byte[] code;

  public CodeCapsule(byte[] code) {
    this.code = code;
  }

  public Sha256Hash getCodeHash() {
    return Sha256Hash.of(CommonParameter.getInstance().isECKeyCryptoEngine(),
        this.code);
  }

  @Override
  public byte[] getData() {
    return this.code;
  }

  @Override
  public byte[] getInstance() {
    return this.code;
  }

  @Override
  public String toString() {
    return Arrays.toString(this.code);
  }
}
