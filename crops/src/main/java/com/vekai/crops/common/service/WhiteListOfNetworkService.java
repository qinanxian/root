package com.vekai.crops.common.service;

import cn.fisok.raw.lang.ValueObject;
import com.vekai.office.excel.reader.ExcelRowData;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WhiteListOfNetworkService {

    public void getWhiteListOfNetwork(List<ExcelRowData> data){
        for (ExcelRowData datum : data) {
            String s = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String, ValueObject> rowData = datum.getRowData();
            rowData.put("WHITE_ID",ValueObject.valueOf(s));
        }
    }
}
