package com.vekai.crops.obiz.market.oppor.handler;

import com.vekai.crops.obiz.market.oppor.entity.ObizOpporIntent;
import com.vekai.appframe.support.PersonIdCard;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OpporIntentInfoHandler extends BeanDataOneHandler<ObizOpporIntent> {
    @Override
    public int save(DataForm dataForm, ObizOpporIntent row) {
        //如果是身份证，则解析下
        String certId = row.getCustCertId();
        if("110".equals(row.getCustCertType()) && StringKit.isNotBlank(certId)){
            try{
                PersonIdCard personIdCard = new PersonIdCard(certId);
                Date now = DateKit.now();
                row.setCustBirth(personIdCard.getBirthday());
                row.setCustGender(personIdCard.getGender());
                int age = DateKit.getRangeMonths(now,personIdCard.getBirthday());
                row.setCustAge(Math.abs((int)age/12));
            }catch(Exception e){
                //如果身份证号解析不正确，则不管他
                LogKit.warn("",e);
            }
        }
        return super.save(dataForm, row);
    }
}
