package com.vekai.lact.core.loader.impl;

import cn.fisok.raw.lang.MapObject;
import com.vekai.fnat.model.BaseInterestRate;
import com.vekai.fnat.service.BaseInterestRateService;
import com.vekai.lact.core.loader.DataObjectLoader;
import com.vekai.lact.entity.LactLoanSegmentEntity;
import com.vekai.lact.exception.LactException;
import com.vekai.lact.model.LactLoan;
import com.vekai.lact.model.LactLoanSegment;
import com.vekai.lact.type.RateAppearMode;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 利率档次加载器
 */
public class InterestRateGradeLoader implements DataObjectLoader<LactLoan> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private int pointScale = 4;//利率保留两位小数，由于需要除以100，因此这里写4位

    @Override
    public void load(LactLoan loan, MapObject parameter) {
        logger.trace("--加载利率档次--");
//        List<LactLoanSegmentEntity> segments = loan.getSegments();
        List<LactLoanSegment> segments = loan.getSegmentList();
        for(LactLoanSegmentEntity segment : segments){
            //不是明示利率，则不需要加载利率档次
            if(RateAppearMode.valueOf(segment.getRateAppearMode()) != RateAppearMode.Indication)continue;

            String baseRateType = segment.getBaseRateType();    //基准利率类型
            Integer termMonth = segment.getTermMonth();         //借款月数
            Integer termDay = segment.getTermDay();             //借款零头天数
            Date startDate = segment.getStartDate();            //起始日期

            //预先计算到期日
            Date endDate = DateKit.plusMonths(startDate,termMonth);
            int endDateMonthDays = DateKit.getMonthDays(endDate);

            if(termMonth == null) termMonth = 0;
            if(termDay == null) termDay = 0;

            ValidateKit.notBlank(baseRateType,"[基准利率类型]不能为空");
            ValidateKit.isTrue(termMonth>0,"[期限（月数）]必需大于0");
            ValidateKit.notNull(startDate,"[开始日期]不能为空");
            //检查一下零头天数不得超过到期日当月的总天数
            ValidateKit.isTrue(termDay<endDateMonthDays,"[零头天数]数据错误，零头天数{0}不得多于结束日期当月的总天数。结束日期：{1},零头天数{2}",termDay,endDate,endDateMonthDays);

            //查找利率档次
            BaseInterestRate interestRate = matchBaseRate(startDate,baseRateType,termMonth,termDay);
            ValidateKit.notNull(interestRate,"没有找到合适的利率档次");
            //设置并处理基准利率
            Double annualRate = interestRate.getAnnualRate();

            segment.setBaseRateGrade(interestRate.getTermCode());
            segment.setBaseRate(NumberKit.round(annualRate/100d, pointScale));

        }
    }

    /**
     * 根据期限，匹配人行基准利率期限档次
     * @param startDate
     * @param baseRateType
     * @param termMonth
     * @param termDay
     * @return
     */
    protected BaseInterestRate matchBaseRate(Date startDate, String baseRateType,Integer termMonth, Integer termDay){
        //1.调用基准利率档次服务
        BaseInterestRateService baseInterestRateService = ApplicationContextHolder.getBean(BaseInterestRateService.class);
        ValidateKit.notNull(baseInterestRateService,"基准利率数据加载服务异常，该服务不存在");
        //2.加载指定利率类型的基准利率
        List<BaseInterestRate> dataList = baseInterestRateService.getBaseInterestRateList(baseRateType);
        ValidateKit.notEmpty(dataList,"基准利率数据异常，到不到类型为{0}的利率数据",baseRateType);
        //3.查找当前业务日期对应的利率生效日
        Date effectDate = mathEffectDate(startDate,dataList);
        ValidateKit.notNull(effectDate,"没有匹配到合适的利率日期，业务日期{0}",startDate);
        //4.根据利率生效日，匹配对应的利率档次数据
        BaseInterestRate interestRate = mathBaseInterestRateGrade(effectDate,dataList,termMonth,termDay);

        return interestRate;
    }

    /**
     * 匹配当前业务日期相适应的利率日期
     * @param matchDate
     * @param dataList
     * @return
     */
    protected Date mathEffectDate(Date matchDate,List<BaseInterestRate> dataList){
        //1.找出所有日期（记录中日期是重复的）,使用Set去重，日期默认从小到大排列
        Set<Date> dateSet = new TreeSet<Date>();
        for(BaseInterestRate row : dataList){
            dateSet.add(row.getEffectDate());
        }
        List<Date> dateList = new ArrayList<Date>();
        dateList.addAll(dateSet);

        //2.从最新日期往前匹配最新的日期
        Date effectDate = null;
        for(int i=dateList.size()-1;i>=0;i--){
            Date curDate = dateList.get(i);
            if(DateKit.isAfter(curDate, matchDate)){
                effectDate = curDate;
                break;
            }
        }

        return effectDate;

    }

    protected BaseInterestRate mathBaseInterestRateGrade(Date effectDate,List<BaseInterestRate> dataList,Integer termMonth, Integer termDay){
        //1.找到对应日期的利率档次记录
        List<BaseInterestRate> effectList = new ArrayList<>();
        for(BaseInterestRate item : dataList){
            if(DateKit.isSameDay(item.getEffectDate(),effectDate)){
                effectList.add(item);
            }
        }
        //2.进行排序，先按日期，再按期次
        Collections.sort(effectList, new Comparator<BaseInterestRate>() {
            public int compare(BaseInterestRate o1, BaseInterestRate o2) {
                    String sortCode1 = StringKit.nvl(o1.getSortCode(),"");
                    String sortCode2 = StringKit.nvl(o2.getSortCode(),"");
                    return sortCode1.compareTo(sortCode2);
            }
        });
        //3.进行匹配
        //如果零头天数大于0，则月数加一个月上去，因为超出当月了
        Integer mathMonth = termMonth;
        if(termDay > 0){
            mathMonth += 1;
        }
        ValidateKit.isTrue(mathMonth <= 360,"期限数据不合法，债权类项目的期限，不得超过360个月（30年）");

        BaseInterestRate interestRate = null;
        Integer monthStart = 0;
        Integer monthEnd = 0; //60年,一般30年就够了
        for(BaseInterestRate item : effectList){
            try{
                monthEnd = Integer.parseInt(item.getTermCode());
            }catch(Exception e){
                throw new LactException("利率数据异常，利率数据[期限代码]值不是合法的整数，{0}",item.getTermCode());
            }
            if(mathMonth > monthStart && mathMonth <= monthEnd){
                interestRate = item;
            }
            //本轮的结束日期是下一轮的开始日期
            monthStart = monthEnd;
        }


        return interestRate;
    }
}
