package com.vekai.crops.insure.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SelfRecInsureHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 本人领取保单，校验输入的手机号格式是否正确
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String recTime="" ,recPhone="";
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        //保单领取时间
        recTime = object.getValue("recTime").strValue();
        //领取人联系方式
        recPhone = object.getValue("recPhone").strValue();

        if((isNotEmpty(recTime))&&(recTime.length()>10)){
            recTime = recTime.substring(0,10);
        }
        object.putValue("recTime",recTime);

        if(recPhone.length() != 11){
            throw new BizException("手机号应为11位数");
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(recPhone);
            boolean isMatch = m.matches();
            if(isMatch){
                object.putValue("recPhone",recPhone);
            }else{
                throw new BizException("您的手机号" + recPhone + "格式错误！");
            }
        }
        return super.save(dataForm, object);
    }
    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
}
