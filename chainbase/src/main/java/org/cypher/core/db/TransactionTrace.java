package org.tron.core.db;

import static org.tron.common.runtime.InternalTransaction.CypType.CYP_CONTRACT_CALL_TYPE;
import static org.tron.common.runtime.InternalTransaction.CypType.CYP_CONTRACT_CREATION_TYPE;
import static org.tron.core.config.Parameter.ChainConstant.WINDOW_SIZE_PRECISION;
import static org.tron.protos.contract.Common.ResourceCode.ENERGY;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.tron.common.runtime.InternalTransaction.CypType;
import org.tron.common.runtime.ProgramResult;
import org.tron.common.runtime.Runtime;
import org.tron.common.runtime.vm.DataWord;
import org.tron.common.utils.DecodeUtil;
import org.tron.common.utils.ForkController;
import org.tron.common.utils.Sha256Hash;
import org.tron.common.utils.StringUtil;
import org.tron.common.utils.WalletUtil;
import org.tron.core.Constant;
import org.tron.core.capsule.AccountCapsule;
import org.tron.core.capsule.BlockCapsule;
import org.tron.core.capsule.ContractCapsule;
import org.tron.core.capsule.ReceiptCapsule;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.core.exception.BalanceInsufficientException;
import org.tron.core.exception.ContractExeException;
import org.tron.core.exception.ContractValidateException;
import org.tron.core.exception.ReceiptCheckErrException;
import org.tron.core.exception.VMIllegalException;
import org.tron.core.store.AbiStore;
import org.tron.core.store.AccountStore;
import org.tron.core.store.CodeStore;
import org.tron.core.store.ContractStore;
import org.tron.core.store.DynamicPropertiesStore;
import org.tron.core.store.StoreFactory;
import org.tron.protos.Protocol.Transaction;
import org.tron.protos.Protocol.Transaction.Contract.ContractType;
import org.tron.protos.Protocol.Transaction.Result.contractResult;
import org.tron.protos.contract.Common;
import org.tron.protos.contract.SmartContractOuterClass.SmartContract.ABI;
import org.tron.protos.contract.SmartContractOuterClass.TriggerSmartContract;

@Slf4j(topic = "DB")
public class TransactionTrace {

  private TransactionCapsule cyp;

  private ReceiptCapsule receipt;

  private StoreFactory storeFactory;

  private DynamicPropertiesStore dynamicPropertiesStore;

  private ContractStore contractStore;

  private AccountStore accountStore;

  private CodeStore codeStore;

  private AbiStore abiStore;

  private EnergyProcessor energyProcessor;

  private CypType cypType;

  private long txStartTimeInMs;

  private Runtime runtime;

  private ForkController forkController;

  @Getter
  private TransactionContext transactionContext;
  @Getter
  @Setter
  private TimeResultType timeResultType = TimeResultType.NORMAL;
  @Getter
  @Setter
  private boolean netFeeForBandwidth = true;

  public TransactionTrace(TransactionCapsule cyp, StoreFactory storeFactory,
      Runtime runtime) {
    this.cyp = cyp;
    Transaction.Contract.ContractType contractType = this.cyp.getInstance().getRawData()
        .getContract(0).getType();
    switch (contractType.getNumber()) {
      case ContractType.TriggerSmartContract_VALUE:
        cypType = CYP_CONTRACT_CALL_TYPE;
        break;
      case ContractType.CreateSmartContract_VALUE:
        cypType = CYP_CONTRACT_CREATION_TYPE;
        break;
      default:
        cypType = CypType.CYP_PRECOMPILED_TYPE;
    }
    this.storeFactory = storeFactory;
    this.dynamicPropertiesStore = storeFactory.getChainBaseManager().getDynamicPropertiesStore();
    this.contractStore = storeFactory.getChainBaseManager().getContractStore();
    this.codeStore = storeFactory.getChainBaseManager().getCodeStore();
    this.abiStore = storeFactory.getChainBaseManager().getAbiStore();
    this.accountStore = storeFactory.getChainBaseManager().getAccountStore();

    this.receipt = new ReceiptCapsule(Sha256Hash.ZERO_HASH);
    this.energyProcessor = new EnergyProcessor(dynamicPropertiesStore, accountStore);
    this.runtime = runtime;
    this.forkController = new ForkController();
    forkController.init(storeFactory.getChainBaseManager());
  }

  public TransactionCapsule getCyp() {
    return cyp;
  }

  private boolean needVM() {
    return this.cypType == CYP_CONTRACT_CALL_TYPE
        || this.cypType == CYP_CONTRACT_CREATION_TYPE;
  }

  public void init(BlockCapsule blockCap) {
    init(blockCap, false);
  }

  //pre transaction check
  public void init(BlockCapsule blockCap, boolean eventPluginLoaded) {
    txStartTimeInMs = System.currentTimeMillis();
    transactionContext = new TransactionContext(blockCap, cyp, storeFactory, false,
        eventPluginLoaded);
  }

