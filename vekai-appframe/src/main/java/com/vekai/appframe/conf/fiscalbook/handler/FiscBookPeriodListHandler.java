package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.conf.fiscalbook.service.FiscalBookConfService;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.fiscal.autoconfigure.FiscalProperties;
import com.vekai.fiscal.entity.FiscBookPO;
import com.vekai.fiscal.entity.FiscBookPeriodPO;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2018/6/6.
 */
@Component
public class FiscBookPeriodListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    FiscalBookConfService fiscalBookService;
    @Autowired
    FiscalProperties fiscalProperties;


    @Transactional
    @Override
    public int save(DataForm dataForm, List<MapObject> dataList) {
        validateDateComplete(dataList);
        validateIsInUseConflict(dataList);
        dataList.forEach(mapData -> {
            if (mapData.getValue("isInUse").isBlank()) {
                mapData.put("isInUse","N");
            }
        });

        //反显帐套当前区间设置
        List<MapObject> filterDataList = dataList.stream()
                .filter(mapData -> AppframeConst.YES_NO_Y.equals(mapData.getValue("isInUse").strValue()))
                .collect(Collectors.toList());
        if (filterDataList.size() > 0) {
            String bookCode = filterDataList.get(0).getValue("bookCode").strValue();
            FiscBookPO fiscBookPO = fiscalBookService.getFiscBook(bookCode);
            fiscBookPO.setCurrentYear(filterDataList.get(0).getValue("periodYear").intValue());
            fiscBookPO.setCurrentTerm(filterDataList.get(0).getValue("periodTerm").intValue());
            beanCruder.save(fiscBookPO);
        }

        return super.save(dataForm,dataList);
    }

    private void validateIsInUseConflict(List<MapObject> dataList) {
        List<MapObject> filterDataList = dataList.stream().filter(mapData -> AppframeConst.YES_NO_Y.equals(mapData.getValue("isInUse").strValue())).collect(Collectors.toList());
        if (filterDataList.size() > 1)
            throw new BizException(MessageHolder.getMessage("","conf.fiscal.book.period.use.not.correct"));
    }

    private void validateDateComplete(List<MapObject> dataList) {
        if (dataList.size() == 0)
            return;
        MapObject firstMapObject = dataList.get(0);
        Integer periodYear = firstMapObject.getValue("periodYear").intValue();
        int dayNumOfYear = this.getDayOfYear(periodYear);
        int allDays = 0;
        for (MapObject mapObject :dataList) {
            allDays += (DateKit.getRangeDays(mapObject.getValue("startDate").dateValue(),
                    mapObject.getValue("endDate").dateValue())+1);
        }
        if (dayNumOfYear < allDays)
            throw new BizException(MessageHolder.getMessage(
                    "","conf.fiscal.book.period.date.not.correct"));

        dataList = dataList.stream()
                .sorted(Comparator.comparing(item -> item.getValue("periodTerm").intValue()))
                .collect(Collectors.toList());
        for (int periodTerm = 1; periodTerm < dataList.size();periodTerm ++ ) {
            Date lastEndDate = dataList.get(periodTerm - 1).getValue("endDate").dateValue();
            Date curStartDate = dataList.get(periodTerm).getValue("startDate").dateValue();
            if (DateKit.getRangeDays(lastEndDate,curStartDate) != 1)
                throw new BizException(MessageHolder.getMessage(
                        "","conf.fiscal.book.period.date.not.correct"));
        }
    }

    public int getDayOfYear(int year){
        LocalDate localDate = LocalDate.of(year, 12, 31);
        return localDate.getDayOfYear();
    }

    @Transactional
	public Integer initPeriodTerm(DataForm dataForm, MapObject object) {
		String bookCode = object.getValue("bookCode").strValue();
		String periodYear = object.getValue("periodYear").strValue();
		this.deletePeriod(bookCode,periodYear);
        FiscBookPO fiscBookPO = fiscalBookService.getFiscBook(bookCode);
        Boolean isCurrentYear = periodYear.equals(Integer.toString(fiscBookPO.getCurrentYear()));
        Boolean isStartYear = periodYear.equals(Integer.toString(fiscBookPO.getStartYear()));
        List<FiscBookPeriodPO> bookPeriodPOList = new ArrayList<>();
		for (int periodTerm = isStartYear ? fiscBookPO.getStartTerm():1; periodTerm < 13; periodTerm++ ) {
            Boolean isCurrentTerm = Boolean.FALSE;
            if (isCurrentYear) {
                isCurrentTerm = fiscBookPO.getCurrentTerm() == periodTerm;
            }
            FiscBookPeriodPO fiscBookPeriodPO = this.createFiscBookPeriod(bookCode,periodYear,periodTerm,isCurrentTerm);
            bookPeriodPOList.add(fiscBookPeriodPO);
        }
        beanCruder.save(bookPeriodPOList);
        return 1;
	}

    private void deletePeriod(String bookCode, String periodYear) {
        String sql = "DELETE FROM FISC_BOOK_PERIOD WHERE BOOK_CODE=:bookCode AND PERIOD_YEAR =:periodYear";
        beanCruder.execute(sql, MapKit.mapOf("bookCode",bookCode,"periodYear",Integer.parseInt(periodYear)));
    }

    private FiscBookPeriodPO createFiscBookPeriod(String bookCode, String periodYear, int periodTerm, Boolean isCurrentTerm) {
        FiscBookPeriodPO fiscBookPeriodPO = new FiscBookPeriodPO();
        fiscBookPeriodPO.setPeriodTerm(periodTerm);
        fiscBookPeriodPO.setPeriodYear(Integer.parseInt(periodYear));
        fiscBookPeriodPO.setBookCode(bookCode);
        fiscBookPeriodPO.setStartDate(this.getFirstDayOfMonth(Integer.parseInt(periodYear),periodTerm));
        fiscBookPeriodPO.setEndDate(this.getLastDayOfMonth(Integer.parseInt(periodYear),periodTerm));
        fiscBookPeriodPO.setIsInUse(isCurrentTerm ? AppframeConst.YES_NO_Y : AppframeConst.YES_NO_N);
        return fiscBookPeriodPO;
    }

    private Date getFirstDayOfMonth(int year,int month){
        LocalDate localDate = LocalDate.of(year, month,1);
        LocalDate now = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return this.localDate2Date(now);
    }

    private Date getLastDayOfMonth(int year,int month){
        LocalDate localDate = LocalDate.of(year, month,1);
        LocalDate now = localDate.with(TemporalAdjusters.lastDayOfMonth());
        Date.from(now.atStartOfDay(ZoneId.systemDefault()).plusDays(1L).minusNanos(1L).toInstant());
        return this.localDate2Date(now);
    }

    private Date localDate2Date(LocalDate localDate){
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date from = Date.from(instant);
        return from;
    }

    @Transactional
    public Integer updatePeriodInUse(DataForm dataForm, MapObject object) {
        String periodId = object.getValue("periodId").strValue();
        String bookCode = object.getValue("bookCode").strValue();
        String isInUse = object.getValue("isInUse").strValue();
        FiscBookPO fiscBookPO = fiscalBookService.getFiscBook(bookCode);
        if (AppframeConst.YES_NO_Y.equals(isInUse)) {
            String allSql = "UPDATE FISC_BOOK_PERIOD SET IS_IN_USE = 'N' WHERE BOOK_CODE=:bookCode";
            beanCruder.execute(allSql,MapKit.mapOf("bookCode",bookCode));
            fiscBookPO.setCurrentYear(object.getValue("periodYear").intValue());
            fiscBookPO.setCurrentTerm(object.getValue("periodTerm").intValue());
        } else {
            fiscBookPO.setCurrentYear(null);
            fiscBookPO.setCurrentTerm(null);
        }
        beanCruder.save(fiscBookPO);
        String sql = "UPDATE FISC_BOOK_PERIOD SET IS_IN_USE = :isInUse WHERE PERIOD_ID=:periodId";
        return beanCruder.execute(sql,MapKit.mapOf("isInUse",isInUse,"periodId",periodId));
    }

    @Transactional
    public List<DictItemNode> addPeriodYear(DataForm dataForm, MapObject object) {
        String bookCode = object.getValue("bookCode").strValue();
        FiscBookPO fiscBookPO = fiscalBookService.getFiscBook(bookCode);
        Optional.ofNullable(fiscBookPO)
                .orElseThrow(() -> new BizException("帐套不存在"));
        Integer endYear = fiscBookPO.getEndYear();
        if (endYear == null || endYear == 0)
            endYear = fiscBookPO.getStartYear() + fiscalProperties.getBookDefaultInitYear() - 1;
        fiscBookPO.setEndYear(endYear + 1);
        beanCruder.save(fiscBookPO);
        return fiscalBookService.getRecentPeriod(bookCode);
    }
}
