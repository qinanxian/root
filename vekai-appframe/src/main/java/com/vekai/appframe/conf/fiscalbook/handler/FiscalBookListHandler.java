package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.conf.fiscalbook.service.FiscalBookConfService;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.entity.FiscBookEntryPO;
import com.vekai.fiscal.entity.FiscVoucherPO;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2018/6/5.
 */
@Component
public class FiscalBookListHandler extends MapDataListHandler {

    @Autowired
    FiscalBookConfService fiscalBookService;
    @Autowired
    BeanCruder beanCruder;

    @Transactional
    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        checkFiscVoucherExist(dataList);

        for (MapObject mapObject : dataList) {
            String bookCode = mapObject.getValue("bookCode").strValue();
            String deleteEntrySql = "SELECT * FROM FISC_BOOK_ENTRY WHERE BOOK_CODE =:bookCode";
            List<FiscBookEntryPO> fiscBookEntryPOList = beanCruder.selectList(
                    FiscBookEntryPO.class,deleteEntrySql, MapKit.mapOf("bookCode",bookCode));
            for (FiscBookEntryPO fiscBookEntryPO : fiscBookEntryPOList) {
                String sql = "DELETE FROM FISC_BOOK_ASSIST WHERE BOOK_ENTRY_ID=:bookEntryId";
                beanCruder.execute(sql,MapKit.mapOf("bookEntryId",fiscBookEntryPO.getBookEntryId()));
            }
            if (fiscBookEntryPOList.size() > 0) {
                beanCruder.delete(fiscBookEntryPOList);
            }

            String deletePeriodSql = "DELETE FROM FISC_BOOK_PERIOD WHERE BOOK_CODE=:bookCode";
            beanCruder.execute(deletePeriodSql,MapKit.mapOf("bookCode",bookCode));

        }
        return super.delete(dataForm,dataList);
    }

    private void checkFiscVoucherExist(List<MapObject> dataList) {
        List<MapObject> mapObjects = dataList.stream()
                .filter(mapData -> this.checkFiscVoucher(mapData.getValue("bookCode").strValue()))
                .collect(Collectors.toList());
        if (mapObjects.size() > 0)
            throw new BizException(MessageHolder.getMessage("","conf.fiscal.voucher.already.exist"));
    }

    private boolean checkFiscVoucher(String bookCode) {
        List<FiscVoucherPO> fiscVoucherPOList = fiscalBookService.getFiscVoucher(bookCode);
        if (fiscVoucherPOList.size() > 0)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
