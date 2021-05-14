package com.vekai.crops.obiz.application.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.base.SerialNoGeneratorFiller;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.crops.dossier.service.FormatDocService;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.service.PolicyApplyTo;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.appframe.policy.service.impl.PolicyApplyToBizObject;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.common.landmark.entity.CmonLandmark;
import com.vekai.common.landmark.service.LandmarkService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class NewApplicationInfoHandler extends MapDataOneHandler {
    @Autowired
    CustomerService customerService;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    DossierService dossierService;
    @Autowired
    FormatDocService formatDocService;
    @Autowired
    LandmarkService landmarkService;
    @Autowired
    SerialNoGeneratorFiller serialNoGeneratorFiller;

    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        int ret = 1;

        String policyId = object.getValue("policyId").strValue();
        Double applyAmt = object.getValue("applyAmt").doubleValue();

        //1. 检查客户是否存在，如果不存在，就创建他
        String custId = touchCustomer(object);

        ObizApplication application = new ObizApplication();
        BeanKit.copyProperties(object,application);
        application.setCustId(custId);
        application.setOperator(AuthHolder.getUser().getId());
        application.setBankCardOwner(application.getCustName());
        application.setAppStatus("CREATED");    //代码：AppStatus，创建状态
        application.setAppMilestone("App1010"); //申请录入

        //2. 处理产品值并应用到进件对象中
        PolicyService policyService = ApplicationContextHolder.getBean(PolicyService.class);
        PolicyDefinition policyDefinition = policyService.getPolicyDefinition(policyId);
        if(policyDefinition == null)throw new BizException("产品{0}不存在",policyId);

        Map<String,Object> properties = MapKit.newHashMap();    //产品字段和业务对象字段的转换映射
        PolicyApplyTo<Object, Map<String,Object>> policyApplyTo = new PolicyApplyToBizObject();
        policyApplyTo.setProperties(properties);
        policyApplyTo.applyTo(policyDefinition,application,MapKit.newHashMap());

        //3. 先把流水号生成出来
        serialNoGeneratorFiller.execFill(application);

        //4. 创建附属信息
        createAddonData(application);
        //5. 保存申请
        ret = beanCruder.save(application);

        return ret;
    }

    protected String touchCustomer(MapObject object){
        String custId = object.getValue("custId").strValue();
        String custName = object.getValue("custName").strValue();
        String custCertType = object.getValue("custCertType").strValue();
        String custCertId = object.getValue("custCertId").strValue();
        String custCellphone = object.getValue("custCellphone").strValue();

        //客户号为空，说明这个客户不是通过选择得到的，是直接新增的客户
        if (StringKit.isBlank(custId)) {

            MapObject row = new MapObject();

            row.put("custName", custName);
            row.put("custCertId", custCertId);
            row.put("cellPhoneNo", custCellphone);

            custId = customerService.insertIndCustomerBy3Elements(row);

            User user = AuthHolder.getUser();
            if(user != null){
                CustPermitPO permit = new CustPermitPO();
                permit.setCustId(custId);
                permit.setUserId(user.getId());
                permit.setAllowHold("Y");
                permit.setAllowBusiness("Y");
                permit.setAllowEdit("Y");
                permit.setAllowView("Y");
                customerService.saveCustPermit(permit);
            }
        }
        return custId;
    }

    /**
     * 创建附属信息 <br/>
     * 1.调查报告
     * 2.资料清单
     * 3.里程碑
     * @param application
     */
    protected void createAddonData(ObizApplication application){
        //1.生成调查报告对象
        String inquireDocDefKey = application.getInquireDocDefKey();
        if(StringKit.isNotBlank(inquireDocDefKey)){
            Dossier dossier = formatDocService.createFormatDoc(inquireDocDefKey, BizConst.APPLICATION,application.getApplicationId());
            if(dossier!=null)application.setInquireDocId(dossier.getDossierId());
        }

        //2.生成资料清单
        String dossierKey = application.getDossierDefKey();
        if(StringKit.isNotBlank(dossierKey)){
            Dossier dossier = dossierService.createDossier(dossierKey, BizConst.APPLICATION,application.getApplicationId());
            if(dossier!=null)application.setDossierId(dossier.getDossierId());
        }

        //3.创建里程碑对象
        String landmarkDefKey = application.getLandmarkDefKey();
        if(StringKit.isNotBlank(landmarkDefKey)){
            CmonLandmark landmark = landmarkService.createCmonLandmark(landmarkDefKey,BizConst.APPLICATION,application.getApplicationId());
            if(landmark != null)application.setLandmarkId(landmark.getLandmarkId());
        }
    }

}
