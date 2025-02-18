package org.tron.core.services.http;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.api.GrpcAPI.EstimateEnergyMessage;
import org.tron.api.GrpcAPI.Return;
import org.tron.api.GrpcAPI.TransactionExtention;
import org.tron.common.utils.ByteArray;
import org.tron.core.Wallet;
import org.tron.core.capsule.TransactionCapsule;
import org.tron.core.exception.ContractValidateException;
import org.tron.protos.Protocol;
import org.tron.protos.contract.SmartContractOuterClass.TriggerSmartContract;

@Component
@Slf4j(topic = "API")
public class EstimateEnergyServlet extends RateLimiterServlet {

  @Autowired
  private Wallet wallet;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    TriggerSmartContract.Builder build = TriggerSmartContract.newBuilder();
    TransactionExtention.Builder cypExtBuilder = TransactionExtention.newBuilder();
    EstimateEnergyMessage.Builder estimateEnergyBuilder = EstimateEnergyMessage.newBuilder();
    Return.Builder retBuilder = Return.newBuilder();
    boolean visible = false;
    try {
      String contract = request.getReader().lines()
          .collect(Collectors.joining(System.lineSeparator()));
      Util.checkBodySize(contract);
      visible = Util.getVisiblePost(contract);
      Util.validateParameter(contract);
      JsonFormat.merge(contract, build, visible);
      JSONObject jsonObject = JSONObject.parseObject(contract);

      boolean isFunctionSelectorSet =
          !StringUtil.isNullOrEmpty(jsonObject.getString(Util.FUNCTION_SELECTOR));
      if (isFunctionSelectorSet) {
        String selector = jsonObject.getString(Util.FUNCTION_SELECTOR);
        String parameter = jsonObject.getString(Util.FUNCTION_PARAMETER);
        String data = Util.parseMethod(selector, parameter);
        build.setData(ByteString.copyFrom(ByteArray.fromHexString(data)));
      }

      TransactionCapsule cypCap = wallet.createTransactionCapsule(build.build(),
          Protocol.Transaction.Contract.ContractType.TriggerSmartContract);

      wallet.estimateEnergy(build.build(), cypCap,
          cypExtBuilder, retBuilder, estimateEnergyBuilder);
    } catch (ContractValidateException e) {
      retBuilder.setResult(false).setCode(Return.response_code.CONTRACT_VALIDATE_ERROR)
          .setMessage(ByteString.copyFromUtf8(e.getMessage()));
    } catch (Exception e) {
      String errString = null;
      if (e.getMessage() != null) {
        errString = e.getMessage().replaceAll("[\"]", "\'");
      }
      retBuilder.setResult(false).setCode(Return.response_code.OTHER_ERROR)
          .setMessage(ByteString.copyFromUtf8(e.getClass() + " : " + errString));
    }
    estimateEnergyBuilder.setResult(retBuilder);
    response.getWriter().println(
        Util.printEstimateEnergyMessage(estimateEnergyBuilder.build(), visible));
  }
}
