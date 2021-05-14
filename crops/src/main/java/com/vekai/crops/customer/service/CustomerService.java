package com.vekai.crops.customer.service;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.constant.dict.DictCertType;
import com.vekai.crops.constant.dict.DictCountry;
import com.vekai.crops.customer.entity.*;
import com.vekai.crops.customer.entity.CustEnterprisePO;
import com.vekai.crops.customer.entity.CustIndividualPO;
import com.vekai.crops.customer.handle.individual.IndividualSummaryHandler;
import com.vekai.appframe.support.PersonIdCard;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    MapObjectCruder mapObjectCruder;

//    public MapObject getCustBase(String custId){
//        return mapObjectCruder.selectOne("select * from CUST_BASE WEHRE CUST_ID=:custId",MapKit.mapOf("custId",custId));
//    }
//    public MapObject getCustInd(String custId){
//        return mapObjectCruder.selectOne("select * from CUST_IND WEHRE CUST_ID=:custId",MapKit.mapOf("custId",custId));
//    }

    /**
     * 使用三要素创建客户对象
     * @return 新客户客户号
     */
    public String insertIndCustomerBy3Elements(MapObject dataRow){
        //参数转换
        String custName = dataRow.getValue("custName").strValue();
        String idCardNo = dataRow.getValue("idCardNo").strValue();
        String cellPhoneNo = dataRow.getValue("cellPhoneNo").strValue();

        //1.检查三要素：姓名，身份证号，手机号
        //2.解析身份证
        //3.创建个人客户

        //构建对象
        CustomerPO base = new CustomerPO();
        base.setCustType(BizConst.CUSTOMER_TYPE_IND);
        base.setCertType(DictCertType.ID_CARD);
        base.setCertId(idCardNo);
        base.setCustName(custName);
        base.setCertCountry(DictCountry.CHN);
        beanCruder.insert(base);

        CustIndividualPO ind = new CustIndividualPO();
        ind.setCertId(idCardNo);
        ind.setChnName(custName);
        ind.setCellPhoneNo(cellPhoneNo);
        if(StringKit.isNotBlank(idCardNo)){
            try{
                PersonIdCard personIdCard = new PersonIdCard(idCardNo);

                Date now = DateKit.now();
                Date birth = personIdCard.getBirthday();
                int age = IndividualSummaryHandler.DateKitAdapter.getRangeYears(birth,now);

                ind.setBirth(personIdCard.getBirthday());
                ind.setGender(personIdCard.getGender());
                ind.setAge(age);
            }catch (Exception e){}
        }
        beanCruder.insert(ind);

        return base.getCustId();

    }

    public int saveCustPermit(CustPermitPO custPermitPO){
        return beanCruder.save(custPermitPO);
    }

    public CustEnterprisePO getEnterpriseByCustId(String custId) {
        CustEnterprisePO enterprise = beanCruder.selectOneById(CustEnterprisePO.class, custId);
        return enterprise;
    }

    public CustIndividualPO getIndividualByCustId(String custId) {
        CustIndividualPO individualPO = beanCruder.selectOneById(CustIndividualPO.class, custId);
        Optional.ofNullable(individualPO).orElseThrow(() -> new BizException(MessageHolder.getMessage(
                "","customer.individual.info.not.exist")));
        return individualPO;
    }

    public CustomerPO getCustomer(String custId) {
        String sql = "SELECT * FROM CUST_BASE WHERE CUST_ID=:custId";
        return beanCruder.selectOne(CustomerPO.class,sql, MapKit.mapOf("custId",custId));
    }

    public CustomerPO getIndividualByCustName(String custName) {
        String sql = "SELECT * FROM CUST_BASE WHERE CUST_NAME=:custName";
        return beanCruder.selectOne(CustomerPO.class,sql, MapKit.mapOf("custName",custName));
    }

    public CustEnterprisePO getEnterpriseByCustName(String custName) {
        String sql = "SELECT * FROM CUST_ENT WHERE CUST_NAME=:custName";
        return beanCruder.selectOne(CustEnterprisePO.class,sql, MapKit.mapOf("custName",custName));
    }

    public CustomerPO getCustByCertIdAndcertType(String certId,String certType) {
        String sql = "SELECT * FROM CUST_BASE WHERE CERT_ID=:certId and CERT_TYPE=:certType";
        return beanCruder.selectOne(CustomerPO.class,sql, MapKit.mapOf("certId",certId,"certType",certType));
    }

    public List<CustAccountPO> getAccountListByCustId(String custId) {
        String sql = "SELECT * FROM CUST_ACCOUNT WHERE CUST_ID=:custId ORDER BY ID ASC";
        List<CustAccountPO> accountS = beanCruder.selectList(CustAccountPO.class, sql, "custId", custId);
        return null == accountS ? Collections.emptyList() : accountS;
    }

    public CustAccountPO getDefaultAccountByCustId(String custId) {
        List<CustAccountPO> accountS = getAccountListByCustId(custId);
        if (accountS.size() > 0) {
            return accountS.get(0);
        }
        return null;
    }

    public CustomerPO getCustomerById(String custId) {
        CustomerPO customerPO = beanCruder.selectOneById(CustomerPO.class,custId);
        Optional.ofNullable(customerPO).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.info.not.exist")));
        return customerPO;
    }


    public CustRelationPO getCustRelation(String custRelationId) {
        String sql = "SELECT * FROM CUST_RELATION where id =:custRelationId";
        CustRelationPO relationPO = beanCruder.selectOne(CustRelationPO.class,sql,"custRelationId",custRelationId);
        Optional.ofNullable(relationPO).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.info.not.exist")));
        return relationPO;
    }

    public CustCapitalStructPO getCapitalStruct(String relationId) {
        String sql = "SELECT * FROM CUST_CAPITAL_STRUCT where RELATION_ID =:relationId";
        CustCapitalStructPO capitalStructPO = beanCruder.selectOne(CustCapitalStructPO.class,sql,"relationId",relationId);
        Optional.ofNullable(capitalStructPO).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.capitalStruct.not.exist")));
        return capitalStructPO;
    }

    public CustInvestPO getCustInvest(String relationId) {
        String sql = "SELECT * FROM CUST_INVEST where RELATION_ID =:relationId";
        CustInvestPO custInvestPO = beanCruder.selectOne(CustInvestPO.class,sql,"relationId",relationId);
        Optional.ofNullable(custInvestPO).orElseThrow(() -> new BizException(MessageHolder.getMessage("","customer.invest.not.exist")));
        return custInvestPO;
    }

    public CustPermitPO getCustPermit(String userId, String custId) {
        String permitSql = "SELECT * FROM CUST_PERMIT WHERE USER_ID=:userId AND CUST_ID=:custId";
        CustPermitPO custPermit = beanCruder.selectOne(CustPermitPO.class,permitSql,
                MapKit.mapOf("userId", userId,"custId",custId));
        return custPermit;
    }

    public String getRelationShip(String investmentWay) {
        String relationShip = null;
        switch (investmentWay) {
            case BizConst.INVESTMENT_WAY_FUND:
                relationShip = "200";
                break;
            case BizConst.INVESTMENT_WAY_MATERIAL_OBJECT:
                relationShip = "202";
                break;
            case BizConst.INVESTMENT_WAY_RIGHT:
                relationShip = "203";
                break;
            case BizConst.INVESTMENT_WAY_TECHNOLOGY:
                relationShip = "201";
                break;
            default:
                relationShip = "299";
                break;
        }
        return relationShip;
    }

}
