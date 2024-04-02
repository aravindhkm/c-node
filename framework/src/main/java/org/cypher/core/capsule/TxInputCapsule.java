package org.tron.core.capsule;

import com.google.protobuf.ByteString;
import org.tron.protos.Protocol.TXInput;

public class TxInputCapsule implements ProtoCapsule<TXInput> {

  private TXInput txInput;

  /**
   * constructor TxInputCapsule.
   *
   * @param txId byte[] txId
   * @param vout int vout
   * @param signature byte[] signature
   * @param pubKey byte[] pubKey
   */
  public TxInputCapsule(byte[] txId, long vout, byte[]
      signature, byte[] pubKey) {
    TXInput.raw txInputRaw = TXInput.raw.newBuilder()
        .setTxID(ByteString.copyFrom(txId))
        .setVout(vout)
        .setPubKey(ByteString.copyFrom(pubKey)).build();

    this.txInput = TXInput.newBuilder()
        .setRawData(txInputRaw)
        .setSignature(ByteString.copyFrom(signature))
        .build();

  }

  public TXInput getTxInput() {
    return txInput;
  }

  public boolean validate() {
    return true;
  }

  @Override
  public byte[] getData() {
    return new byte[0];
  }

  @Override
  public TXInput getInstance() {
    return this.txInput;
  }
}
