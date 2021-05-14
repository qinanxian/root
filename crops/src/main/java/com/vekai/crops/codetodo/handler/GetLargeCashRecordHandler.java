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

@Component
public class GetLargeCashRecordHandler extends MapDataListHandler {

    protected final Logger logger = LoggerFactory.getLogger(GetLargeCashRecordHandler.class);

    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex){
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<MapObject> dataList = result.getDataList();
        dataList.stream().forEach(mapData -> {
            String resvAmt = mapData.getValue("resvAmt").strValue();
            if(isNotEmpty(resvAmt)){
                String newResvAmt = resvAmt.replaceAll(",","");
                mapData.putValue("resvAmt",newResvAmt);
            }else{
                mapData.putValue("resvAmt","");
            }
        });
        return result;
    }

    //判断是否为空
    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
}
