package com.vekai.crops.codetodo.handler;

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
public class SaveWhiteInfoHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;
    /**
     * 对机构号、轮休情况做处理、手机号做校验
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String dayoffFlag="",mobileNo="",orgNo="";
        StringBuffer newFlag=new StringBuffer("0000000");
        String regex = "^1(3[0-9]|4[01456879]|5[0-3,5-9]|6[2567]|7[0-8]|8[0-9]|9[0-3,5-9])\\d{8}$";
        //轮休情况
        dayoffFlag = object.getValue("dayoffFlag").strValue();
        //员工手机号
        mobileNo = object.getValue("mobileNo").strValue();
        //机构号
        orgNo = object.getValue("orgNo").strValue();

        //选择的机构号必须是叶子节点结构，并且一个结构只有一条数据；
        String sql = "SELECT  ORG_LEVEL FROM AUTH_ORG WHERE ID =:orgNo";
        String level = beanCruder.selectOne(String.class, sql, "orgNo", orgNo);
        if(level.equals("4")){
            String sql2 = "SELECT ORG_NO FROM MSB_BUSI_DEYY_WHITE_LIST WHERE ORG_NO =:orgNo";
            String orgId = beanCruder.selectOne(String.class, sql2, "orgNo", orgNo);
            if(isNotEmpty(orgId)){
                throw new BizException("当前选择的机构已存在，无法新增");
            }else{
                object.putValue("orgNo",orgNo);
            }
        }else{
            throw new BizException("网点人员白名单配置，只针对网点请重新选择");
        }
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

    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
}
