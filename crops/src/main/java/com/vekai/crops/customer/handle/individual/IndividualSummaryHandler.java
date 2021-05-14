package com.vekai.crops.customer.handle.individual;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
//import com.vekai.crops.constant.DocListCode;
import com.vekai.crops.constant.dict.DictCertType;
import com.vekai.crops.constant.dict.DictCountry;
import com.vekai.crops.customer.entity.CustIndividualPO;
import com.vekai.crops.customer.entity.CustPermitPO;
import com.vekai.crops.customer.entity.CustomerPO;
import com.vekai.crops.customer.service.CustomerService;
import com.vekai.appframe.support.PersonIdCard;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class IndividualSummaryHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder beanCruder;
//    @Autowired
//    DocListService docListService;
    @Autowired
    CustomerService customerService;


    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {

        String certId = object.getValue("certId").strValue();
        String custName = object.getValue("custName").strValue();
        String phone = object.getValue("phone").strValue();
        CustomerPO customerPO = new CustomerPO();

        customerPO.setCustType(BizConst.CUSTOMER_TYPE_IND);
        customerPO.setCustName(custName);
        customerPO.setCertId(certId);
        customerPO.setCertCountry(DictCountry.CHN);
        customerPO.setCertType(DictCertType.ID_CARD);

        validateCustomerHasExisted(certId,customerPO.getCertType());

        Integer result = beanCruder.save(customerPO);
        if (result > 0) {

            CustIndividualPO individual = new CustIndividualPO();
            individual.setCustId(customerPO.getCustId());
            individual.setCertId(certId);
            individual.setChnName(customerPO.getCustName());

            try{
                PersonIdCard personIdCard = new PersonIdCard(certId);
                Date now = DateKit.now();
                Date birth = personIdCard.getBirthday();
                int age = DateKitAdapter.getRangeYears(birth,now);
                individual.setGender(personIdCard.getGender());
                individual.setBirth(birth);
                individual.setAge(age);
            }catch (Exception e){

            }

            individual.setCellPhoneNo(phone);
            result += beanCruder.save(individual);
            createPermit(customerPO.getCustId());

//            result += docListService.initDocList(DocListCode.DRIVER,customerPO.getCustId(), ObjectType.DRIVER.toString());
            return result;
        } else {
            return 0;
        }
    }

    private void createPermit(String custId) {
        CustPermitPO custPermitPO = new CustPermitPO();
        custPermitPO.setAllowView(BizConst.YES_NO_Y);
        custPermitPO.setAllowHold(BizConst.YES_NO_Y);
        custPermitPO.setAllowEdit(BizConst.YES_NO_Y);
        custPermitPO.setAllowBusiness(BizConst.YES_NO_Y);
        custPermitPO.setUserId(AuthHolder.getUser().getId());
        custPermitPO.setCustId(custId);
        beanCruder.save(custPermitPO);
    }

    private void validateCustomerHasExisted(String certId,String certType) {
        CustomerPO customerPO = customerService.getCustByCertIdAndcertType(certId,certType);
        if(null != customerPO)
            throw new BizException("该身份证号所对应的客户已存在");
    }

    /**
     * DateKit暂时不支持的功能，在这里暂时实现下，下个版本DateKit将会实现掉
     */
    public static class DateKitAdapter{
        /**
         * 取两个日期间隔的年数
         *
         * @param date1 date1
         * @param date2 date2
         * @return int
         */
        public static int getRangeYears(Date date1, Date date2){
            DateTime begin = new DateTime(date1);
            DateTime end = new DateTime(date2);
            Period period = new Period(begin,end, PeriodType.years());
            return period.getYears();
        }
    }
}
