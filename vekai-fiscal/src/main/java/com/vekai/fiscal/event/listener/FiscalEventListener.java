package com.vekai.fiscal.event.listener;

import com.vekai.fiscal.consts.ChargeupDirection;
import com.vekai.fiscal.exception.FiscalException;
import com.vekai.fiscal.voucher.service.FiscalVoucherService;
import com.vekai.fiscal.book.model.FiscalBook;
import com.vekai.fiscal.book.model.FiscalBookAssit;
import com.vekai.fiscal.book.model.FiscalBookEntry;
import com.vekai.fiscal.book.model.FiscalBookPeriod;
import com.vekai.fiscal.book.model.FiscalVoucher;
import com.vekai.fiscal.book.model.FiscalVoucherAssit;
import com.vekai.fiscal.book.model.FiscalVoucherEntry;
import com.vekai.fiscal.book.service.FiscalBookService;
import com.vekai.fiscal.book.service.FiscalEventService;
import com.vekai.fiscal.event.model.FiscalEvent;
import com.vekai.fiscal.event.model.FiscalEventEntry;
import com.vekai.fiscal.event.model.FiscalEventParam;
import com.vekai.fiscal.event.wrapper.FiscalEventWrapperEvent;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订阅财务事件，记会计分录
 *  * Created by 杨松<yangsong158@qq.com> on 2018-06-14
 */
@Component
public class FiscalEventListener  {
    @Autowired
    FiscalBookService bookService;
    @Autowired
    FiscalVoucherService voucherService;
    @Autowired
    FiscalEventService fiscalEventService;

    @EventListener
    public void listenerFiscalEvent(FiscalEventWrapperEvent eventWrapper){
        FiscalEvent event = eventWrapper.getEvent();
        //记录会计分录
        System.out.println("财务事件2："+event.getEventName());
        //1.找出所有匹配到的账套，科目，凭证GROUP,计算出凭证数量
        List<VoucherGroupItem> voucherGroupItems = splitVoucherGroupItems(event);
        if(voucherGroupItems.size()==0)return;

        voucherGroupItems.forEach(groupItem->{
            //2.查找当前会计期间
            FiscalBook fiscalBook = bookService.getFiscalBook(groupItem.bookCode);
            if(fiscalBook == null)return;//没有找到账套，就不要记了
            FiscalBookPeriod period = fiscalBook.getActivePeriod();
            if(period == null){
                throw new FiscalException("账套{0}没有找到打开的会计期间",groupItem.bookCode);
            }
            //3.填充记账凭证
            FiscalVoucher voucher = createFiscalVoucher(event,fiscalBook,groupItem);
            //4.填充凭证科目
            fillVoucherEntry(voucher,groupItem,fiscalBook,event);

            voucherService.saveFiscalVoucher(voucher);
        });
    }

    /**
     * 填充凭证科目明细
     * @param voucher
     * @param groupItem
     * @param fiscalBook
     */
    protected void fillVoucherEntry(FiscalVoucher voucher,VoucherGroupItem groupItem,FiscalBook fiscalBook,FiscalEvent event){
        List<FiscalVoucherEntry> voucherEntryList = new ArrayList<>();    //凭证分录
        //参数对象池
        final Map<String,String> paramsMap = MapKit.newHashMap();
        final Map<String,FiscalEventParam> paramsObjectMap = MapKit.newHashMap();
        List<FiscalEventParam> paramList = event.getParams();
        paramList.forEach(param->{
            paramsMap.put(param.getItemCode(),param.getStrValue());
            paramsObjectMap.put(param.getItemCode(),param);
        });

        groupItem.entries.forEach(eventEntry->{
            FiscalBookEntry bookEntry = fiscalBook.getBookEntry(eventEntry.getEntryCode());//设置在账套上的账套科目
            String tpl = bookEntry.getAssistNameTpl();
            String assitName = null;
            //辅助科目名
            if (!StringKit.isBlank(tpl))
                assitName = StringKit.fillTpl(tpl,paramsMap);

            FiscalVoucherEntry voucherEntry = new FiscalVoucherEntry();         //凭证科目
            voucherEntry.setBookEntryId(bookEntry.getBookEntryId());
            voucherEntry.setEventEntryId(eventEntry.getEventEntryId());
            voucherEntry.setEntryCode(bookEntry.getEntryCode());
            voucherEntry.setHierarchy(bookEntry.getHierarchy());
            voucherEntry.setMnemonicCode(bookEntry.getMnemonicCode());
            voucherEntry.setEntryName(bookEntry.getEntryName());
            voucherEntry.setEntryFullName(bookEntry.getEntryFullName());
            voucherEntry.setEntryNote(eventEntry.getEventSummary());
            voucherEntry.setIsCash(bookEntry.getIsCash());
            voucherEntry.setIsBank(bookEntry.getIsBank());
            voucherEntry.setAssistName(assitName);
            voucherEntry.setCurrency(voucher.getCurrency());
            voucherEntry.setDirection(bookEntry.getDirection());
            voucherEntry.setOriginalCurrency(eventEntry.getOriginalCurrency());
            voucherEntry.setOccurAmt(eventEntry.getOccurAmt()); //取金额
            voucherEntry.setOriginalAmt(eventEntry.getOriginalAmt());
            voucherEntryList.add(voucherEntry);
            //生成科目辅助项
            List<FiscalVoucherAssit> assitInstList = this.generateVoucherAssit(bookEntry,paramsObjectMap);
            voucherEntry.setAssitList(assitInstList);
        });
        voucher.setEntryList(voucherEntryList);
    }

