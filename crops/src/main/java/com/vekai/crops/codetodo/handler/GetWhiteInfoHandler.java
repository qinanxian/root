package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GetWhiteInfoHandler extends MapDataListHandler {

    protected final Logger logger = LoggerFactory.getLogger(GetWhiteInfoHandler.class);

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex){
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<MapObject> dataList = result.getDataList();
        dataList.stream().forEach(mapData -> {
            String dayoffFlag = mapData.getValue("dayoffFlag").strValue();
            String mobileNo = mapData.getValue("mobileNo").strValue();
            //对轮休情况做处理
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
                mapData.putValue("dayoffFlag",newFlag.toString());
            }else{
                mapData.putValue("dayoffFlag","");
            }
            //对手机号做处理
            if(isPhone(mobileNo)){
                mapData.putValue("mobileNo",mobileNo);
            }else {
                mapData.putValue("mobileNo","");
            }
        });
        return result;
    }

    //校验手机号是否正确
    private boolean isPhone(String str) {
        String regex = "^1(3[0-9]|4[01456879]|5[0-3,5-9]|6[2567]|7[0-8]|8[0-9]|9[0-3,5-9])\\d{8}$";
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
