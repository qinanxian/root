package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.entity.FiscBookEntryPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/6/6.
 */
@Component
public class FiscBookEntrySummaryHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder beanCruder;

    @Transactional
    @Override
    public int save(DataForm dataForm, MapObject object) {
        String entryCode = object.getValue("entryCode").strValue();
        String entryName = object.getValue("entryName").strValue();

        this.validateEntryExist(entryCode,entryName);
        FiscBookEntryPO fiscBookEntry = this.initFiscBookEntry();
        BeanKit.copyProperties(object,fiscBookEntry);
        return beanCruder.save(fiscBookEntry);
    }

    private void validateEntryExist(String entryCode, String entryName) {
        String sql = "SELECT * FROM FISC_BOOK_ENTRY WHERE ENTRY_CODE=:entryCode";
        List<FiscBookEntryPO> fiscBookEntryPOList =
                beanCruder.selectList(FiscBookEntryPO.class,sql,"entryCode",entryCode);
        if (fiscBookEntryPOList.size() > 0)
            throw new BizException(MessageHolder.getMessage("","conf.fiscal.book.entry.has.exist"));
        String nameSql = "SELECT * FROM FISC_BOOK_ENTRY WHERE ENTRY_NAME=:entryName";
        List<FiscBookEntryPO> fiscBookEntryPOs =
                beanCruder.selectList(FiscBookEntryPO.class,nameSql,"entryName",entryName);
        if (fiscBookEntryPOs.size() > 0)
            throw new BizException(MessageHolder.getMessage("","conf.fiscal.book.entry.name.has.exist"));
    }

    private FiscBookEntryPO initFiscBookEntry() {
        FiscBookEntryPO fiscBookEntry = new FiscBookEntryPO();
        fiscBookEntry.setStatus(AppframeConst.EFFECT_STATUS_VALID);
        SerialNumberGenerator<String> serialNumberGenerator =
                serialNumberGeneratorFinder.find(FiscalBookInfoHandler.FISC_BOOK_ENTRY_PO + ".BOOK_ENTRY_ID");
        String bookEntryId = serialNumberGenerator.next(FiscalBookInfoHandler.FISC_BOOK_ENTRY_PO + ".BOOK_ENTRY_ID");
        fiscBookEntry.setBookEntryId(bookEntryId);
        return fiscBookEntry;
    }

}
