package com.vekai.crops.obiz.duebill.service;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.ObjectType;
import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.model.ContractStatus;
import com.vekai.crops.obiz.contract.service.ContractService;
import com.vekai.crops.obiz.duebill.entity.ObizDuebill;
import com.vekai.crops.obiz.duebill.model.DuebillStatus;
import com.vekai.crops.obiz.duebill.model.LeaseStatus;
import com.vekai.crops.obiz.tradetally.entity.ObizTradeTally;
import com.vekai.crops.obiz.tradetally.model.TradeTallyDataForm;
import com.vekai.crops.obiz.tradetally.service.TradeTallyService;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.fiscal.consts.ChargeupDirection;
import com.vekai.fiscal.event.service.EventData;
import com.vekai.fiscal.event.service.FisicalEventService;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DuebillService {
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    ContractService contractService;
    @Autowired
    FisicalEventService fisicalEventService;
    @Autowired
    TradeTallyService tradeTallyService;

    /**
     * 根据合同生成借据对象
     * @return
     */
    public ObizDuebill createDuebill(String contractId){
        ObizContract contract = contractService.queryContract(contractId);
        return createDuebill(contract);
    }

    /**
     * 根据合同生成借据对象
     * @return
     */
    public ObizDuebill createDuebill(ObizContract contract){
        validateContract(contract);

        ObizDuebill duebill = new ObizDuebill();

        copyContractToDuebill(contract,duebill);
        beanCruder.insert(duebill);

        return duebill;
    }

    private void validateContract(ObizContract contract){
        String contractId = contract.getContractId();
        ValidateKit.isTrue(ContractStatus.ENABLE.name().equals(contract.getContractStatus()),"合同[{0}]状态不正确，不能发起放款",contractId);
        ValidateKit.notBlank(contract.getBankCardOwner(),"合同[{0}]银行卡户名为空，不能发起放款",contractId);
        ValidateKit.notBlank(contract.getBankCardNo(),"合同[{0}]银行卡号为空，不能发起放款",contractId);
        ValidateKit.notBlank(contract.getBankCardIssuer(),"合同[{0}]银行卡开户行，不能发起放款",contractId);

        //检查合同是否已经放款过了
        String sql = "SELECT * FROM OBIZ_DUEBILL WHERE CONTRACT_ID=:contractId";
        int count = beanCruder.selectCount(sql,"contractId",contractId);
        ValidateKit.isTrue(count==0,"合同[{0}]已经发起放款了，不能再次发起",contractId);
    }

    /**
     * 根据合同生成借据
     * @param contract
     * @param duebill
     */
    private void copyContractToDuebill(ObizContract contract,ObizDuebill duebill){

        duebill.setContractId(contract.getContractId());
        duebill.setLactLoanId(contract.getLactLoanId());
        duebill.setApplicationId(contract.getApplicationId());
        duebill.setBusinessNo(contract.getBusinessNo());
        duebill.setCustId(contract.getCustId());
        duebill.setCustName(contract.getCustName());
        duebill.setPolicyId(contract.getPolicyId());
        duebill.setFunderId(contract.getFunderId());
        duebill.setInterestRate(contract.getInterestRate());
        duebill.setStartDate(contract.getStartDate());
        duebill.setExpiryDate(contract.getExpiryDate());
        duebill.setTermMonth(contract.getTermMonth());
        duebill.setPaymentPeriod(contract.getPaymentPeriod());
        duebill.setPaymentMode(contract.getPaymentMode());

        duebill.setLoanAccountIssuer(contract.getBankCardIssuer());
        duebill.setLoanAccountOwner(contract.getBankCardOwner());
        duebill.setLoanAccountNo(contract.getBankCardNo());
        duebill.setDuebillAmt(contract.getContractAmt());
        duebill.setDuebillBalance(0d);
        duebill.setNormalBalance(0d);
        duebill.setOverdueBalance(0d);
        duebill.setOverdueDays(0);

        duebill.setLeaseStatus(LeaseStatus.STARTED.toString());
        duebill.setDuebillStatus(DuebillStatus.READY.toString());
        duebill.setOperator(AuthHolder.getUser().getId());

    }

    public ObizDuebill getDuebill(String duebillId){
        String sql = "SELECT * FROM OBIZ_DUEBILL WHERE DUEBILL_ID=:duebillId";
        MapObject param = MapObject.valueOf("duebillId",duebillId);
        return beanCruder.selectOne(ObizDuebill.class,sql,param);
    }


    private EventData createEventData(ObizDuebill duebill){
        double occurAmt = duebill.getDuebillAmt();
        String currency = "CNY";        //暂时写死币种
//        String mainCorpId = "FB0001";   //暂时写死一个账套

        EventData eventData = new EventData(currency,occurAmt);
        eventData.setObjectType(ObjectType.BIZ_DUEBILL.name());// 借据
        eventData.setObjectId(duebill.getDuebillId());
        eventData.setOriginalCurrency(currency);
        eventData.setOriginalAmt(occurAmt);
        eventData.setExchangeRateType("MEDIAL_RATE");   //中间汇率
        eventData.setExchangeRateValue(1d);

        eventData.setOccurTime(DateKit.now());
//        eventData.setMainCorpId(mainCorpId);

        eventData.addParam("customerId",duebill.getCustId());
        eventData.addParam("customerName",duebill.getCustName());
        eventData.addParam("applicationId",duebill.getApplicationId());
        eventData.addParam("duebillId",duebill.getDuebillId());
        eventData.addParam("contractId",duebill.getContractId());

        return eventData;
    }

    private void fillTradeTally(ObizTradeTally tradeTally,ObizDuebill duebill){
        tradeTally.setApplicationId(duebill.getApplicationId());
        tradeTally.setBusinessNo(duebill.getBusinessNo());
        tradeTally.setContractId(duebill.getContractId());
        tradeTally.setDuebillId(duebill.getDuebillId());
        tradeTally.setCustId(duebill.getCustId());
        tradeTally.setCustName(duebill.getCustName());
        tradeTally.setLactLoanId(duebill.getLactLoanId());
        tradeTally.setDataFrom(TradeTallyDataForm.SELF.name());
    }
    /**
     * 发放贷款并且记账处理
     * @param duebillId
     * @return
     */
    public ObizDuebill makeLoanCharge(String duebillId){
        ObizDuebill duebill = getDuebill(duebillId);
        ValidateKit.notNull(duebill,"借据{0}不存在",duebillId);
        ValidateKit.isTrue(LeaseStatus.SUCCESS!=LeaseStatus.valueOf(duebill.getLeaseStatus()),"借据{0}已经放款入账",duebillId);

        String eventDef = "LoanIssuance";
        //1.调用放款交易
        EventData eventData = createEventData(duebill);
        fisicalEventService.publishEvent(eventDef,eventData);

        //2.更新借据以及更新合同
        duebill.setDuebillBalance(duebill.getDuebillAmt());
        duebill.setLeaseStatus(LeaseStatus.SUCCESS.toString());
        duebill.setDuebillStatus(DuebillStatus.EFFECT.toString());
        beanCruder.update(duebill);
        contractService.refreshContractBalance(duebill.getContractId());

        //3.记录交易流水
        ObizTradeTally tradeTally = new ObizTradeTally();
        fillTradeTally(tradeTally,duebill); //使用借据填充一些数据

        String tradeIntro = StringKit.format("向客户:{0},的合同:{1}，发放贷款",duebill.getCustName(),duebill.getContractId());
        tradeTally.setTradeEvent(eventDef);
        tradeTally.setTradeType("");
        tradeTally.setTradeIntro(tradeIntro);
        tradeTally.setBankCardIssuer(duebill.getLoanAccountIssuer());
        tradeTally.setBankCardOwner(duebill.getLoanAccountOwner());
        tradeTally.setBankCardNo(duebill.getLoanAccountNo());
        tradeTally.setTradeAmt(duebill.getDuebillAmt());
        tradeTally.setDirection(ChargeupDirection.Credit.name());
        tradeTallyService.appendTradeTally(tradeTally);

        return duebill;
    }

    /**
     * 反冲发放贷款记账处理
     * @param duebillId
     * @return
     */
    public ObizDuebill reverseLoanCharge(String duebillId){
        ObizDuebill duebill = getDuebill(duebillId);
        ValidateKit.notNull(duebill,"借据{0}不存在",duebillId);
        ValidateKit.isTrue(LeaseStatus.SUCCESS==LeaseStatus.valueOf(duebill.getLeaseStatus()),"借据{0}已经放款未入账",duebillId);

        String eventDef = "LoanIssuanceReverse";
        //1. 调用放款交易
        EventData eventData = createEventData(duebill);
        fisicalEventService.publishEvent(eventDef,eventData);

        //2. 更新借据以及更新合同
        duebill.setDuebillBalance(0d);
        duebill.setLeaseStatus(LeaseStatus.STARTED.toString());
        duebill.setDuebillStatus(DuebillStatus.READY.toString());
        beanCruder.update(duebill);
        contractService.refreshContractBalance(duebill.getContractId());

        //3.记录交易流水
        ObizTradeTally tradeTally = new ObizTradeTally();
        fillTradeTally(tradeTally,duebill); //使用借据填充一些数据

        String tradeIntro = StringKit.format("向客户:{0},的合同:{1}，反冲贷款",duebill.getCustName(),duebill.getContractId());
        tradeTally.setTradeEvent(eventDef);
        tradeTally.setTradeType("");
        tradeTally.setTradeIntro(tradeIntro);
        tradeTally.setBankCardIssuer(duebill.getLoanAccountIssuer());
        tradeTally.setBankCardOwner(duebill.getLoanAccountOwner());
        tradeTally.setBankCardNo(duebill.getLoanAccountNo());
        tradeTally.setTradeAmt(duebill.getDuebillAmt());
        tradeTally.setDirection(ChargeupDirection.Debit.name());
        tradeTallyService.appendTradeTally(tradeTally);

        return duebill;
    }
}
