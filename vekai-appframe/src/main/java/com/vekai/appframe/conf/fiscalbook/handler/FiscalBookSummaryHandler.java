package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.conf.fiscalbook.service.FiscalBookConfService;
import com.vekai.auth.entity.Corp;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.autoconfigure.FiscalProperties;
import com.vekai.fiscal.entity.ConfFiscGaapAssistPO;
import com.vekai.fiscal.entity.ConfFiscGaapEntryPO;
import com.vekai.fiscal.entity.FiscBookAssistPO;
import com.vekai.fiscal.entity.FiscBookEntryPO;
import com.vekai.fiscal.entity.FiscBookPO;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyu on 2018/6/29.
 */
@Component
public class FiscalBookSummaryHandler extends MapDataOneHandler {

    @Autowired
    FiscalBookConfService fiscalBookConfService;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    FiscalProperties fiscalProperties;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String gaapDef = object.getValue("gaapDef").strValue();
        String bookCode = object.getValue("bookCode").strValue();
        String mainCorpId = object.getValue("mainCorpId").strValue();
        List<ConfFiscGaapEntryPO> confFiscGaapEntryPOList = fiscalBookConfService.getConfFiscGaapEntry(gaapDef);

        List<FiscBookAssistPO>  fiscBookAssistPOList = new ArrayList<>();
        for (ConfFiscGaapEntryPO confFiscGaapEntry : confFiscGaapEntryPOList) {
            FiscBookEntryPO fiscBookEntryPO = new FiscBookEntryPO();
            fiscBookEntryPO.setBookCode(bookCode);

            BeanKit.copyProperties(confFiscGaapEntry,fiscBookEntryPO);
            beanCruder.save(fiscBookEntryPO);

            String gaapEntryDef = confFiscGaapEntry.getGaapEntryDef();
            List<ConfFiscGaapAssistPO> confFiscGaapAssistPOList = fiscalBookConfService.getConfFiscGaapAssists(gaapEntryDef);
            for (ConfFiscGaapAssistPO confFiscGaapAssistPO : confFiscGaapAssistPOList) {
                FiscBookAssistPO fiscBookAssistPO = new FiscBookAssistPO();
                fiscBookAssistPO.setBookEntryId(fiscBookEntryPO.getBookEntryId());
                BeanKit.copyProperties(confFiscGaapAssistPO,fiscBookAssistPO);
                fiscBookAssistPOList.add(fiscBookAssistPO);
            }
        }
        beanCruder.save(fiscBookAssistPOList);

        FiscBookPO fiscBookPO = new FiscBookPO();
        BeanKit.copyProperties(object,fiscBookPO);

        this.fillMainCorpInfo(fiscBookPO,mainCorpId);

        this.initPeriodYear(fiscBookPO);

        return beanCruder.save(fiscBookPO);
    }

    private void initPeriodYear(FiscBookPO fiscBookPO) {
        Integer startYear = fiscBookPO.getStartYear();
        fiscBookPO.setEndYear(fiscalProperties.getBookDefaultInitYear() + startYear -1);
    }

    private void fillMainCorpInfo(FiscBookPO fiscBookPO, String mainCorpId) {
        String sql = "SELECT * FROM AUTH_CORP WHERE CORP_ID=:mainCorpId";
        Corp corp = beanCruder.selectOne(Corp.class,sql,"mainCorpId",mainCorpId);
        if (corp == null)
            return;
        BeanKit.copyProperties(corp,fiscBookPO);
        fiscBookPO.setMainCorpId(mainCorpId);
        fiscBookPO.setMainCorpName(corp.getCorpName());
    }
}
