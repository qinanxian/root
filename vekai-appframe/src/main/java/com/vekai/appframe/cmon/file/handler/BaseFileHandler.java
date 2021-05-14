package com.vekai.appframe.cmon.file.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.base.aspect.DataFormVirNameConverter;
import com.vekai.auth.service.AuthService;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BaseFileHandler extends MapDataListHandler {

    private static final Long RATE = 1024L;
    private static final String DEFAULT_SIZE = "0KB";

    @Autowired
    AuthService authService;

    @Override
    public void initDataForm(DataForm dataForm) {
        dataForm.getProperties().put(DataFormVirNameConverter.CLOSE_NAME_AUTO_CONVERT, "Y");
        super.initDataForm(dataForm);
    }

    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        PaginResult<MapObject> result = super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
        List<MapObject> mapObjectList = result.getDataList();
        mapObjectList.forEach(mapData -> {
            Object fileSize = mapData.get("fileSize");
            if (null == fileSize) {
                mapData.put("fileSize", DEFAULT_SIZE);
            } else {
                mapData.put("fileSize", rebuildFileSize(String.valueOf(fileSize)));
            }

            String createdBy = ValueObject.valueOf(mapData.get("createdBy")).strValue();
            if (!StringKit.isBlank(createdBy)) {
                //登记用户，部门
                mapData.put("createdUserName", authService.getUserNameById(createdBy));
                mapData.put("createdOrgName", authService.getUserOrgName(createdBy));
            }

            String updatedBy = ValueObject.valueOf(mapData.get("updatedBy")).strValue();
            if (!StringKit.isBlank(updatedBy)) {
                //更新用户，部门
                mapData.put("updatedUserName", authService.getUserNameById(updatedBy));
                mapData.put("updatedOrgName", authService.getUserOrgName(updatedBy));
            }
        });

        return result;
    }

    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        Integer result = 0;
        if (null != dataList) {
            for (MapObject mapObject : dataList) {
                String itemId = String.valueOf(mapObject.get("itemId"));
                MapObject docListItem = mapObjectCruder.selectOne(
                        "SELECT * FROM CMON_DOCLIST_ITEM WHERE ITEM_ID=:itemId", "itemId", itemId);
                if (null != docListItem) {
                    Object itemCode = docListItem.get("itemCode");
                    if (null == itemCode) {
                        result += super.delete(dataForm, dataList);
                    } else {
                        throw new BizException(MessageHolder.getMessage("", "cmon.DocListItem.delete.message"));
                    }
                }
            }
        }

        return result;
    }


    private String rebuildFileSize(String fileSize) {
        Long target = Long.valueOf(fileSize);
        if (target <= RATE && target > 0) {
            return fileSize + SizeUnit.B;
        } else if (target > RATE && target <= Math.pow(RATE, 2)) {
            return String.valueOf(new BigDecimal(target).divide(new BigDecimal(RATE), 2, BigDecimal.ROUND_HALF_UP)) + SizeUnit.KB;
        } else {
            return String.valueOf(new BigDecimal(target).divide(new BigDecimal(Math.pow(RATE, 2)), 2, BigDecimal.ROUND_HALF_UP)) + SizeUnit.MB;
        }
    }

    enum SizeUnit {
        B, KB, MB
    }
}
