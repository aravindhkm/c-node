package org.tron.core.capsule;

import static java.lang.Math.max;
import static java.lang.Math.min;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.tron.core.Constant;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.contract.SmartContractOuterClass.CreateSmartContract;
import org.tron.protos.contract.SmartContractOuterClass.SmartContract;
import org.tron.protos.contract.SmartContractOuterClass.SmartContract.ABI;
import org.tron.protos.contract.SmartContractOuterClass.SmartContractDataWrapper;
import org.tron.protos.contract.SmartContractOuterClass.SmartContractDataWrapperOrBuilder;
import org.tron.protos.contract.SmartContractOuterClass.TriggerSmartContract;

@Slf4j(topic = "capsule")
public class ContractCapsule implements ProtoCapsule<SmartContract> {

  private SmartContract smartContract;
  private byte[] runtimecode;

  /**
   * constructor TransactionCapsule.
   */
  public ContractCapsule(SmartContract smartContract) {
    this.smartContract = smartContract;
  }

  public ContractCapsule(byte[] data) {
    try {
      this.smartContract = SmartContract.parseFrom(data);
    } catch (InvalidProtocolBufferException e) {
      // logger.debug(e.getMessage());
    }
  }

  public static CreateSmartContract getSmartContractFromTransaction(Transaction cyp) {
    try {
      Any any = cyp.getRawData().getContract(0).getParameter();
      CreateSmartContract createSmartContract = any.unpack(CreateSmartContract.class);
      return createSmartContract;
    } catch (InvalidProtocolBufferException e) {
      return null;
    }
  }

  public static TriggerSmartContract getTriggerContractFromTransaction(Transaction cyp) {
    try {
      Any any = cyp.getRawData().getContract(0).getParameter();
      TriggerSmartContract contractTriggerContract = any.unpack(TriggerSmartContract.class);
      return contractTriggerContract;
    } catch (InvalidProtocolBufferException e) {
      return null;
    }
  }

  public byte[] getCodeHash() {
    return this.smartContract.getCodeHash().toByteArray();
  }

  public void setCodeHash(byte[] codeHash) {
    this.smartContract = this.smartContract.toBuilder().setCodeHash(ByteString.copyFrom(codeHash))
        .build();
  }

  public void setRuntimecode(byte[] bytecode) {
    this.runtimecode = bytecode;
  }

  public SmartContractDataWrapper generateWrapper() {
    return SmartContractDataWrapper.newBuilder().setSmartContract(this.smartContract)
        .setRuntimecode(ByteString.copyFrom(this.runtimecode)).build();
  }

  @Override
  public byte[] getData() {
    return this.smartContract.toByteArray();
  }

  @Override
  public SmartContract getInstance() {
    return this.smartContract;
  }

  @Override
  public String toString() {
    return this.smartContract.toString();
  }

  public byte[] getOriginAddress() {
    return this.smartContract.getOriginAddress().toByteArray();
  }

  public long getConsumeUserResourcePercent() {
    long percent = this.smartContract.getConsumeUserResourcePercent();
    return max(0, min(percent, Constant.ONE_HUNDRED));
  }

  public long getOriginEnergyLimit() {
    long originEnergyLimit = this.smartContract.getOriginEnergyLimit();
    if (originEnergyLimit == Constant.PB_DEFAULT_ENERGY_LIMIT) {
      originEnergyLimit = Constant.CREATOR_DEFAULT_ENERGY_LIMIT;
    }
    return originEnergyLimit;
  }

  public void clearABI() {
    this.smartContract = this.smartContract.toBuilder().setAbi(ABI.getDefaultInstance()).build();
  }

  public byte[] getCypHash() {
    return this.smartContract.getCypHash().toByteArray();
  }

  public int getContractVersion() {
    return this.smartContract.getVersion();
  }

}
