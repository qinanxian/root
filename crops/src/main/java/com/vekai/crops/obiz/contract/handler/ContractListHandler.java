package com.vekai.crops.obiz.contract.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.obiz.application.service.ApplicationService;
import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.service.ContractService;
import com.vekai.crops.obiz.duebill.entity.ObizDuebill;
import com.vekai.crops.obiz.duebill.service.DuebillService;
import com.vekai.crops.obiz.lact.service.LactLoanService;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.common.landmark.dict.LandmarkItemStatus;
import com.vekai.common.landmark.service.LandmarkService;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 合同列表的处理
 */
@Component
public class ContractListHandler extends BeanDataListHandler<ObizContract> {
    @Autowired
    ContractService contractService;
    @Autowired
    LandmarkService landmarkService;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    private LactLoanService lactLoanService;
    @Autowired
    DuebillService duebillService;

    @Override
    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
        dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "operator");
    }

    /**
     *
     * @param contractId
     * @param status READY:待生效,ENABLE:有效,DISABLED:失效,IN_CHANGE:变更中
     * @return
     */
    public int updateContractStatus(String contractId,String status){
        String sql = "UPDATE OBIZ_CONTRACT " +
                "SET CONTRACT_STATUS=:contractStatus,UPDATED_BY=:updatedBy,UPDATED_TIME=:updatedTime " +
                "WHERE contract_id=:contractId";
        MapObject params = new MapObject();
        params.put("updatedBy", AuthHolder.getUser().getId());
        params.put("updatedTime", DateKit.now());
        params.put("contractStatus", status);
        params.put("contractId", contractId);

        return beanCruder.execute(sql,params);

    }

    public int ready(DataForm dataForm, MapObject mapObject){
        String contractId = mapObject.getValue("contractId").strValue();
        ValidateKit.notBlank(contractId,"数据异常，合同号不存在");

        return updateContractStatus(contractId,"READY");
    }

    public int start(DataForm dataForm, MapObject mapObject){
        String contractId = mapObject.getValue("contractId").strValue();
        ValidateKit.notBlank(contractId,"数据异常，合同号不存在");

        ObizContract contract = contractService.queryContract(contractId);
        ValidateKit.notBlank(contract.getBankCardOwner(),"合同[{0}]银行卡户名为空",contractId);
        ValidateKit.notBlank(contract.getBankCardNo(),"合同[{0}]银行卡号为空",contractId);
        ValidateKit.notBlank(contract.getBankCardIssuer(),"合同[{0}]银行卡开户行",contractId);

        return updateContractStatus(contractId,"ENABLE");
    }

    public int stop(DataForm dataForm, MapObject mapObject){
        String contractId = mapObject.getValue("contractId").strValue();
        ValidateKit.notBlank(contractId,"数据异常，合同号不存在");

        return updateContractStatus(contractId,"DISABLED");
    }


    public Integer delete(DataForm dataForm, List<ObizContract> dataList) {
        dataList.forEach(contract ->{
//            ObizContract contract = BeanKit.map2Bean(row,ObizContract.class);
            String landmarkId = contract.getLandmarkId();
            String applicationId = contract.getApplicationId();
            String contractStatus = contract.getContractStatus();
            if(!"READY".equals(contractStatus)){
                throw new BizException("合同{0}不是新增状态，不能删除",contract.getContractId());
            }

            //状态回到合同状态
            applicationService.updateAppMilestone(applicationId,"App6010");
            landmarkService.updateStepStatus(landmarkId,"App6010", LandmarkItemStatus.DOING);
//            //把当前状态重新处理
//            landmarkService.updateStepStatus(landmarkId,"App70", LandmarkItemStatus.INIT);
//            landmarkService.updateStepStatus(landmarkId,"App7010", LandmarkItemStatus.INIT);

            if(StringKit.isNotBlank(contract.getLactLoanId())){
                lactLoanService.deleteLactLoan(contract.getLactLoanId());
            }
        });

        return super.delete(dataForm, dataList);
    }

    /**
     * 发起放款，合同生成借据对象
     * @param dataForm
     * @param mapObject
     * @return
     */
    public int makeDuebill(DataForm dataForm, MapObject mapObject){
        String contractId = mapObject.getValue("contractId").strValue();
        ValidateKit.notBlank(contractId,"数据异常，合同号不存在");

        ObizContract contract = contractService.queryContract(contractId);
        ValidateKit.notNull(contract,"数据异常，合同号{0}对应的合同不存在",contractId);

        ObizDuebill duebill = duebillService.createDuebill(contract);

        return 1;
    }
}