  public void checkIsConstant() throws ContractValidateException, VMIllegalException {
    if (dynamicPropertiesStore.getAllowTvmConstantinople() == 1) {
      return;
    }
    TriggerSmartContract triggerContractFromTransaction = ContractCapsule
        .getTriggerContractFromTransaction(this.getCyp().getInstance());
    if (CYP_CONTRACT_CALL_TYPE == this.cypType) {
      ContractCapsule contract = contractStore
          .get(triggerContractFromTransaction.getContractAddress().toByteArray());
      if (contract == null) {
        throw new ContractValidateException(String.format("contract: %s is not in contract store",
            StringUtil.encode58Check(triggerContractFromTransaction
                .getContractAddress().toByteArray())));

      }
      ABI abi = contract.getInstance().getAbi();
      if (WalletUtil.isConstant(abi, triggerContractFromTransaction)) {
        throw new VMIllegalException("cannot call constant method");
      }
    }
  }

  //set bill
  public void setBill(long energyUsage) {
    if (energyUsage < 0) {
      energyUsage = 0L;
    }
    receipt.setEnergyUsageTotal(energyUsage);
  }

  public void setPenalty(long energyPenalty) {
    if (energyPenalty < 0) {
      energyPenalty = 0L;
    }
    receipt.setEnergyPenaltyTotal(energyPenalty);
  }

  //set net bill
  public void setNetBill(long netUsage, long netFee) {
    receipt.setNetUsage(netUsage);
    receipt.setNetFee(netFee);
  }

  public void setNetBillForCreateNewAccount(long netUsage, long netFee) {
    receipt.setNetUsage(netUsage);
    receipt.setNetFee(netFee);
    setNetFeeForBandwidth(false);
  }

  public void addNetBill(long netFee) {
    receipt.addNetFee(netFee);
  }

  public void exec()
      throws ContractExeException, ContractValidateException, VMIllegalException {
    /*  VM execute  */
    runtime.execute(transactionContext);
    setBill(transactionContext.getProgramResult().getEnergyUsed());
    setPenalty(transactionContext.getProgramResult().getEnergyPenaltyTotal());

//    if (CypType.CYP_PRECOMPILED_TYPE != cypType) {
//      if (contractResult.OUT_OF_TIME
//          .equals(receipt.getResult())) {
//        setTimeResultType(TimeResultType.OUT_OF_TIME);
//      } else if (System.currentTimeMillis() - txStartTimeInMs
//          > CommonParameter.getInstance()
//          .getLongRunningTime()) {
//        setTimeResultType(TimeResultType.LONG_RUNNING);
//      }
//    }
  }

  public void saveEnergyLeftOfOrigin(long energyLeft) {
    receipt.setOriginEnergyLeft(energyLeft);
  }

  public void saveEnergyLeftOfCaller(long energyLeft) {
    receipt.setCallerEnergyLeft(energyLeft);
  }

  public void finalization() throws ContractExeException {
    try {
      pay();
    } catch (BalanceInsufficientException e) {
      throw new ContractExeException(e.getMessage());
    }
    if (StringUtils.isEmpty(transactionContext.getProgramResult().getRuntimeError())) {
      for (DataWord contract : transactionContext.getProgramResult().getDeleteAccounts()) {
        deleteContract(contract.toTronAddress());
      }
    }
  }

  /**
   * pay actually bill(include ENERGY and storage).
   */
  public void pay() throws BalanceInsufficientException {
    byte[] originAccount;
    byte[] callerAccount;
    long percent = 0;
    long originEnergyLimit = 0;
    switch (cypType) {
      case CYP_CONTRACT_CREATION_TYPE:
        callerAccount = cyp.getOwnerAddress();
        originAccount = callerAccount;
        break;
      case CYP_CONTRACT_CALL_TYPE:
        TriggerSmartContract callContract = ContractCapsule
            .getTriggerContractFromTransaction(cyp.getInstance());
        ContractCapsule contractCapsule =
            contractStore.get(callContract.getContractAddress().toByteArray());

        callerAccount = callContract.getOwnerAddress().toByteArray();
        originAccount = contractCapsule.getOriginAddress();
        percent = Math
            .max(Constant.ONE_HUNDRED - contractCapsule.getConsumeUserResourcePercent(), 0);
        percent = Math.min(percent, Constant.ONE_HUNDRED);
        originEnergyLimit = contractCapsule.getOriginEnergyLimit();
        break;
      default:
        return;
    }

    // originAccount Percent = 30%
    AccountCapsule origin = accountStore.get(originAccount);
    AccountCapsule caller = accountStore.get(callerAccount);
    if (dynamicPropertiesStore.supportUnfreezeDelay()
        && getRuntimeResult().getException() == null && !getRuntimeResult().isRevert()) {

      // just fo caller is not origin, we set the related field for origin account
      if (origin != null && !caller.getAddress().equals(origin.getAddress())) {
        resetAccountUsage(origin,
            receipt.getOriginEnergyUsage(),
            receipt.getOriginEnergyWindowSize(),
            receipt.getOriginEnergyMergedUsage(),
            receipt.getOriginEnergyMergedWindowSize(),
            receipt.getOriginEnergyWindowSizeV2());
      }

      resetAccountUsage(caller,
          receipt.getCallerEnergyUsage(),
          receipt.getCallerEnergyWindowSize(),
          receipt.getCallerEnergyMergedUsage(),
          receipt.getCallerEnergyMergedWindowSize(),
          receipt.getCallerEnergyWindowSizeV2());
    }
    receipt.payEnergyBill(
        dynamicPropertiesStore, accountStore, forkController,
        origin,
        caller,
        percent, originEnergyLimit,
        energyProcessor,
        EnergyProcessor.getHeadSlot(dynamicPropertiesStore));
  }

