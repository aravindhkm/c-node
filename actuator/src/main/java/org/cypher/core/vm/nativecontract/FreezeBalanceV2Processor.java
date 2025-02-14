package org.tron.core.vm.nativecontract;

import static org.tron.core.actuator.ActuatorConstant.ACCOUNT_EXCEPTION_STR;
import static org.tron.core.actuator.ActuatorConstant.STORE_NOT_EXIST;
import static org.tron.core.config.Parameter.ChainConstant.CYP_PRECISION;
import static org.tron.protos.contract.Common.ResourceCode.BANDWIDTH;
import static org.tron.protos.contract.Common.ResourceCode.ENERGY;

import lombok.extern.slf4j.Slf4j;
import org.tron.common.utils.DecodeUtil;
import org.tron.common.utils.StringUtil;
import org.tron.core.capsule.AccountCapsule;
import org.tron.core.exception.ContractValidateException;
import org.tron.core.store.DynamicPropertiesStore;
import org.tron.core.vm.nativecontract.param.FreezeBalanceV2Param;
import org.tron.core.vm.repository.Repository;

@Slf4j(topic = "VMProcessor")
public class FreezeBalanceV2Processor {

  public void validate(FreezeBalanceV2Param param, Repository repo) throws ContractValidateException {
    if (repo == null) {
      throw new ContractValidateException(STORE_NOT_EXIST);
    }

    byte[] ownerAddress = param.getOwnerAddress();
    if (!DecodeUtil.addressValid(ownerAddress)) {
      throw new ContractValidateException("Invalid address");
    }
    AccountCapsule ownerCapsule = repo.getAccount(ownerAddress);
    if (ownerCapsule == null) {
      String readableOwnerAddress = StringUtil.createReadableString(ownerAddress);
      throw new ContractValidateException(
          ACCOUNT_EXCEPTION_STR + readableOwnerAddress + "] does not exist");
    }
    long frozenBalance = param.getFrozenBalance();
    if (frozenBalance <= 0) {
      throw new ContractValidateException("FrozenBalance must be positive");
    } else if (frozenBalance < CYP_PRECISION) {
      throw new ContractValidateException("FrozenBalance must be greater than or equal to 1 CYP");
    } else if (frozenBalance > ownerCapsule.getBalance()) {
      throw new ContractValidateException(
          "FrozenBalance must be less than or equal to accountBalance");
    }

    // validate arg @resourceType
    switch (param.getResourceType()) {
      case BANDWIDTH:
      case ENERGY:
        break;
      case CYPHER_POWER:
        if (!repo.getDynamicPropertiesStore().supportAllowNewResourceModel()) {
          throw new ContractValidateException(
              "Unknown ResourceCode, valid ResourceCode[BANDWIDTH、ENERGY]");
        }
        break;
      default:
        if (repo.getDynamicPropertiesStore().supportAllowNewResourceModel()) {
          throw new ContractValidateException(
              "Unknown ResourceCode, valid ResourceCode[BANDWIDTH、ENERGY、CYPHER_POWER]");
        } else {
          throw new ContractValidateException(
              "Unknown ResourceCode, valid ResourceCode[BANDWIDTH、ENERGY]");
        }
    }
  }

  public void execute(FreezeBalanceV2Param param, Repository repo) {
    DynamicPropertiesStore dynamicStore = repo.getDynamicPropertiesStore();

    byte[] ownerAddress = param.getOwnerAddress();
    long frozenBalance = param.getFrozenBalance();
    AccountCapsule accountCapsule = repo.getAccount(ownerAddress);
    if (dynamicStore.supportAllowNewResourceModel()
        && accountCapsule.oldTronPowerIsNotInitialized()) {
      accountCapsule.initializeOldTronPower();
    }
    switch (param.getResourceType()) {
      case BANDWIDTH:
        long oldNetWeight = accountCapsule.getFrozenV2BalanceWithDelegated(BANDWIDTH) / CYP_PRECISION;
        accountCapsule.addFrozenBalanceForBandwidthV2(frozenBalance);
        long newNetWeight = accountCapsule.getFrozenV2BalanceWithDelegated(BANDWIDTH) / CYP_PRECISION;
        repo.addTotalNetWeight(newNetWeight - oldNetWeight);
        break;
      case ENERGY:
        long oldEnergyWeight = accountCapsule.getFrozenV2BalanceWithDelegated(ENERGY) / CYP_PRECISION;
        accountCapsule.addFrozenBalanceForEnergyV2(frozenBalance);
        long newEnergyWeight = accountCapsule.getFrozenV2BalanceWithDelegated(ENERGY) / CYP_PRECISION;
        repo.addTotalEnergyWeight(newEnergyWeight - oldEnergyWeight);
        break;
      case CYPHER_POWER:
        long oldTPWeight = accountCapsule.getTronPowerFrozenV2Balance() / CYP_PRECISION;
        accountCapsule.addFrozenForTronPowerV2(frozenBalance);
        long newTPWeight = accountCapsule.getTronPowerFrozenV2Balance() / CYP_PRECISION;
        repo.addTotalTronPowerWeight(newTPWeight - oldTPWeight);
        break;
      default:
        logger.debug("Resource Code Error.");
    }

    // deduce balance of owner account
    long newBalance = accountCapsule.getBalance() - frozenBalance;
    accountCapsule.setBalance(newBalance);
    repo.updateAccount(accountCapsule.createDbKey(), accountCapsule);
  }
}
