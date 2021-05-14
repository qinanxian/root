package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EditWhiteInfoHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;
    /**
     * 多轮休情况处理
     *
     * @param dataForm
     * @param
     * @return
     */
    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters){
        MapObject query = super.query(dataForm, queryParameters);
        String dayoffFlag = query.getValue("dayoffFlag").strValue();
        String mobileNo = query.getValue("mobileNo").strValue();
        //处理轮休情况
        StringBuffer newFlag=new StringBuffer("");
        if((isNotEmpty(dayoffFlag))&&(isMatch(dayoffFlag))){
            for(int i=0;i<dayoffFlag.length();i++){
                if(dayoffFlag.substring(i,i+1).equals("1")){
                    if(newFlag.length()<1){
                        newFlag.append(String.valueOf(i+1));
                    }else{
                        newFlag.append(",").append(String.valueOf(i+1));
                    }
                }
            }
            query.putValue("dayoffFlag",newFlag.toString());
        }else{
            query.putValue("dayoffFlag","");
        }
        //处理手机号
        if(isPhone(mobileNo)){
            query.putValue("mobileNo",mobileNo);
        }else{
            query.putValue("mobileNo","");
        }

        return query;
    }

    /**
     * 对轮休情况做处理、手机号做校验
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String dayoffFlag="",mobileNo="";
        StringBuffer newFlag=new StringBuffer("0000000");
        String regex = "^1(3[0-9]|4[01456879]|5[0-3,5-9]|6[2567]|7[0-8]|8[0-9]|9[0-3,5-9])\\d{8}$";
        //轮休情况
        dayoffFlag = object.getValue("dayoffFlag").strValue();
        //员工手机号
        mobileNo = object.getValue("mobileNo").strValue();

        //校验员工手机号是否正确
        if(isNotEmpty(mobileNo)){
            if(mobileNo.length() != 11){
                throw new BizException("手机号应为11位数");
            }else{
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(mobileNo);
                boolean isMatch = m.matches();
                if(isMatch){
                    object.putValue("mobileNo",mobileNo);
                }else{
                    throw new BizException("您的手机号" + mobileNo + "格式错误！");
                }
            }
        }
        //轮休情况数据转换成“0或1”保存
        if (isNotEmpty(dayoffFlag)){
            String[] Flag = dayoffFlag.split(",");
            int count = Flag.length;
            for(int i=0;i<count;i++){
                int num = Integer.parseInt(Flag[i]);
                newFlag.replace((num-1),num,"1");
            }
        }
        object.putValue("dayoffFlag",newFlag.toString());

        return super.save(dataForm, object);
    }

    //校验手机号是否正确
    private boolean isPhone(String str) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(isNotEmpty(str)){
            if(str.length() != 11){
                return false;
            }else{
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                return m.matches();
            }
        }else{
            return false;
        }
    }
    //判断是否为空
    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
    //校验只有1或0
    private boolean isMatch(String str) {
        String regex = "^[0|1]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