  private void resetAccountUsage(AccountCapsule accountCap,
      long usage, long size, long mergedUsage, long mergedSize, long size2) {
    if (dynamicPropertiesStore.supportAllowCancelAllUnfreezeV2()) {
      resetAccountUsageV2(accountCap, usage, size, mergedUsage, mergedSize, size2);
      return;
    }
    long currentSize = accountCap.getWindowSize(ENERGY);
    long currentUsage = accountCap.getEnergyUsage();
    // Drop the pre consumed frozen energy
    long newArea = currentUsage * currentSize
        - (mergedUsage * mergedSize - usage * size);
    // If area merging happened during suicide, use the current window size
    long newSize = mergedSize == currentSize ? size : currentSize;
    // Calc new usage by fixed x-axes
    long newUsage = Long.max(0, newArea / newSize);
    // Reset account usage and window size
    accountCap.setEnergyUsage(newUsage);
    accountCap.setNewWindowSize(ENERGY, newUsage == 0 ? 0L : newSize);
  }

  private void resetAccountUsageV2(AccountCapsule accountCap,
      long usage, long size, long mergedUsage, long mergedSize, long size2) {
    long currentSize = accountCap.getWindowSize(ENERGY);
    long currentSize2 = accountCap.getWindowSizeV2(ENERGY);
    long currentUsage = accountCap.getEnergyUsage();
    // Drop the pre consumed frozen energy
    long newArea = currentUsage * currentSize - (mergedUsage * mergedSize - usage * size);
    // If area merging happened during suicide, use the current window size
    long newSize = mergedSize == currentSize ? size : currentSize;
    long newSize2 = mergedSize == currentSize ? size2 : currentSize2;
    // Calc new usage by fixed x-axes
    long newUsage = Long.max(0, newArea / newSize);
    // Reset account usage and window size
    accountCap.setEnergyUsage(newUsage);
    accountCap.setNewWindowSizeV2(ENERGY, newUsage == 0 ? 0L : newSize2);
  }

  public boolean checkNeedRetry() {
    if (!needVM()) {
      return false;
    }
    return cyp.getContractRet() != contractResult.OUT_OF_TIME && receipt.getResult()
        == contractResult.OUT_OF_TIME;
  }

  public void check() throws ReceiptCheckErrException {
    if (!needVM()) {
      return;
    }
    if (Objects.isNull(cyp.getContractRet())) {
      throw new ReceiptCheckErrException(
          String.format("null resultCode id: %s", cyp.getTransactionId()));
    }
    if (!cyp.getContractRet().equals(receipt.getResult())) {
      throw new ReceiptCheckErrException(String.format(
          "different resultCode txId: %s, expect: %s, actual: %s",
          cyp.getTransactionId(), cyp.getContractRet(), receipt.getResult()));
    }
  }

  public ReceiptCapsule getReceipt() {
    return receipt;
  }

  public void setResult() {
    if (!needVM()) {
      return;
    }
    receipt.setResult(transactionContext.getProgramResult().getResultCode());
  }

  public String getRuntimeError() {
    return transactionContext.getProgramResult().getRuntimeError();
  }

  public ProgramResult getRuntimeResult() {
    return transactionContext.getProgramResult();
  }

  public Runtime getRuntime() {
    return runtime;
  }

  public void deleteContract(byte[] address) {
    abiStore.delete(address);
    codeStore.delete(address);
    accountStore.delete(address);
    contractStore.delete(address);
  }

  public static byte[] convertToTronAddress(byte[] address) {
    if (address.length == 20) {
      byte[] newAddress = new byte[21];
      byte[] temp = new byte[] {DecodeUtil.addressPreFixByte};
      System.arraycopy(temp, 0, newAddress, 0, temp.length);
      System.arraycopy(address, 0, newAddress, temp.length, address.length);
      address = newAddress;
    }
    return address;
  }

  public enum TimeResultType {
    NORMAL,
    LONG_RUNNING,
    OUT_OF_TIME
  }
}
