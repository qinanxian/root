package com.vekai.crops.obiz.contract.handler;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.base.SerialNoGeneratorFiller;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.crops.dossier.service.FormatDocService;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.crops.obiz.application.service.ApplicationService;
import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.model.ContractStatus;
import com.vekai.crops.obiz.lact.invoker.LactEngineInvoker;
import com.vekai.crops.obiz.lact.invoker.LactLoanCalcParam;
import com.vekai.crops.obiz.lact.service.LactLoanService;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.common.landmark.dict.LandmarkItemStatus;
import com.vekai.common.landmark.service.LandmarkService;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.type.*;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 申请签合同处理Handler
 */
@Component
public class AddNewContractInfoHandler extends BeanDataOneHandler<ObizContract> {
    @Autowired
    DossierService dossierService;
    @Autowired
    FormatDocService formatDocService;
    @Autowired
    LandmarkService landmarkService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    SerialNoGeneratorFiller serialNoGeneratorFiller;
    @Autowired
    private LactEngineInvoker invoker;
    @Autowired
    private LactLoanService lactLoanService;

    @Transactional
    public int save(DataForm dataForm, ObizContract contract) {
        String applicationId = contract.getApplicationId();
        ValidateKit.notBlank(applicationId,"申请号不存在，请确定数据的准确性");
        ObizApplication obizApplication = applicationService.queryApplication(applicationId);
        ValidateKit.notNull(obizApplication,"申请{0}不存在，请确定数据的准确性",applicationId);
        String landmarkId = obizApplication.getLandmarkId();

        //把显示模板上没有的字段都从申请中复制
        contract.setFunderId(obizApplication.getFunderId());
        contract.setPolicyId(obizApplication.getPolicyId());

        //把界面输入的值放到这个变量中来
        MapObject inputValues = new MapObject();
        dataForm.getElements().forEach(element->{
            inputValues.put(element.getCode(), BeanKit.getPropertyValue(contract,element.getCode()));
        });
        //使用申请以及界面本身录入的数据填充
        BeanKit.copyProperties(obizApplication,contract);
        BeanKit.copyProperties(inputValues,contract);


        //3. 先把流水号生成出来
        serialNoGeneratorFiller.execFill(contract);

        //4. 设置合同状态以及初始数据
        calcContractExpiryDate(contract);
        createFormatDocContractTex(contract);
        contract.setContractStatus(ContractStatus.READY.name());

        //5. 核算还款计划
        LactLoan lactLoan = calcLactLoan(contract);
        lactLoanService.fillSerialNo(lactLoan);
        lactLoanService.saveLactLoan(lactLoan);
        contract.setLactLoanId(lactLoan.getLoanId());


        //6. 进入合同状态
        applicationService.updateAppMilestone(applicationId,"App7010");
        landmarkService.updateStepStatus(landmarkId,"App70", LandmarkItemStatus.DOING);
        landmarkService.updateStepStatus(landmarkId,"App7010", LandmarkItemStatus.DOING);

        //7.生成格式化合同
        String contractDocDefKey = contract.getContractDocDefKey();
        if(StringKit.isNotBlank(contractDocDefKey)){
            Dossier dossier = formatDocService.createFormatDoc(contractDocDefKey, BizConst.CONTRACT,contract.getContractId());
            if(dossier!=null)contract.setContractDocId(dossier.getDossierId());
        }

        return super.save(dataForm, contract);
    }


    protected LactLoan calcLactLoan(ObizContract contract){
        LactLoanCalcParam param = new LactLoanCalcParam();
        param.loanAmt = contract.getContractAmt();                          //债权金额
        param.terms = contract.getTermMonth();                              //期数
        param.paymentPeriod = PaymentPeriod.M;                              //期数单位，还款周期：按月
        param.startDate = contract.getStartDate();                          //开始日期
        param.rateAppearMode = RateAppearMode.Indication;                   //利率表现形式:明示利率
        param.rateFloatType = RateFloatType.Fixed;                          //利率浮动方式:固定
        param.rateFloat = contract.getInterestRate();                       //利率浮动值:实际利率
        param.paymentMode = PaymentMode.valueOf(contract.getPaymentMode()); //还款方式
        param.payPointTime = PayPointTime.Postpay;                          //付款时点：后付
        param.interestCalcMode = InterestCalcMode.D;                        //计息方式：按日

        LactLoan lactLoan = invoker.invokeCalcPaymentScheduleList(param);
        return lactLoan;
    }

    public void calcContractExpiryDate(ObizContract contract){
        //根据月数计算到期日
        Date startDate = contract.getStartDate();
        int termMonth = contract.getTermMonth();
        Date expiryDate = DateKit.plusMonths(startDate,termMonth);
        contract.setExpiryDate(expiryDate);
    }

    /**
     * 创建格式化合同文本
     * @param contract
     */
    public void createFormatDocContractTex(ObizContract contract){
        String policyId = contract.getPolicyId();
        ValidateKit.notBlank(policyId,"产品号不存在，请确定数据的准确性");

        //2. 处理产品值并应用到进件对象中
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        PolicyDefinition policyDefinition = policyService.getPolicyDefinition(policyId);
        if(policyDefinition == null)throw new BizException("产品{0}不存在",policyId);

        //1.生成电子合同对象
        String contractDocDefKey = contract.getContractDocDefKey();
        if(StringKit.isNotBlank(contractDocDefKey)){
            Dossier dossier = formatDocService.createFormatDoc(contractDocDefKey, BizConst.CONTRACT,contract.getContractId());
            if(dossier!=null)contract.setContractDocId(dossier.getDossierId());
        }
    }

}