    /**
     * 生成科目辅助项
     * @param bookEntry
     * @param paramsObjectMap
     */
    private List<FiscalVoucherAssit> generateVoucherAssit(FiscalBookEntry bookEntry, Map<String, FiscalEventParam> paramsObjectMap) {
        List<FiscalBookAssit> bookAssits = bookEntry.getAssitList();    //取辅助科目
        List<FiscalVoucherAssit> assitInstList = ListKit.newArrayList();
        bookAssits.forEach(bookAssit->{
            FiscalVoucherAssit assitInst = new FiscalVoucherAssit();
            String itemCode = bookAssit.getItemCode();
            FiscalEventParam eventParam = paramsObjectMap.get(itemCode);
            if(eventParam == null)return;

            assitInst.setBookEntryId(bookEntry.getBookEntryId());
            assitInst.setItemCode(itemCode);
            assitInst.setItemName(bookAssit.getItemName());
            assitInst.setDataText(eventParam.getDataText());
            assitInst.setDataType(eventParam.getDataType());
            assitInst.setDateValue(eventParam.getDateValue());
            assitInst.setStrValue(eventParam.getStrValue());
            assitInst.setFloatValue(eventParam.getFloatValue());
            assitInst.setIntValue(eventParam.getIntValue());
            assitInstList.add(assitInst);
        });
        return assitInstList;
    }


    protected FiscalVoucher createFiscalVoucher(FiscalEvent event,FiscalBook fiscalBook,VoucherGroupItem groupItem){
        FiscalBookPeriod period = fiscalBook.getActivePeriod();

        FiscalVoucher voucher = new FiscalVoucher();
        voucher.setBookCode(fiscalBook.getBookCode());
        voucher.setPeriodId(period.getPeriodId());
        voucher.setEventId(event.getEventId());
        voucher.setPeriodYear(period.getPeriodYear());
        voucher.setPeriodTerm(period.getPeriodTerm());
        voucher.setVoucherGenerateDate(DateKit.now());
        voucher.setOccurDate(event.getOccurTime());
        voucher.setEntryNote(event.getEventName());
        voucher.setVoucherWord(voucherService.getVoucherWord(fiscalBook));  //凭证字
        voucher.setVoucherNo(voucherService.getVoucherWordSeq(fiscalBook)); //凭证号
        voucher.setExchangeRateType(event.getExchangeRateType());
        voucher.setExchangeRate(event.getExchangeRate());
        voucher.setCurrency(event.getCurrency());

        //汇总借方,贷方金额
        double debitAmt = 0d;   //借方金额
        double creditAmt = 0d;  //贷方金额
        for (FiscalEventEntry entry : groupItem.entries) {
            String direc = entry.getDirection();
            if(StringKit.isBlank(direc))continue;
            ChargeupDirection direction = ChargeupDirection.valueOf(direc);
            if(direction == ChargeupDirection.Debit){
                debitAmt += entry.getOccurAmt();
            }else{
                creditAmt += entry.getOccurAmt();
            }
        }

        if(debitAmt - creditAmt >= 0.0001){
            throw new FiscalException("记账异常，借贷方向金额不等，借方金额:{0},贷方金额:{1}",debitAmt,creditAmt);
        }

        voucher.setDebitAmt(debitAmt);
        voucher.setCreditAmt(creditAmt);

        return voucher;

    }

    /**
     * 拆分出账套，凭证
     * @param event
     * @return
     */
    protected List<VoucherGroupItem> splitVoucherGroupItems(FiscalEvent event){
        List<FiscalEventEntry> entries = event.getEntries();
        List<VoucherGroupItem> items = ListKit.newArrayList();
        Map<String,VoucherGroupItem> mapPool = MapKit.newHashMap();

        entries.forEach(eventEntry->{
            String key = String.format("%s/%s",eventEntry.getBookCode(),StringKit.nvl(eventEntry.getVoucherGroup(),""));
            VoucherGroupItem groupItem = mapPool.get(key);
            if(groupItem == null){
                groupItem = new VoucherGroupItem();
                groupItem.setBookCode(eventEntry.getBookCode());
                groupItem.setVoucherGroup(eventEntry.getVoucherGroup());
                mapPool.put(key,groupItem);
            }
            groupItem.entries.add(eventEntry);
        });

        items.addAll(mapPool.values());

        return items;
    }

    public static class VoucherGroupItem{
        private String bookCode;
        private String voucherGroup;
        List<FiscalEventEntry> entries = ListKit.newArrayList();

        public String getBookCode() {
            return bookCode;
        }

        public void setBookCode(String bookCode) {
            this.bookCode = bookCode;
        }

        public String getVoucherGroup() {
            return voucherGroup;
        }

        public void setVoucherGroup(String voucherGroup) {
            this.voucherGroup = voucherGroup;
        }
    }
}
