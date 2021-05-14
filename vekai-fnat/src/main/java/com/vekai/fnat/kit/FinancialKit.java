package com.vekai.fnat.kit;

import java.text.MessageFormat;
import java.util.Date;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ValidateKit;

/**
 * 财务上的数学公式
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月21日
 */
public abstract class FinancialKit {
    public static final double XIRR_GUESS_MAX=999.999;        //最大收益率
    public static final double XIRR_GUESS_MIN=-0.99999999;    //最大收益率
    public static final double XIRR_GUESS_CRITICAL=0.00000001;//精确值
    public static final double XIRR_GUESS_COUNT=50;           //XIRR猜测最大次数
    
    /**
     * 一个基点为一个BP，万分之一
     */
    public static final double BASE_POINT = 0.0001;

    /**
     * 财务PMT公式<br/>
     * 目前暂时没有实现余值
     * @param rate 利率
     * @param terms 期数
     * @param capital 本金
     * @param residual 剩余值
     * @param type 0表示期末，1表示期初
     * @return
     */
    public static double pmt(double rate,int terms,double capital,double residual,int type){
    	double temp = Math.pow(1 + rate, terms);
    	double value = (capital * rate * temp - residual * rate)/(temp - 1)/ Math.pow(1+rate,type) ;
        return -value;
    }
    /**
     * 校验日期流和时间流是否匹配，是否合法
     * @param dates
     * @param values
     */
    private static void validateDateValuesFlow(Date[] dates,Double values[]){
        ValidateKit.notEmpty(dates,"日期序列不能为空");
        ValidateKit.notEmpty(values,"值序列不能为空");
        ValidateKit.validState(dates.length==values.length, "日期序列的值序列长度不相同");
        ValidateKit.validState(dates.length>1, "现金流少于一条");
        ValidateKit.notNull(values[0],"第0期值为空");
    }
    
    /**
     * EXCEL的NPV计算。通过贴现利率，计算现值<br>
     * <dt>数据要求如下：</dt>
     * <dd>1.各期间长度必需相等</dd>
     * <dd>2.各期的收入和支出都必需发生在期末</dd>
     * @param rate 期间贴现利率
     * @param values 期间支出及收入
     * @return
     */
    public static double npv(double rate,double... values){
        double sum = 0d;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] / Math.pow(rate + 1d, i);
        }
        return sum;
        
    }
    
    /**
     * 折现值(Presend Discounted Value)</br>
     * 将未来的一笔钱，按照某种利率折算到现在的值</br>
     * 参考算法，现值=未来值/(1+年利率)^年数
     * @param futureValue 未来值（未来收入金额）
     * @param rate 利率(必需折算成年利率)
     * @param distanceDays 期间天数
     * @param yearDays 年计天数
     * @return
     */
    public static double pdv(double futureValue,double rate,int distanceDays,int yearDays){
        return futureValue / ((Math.pow((1 + rate), distanceDays / ((double)yearDays))));
    }
    /**
     * 现金流按照指定的利率，指定的日期折现值
     * @param dates 日期序列 
     * @param values 金额序列
     * @param rate 利率值（年化）
     * @param startDate 折现日期
     * @param yearDays 年计天数
     * @return
     */
    public static double xpdv(Date[] dates, Double values[], double rate, Date startDate, int yearDays){
        validateDateValuesFlow(dates,values);
        double sumValue = 0d;
        for(int i=0;i<dates.length;i++){
            int days = DateKit.getRangeDays(startDate, dates[i]);
            sumValue += pdv(values[i],rate,days,yearDays);
        }
        
        return sumValue;
        
    }
    
    /**
     * 计算IRR，传入一个等期现金流，不等期无意义
     * @param values
     * @return
     */
	public static double irr(Double values[]) {
		double sumAll = 0;
		for (Double value : values) {
			sumAll += value;
		}
		boolean isProfit = sumAll > 0; // 标识是否盈利
		double irrValue = 0d;
		double guessMax = 0d;
		double guessMin = 0d;
		double guessCount = XIRR_GUESS_COUNT;
		if (isProfit) {
			guessMax = XIRR_GUESS_MAX;
		} else {
			guessMin = XIRR_GUESS_MIN;
		}
		while (guessCount-- > 0) {
			double sumValue = 0d;
			irrValue = (guessMax + guessMin) / 2d;
			for (int i = 0; i < values.length; i++) {
				sumValue += pdv(values[i], irrValue, i, 1);
			}
			if (sumValue > 0) {
				guessMin = irrValue;
			} else {
				guessMax = irrValue;
			}
			if (Math.abs(sumValue) <= XIRR_GUESS_CRITICAL) {
				break;
			}
		}
		return irrValue;
	}
    
    /**
     * 计算XIRR，传入一个现金流，日期序列和金额序列必需相一对一匹配
     * @param dates 日期序列 
     * @param values 金额序列
     * @param yearDays 年计天数
     * @return 收益率（年化）
     */
    public static double xirr(Date[] dates,Double values[],int yearDays){
        validateDateValuesFlow(dates,values);
        
        Date startDate = dates[0];
        double sumAll = values[0];
        for(int i=1;i<dates.length;i++){
            ValidateKit.validState(DateKit.isAfter(startDate, dates[i]), MessageFormat.format("日期[{0}]不晚于开始日期[{1}]",dates[i],startDate));
            ValidateKit.notNull(values[i],"第{0}期值为空");
            sumAll += values[i];
        }
        boolean isProfit = sumAll>0;    //标识是否盈利
        double xirrValue = 0d;
        double guessMax = 0d;
        double guessMin = 0d;
        double guessCount = XIRR_GUESS_COUNT;
        if(isProfit){
            guessMax = XIRR_GUESS_MAX;
        }else{
            guessMin = XIRR_GUESS_MIN;
        }
        while(guessCount-- > 0){
            xirrValue = (guessMin + guessMax) / 2d;
            double xpdv = xpdv(dates,values,xirrValue,startDate,yearDays);
            if (xpdv > 0) {
                guessMin = xirrValue;
            } else {
                guessMax = xirrValue;
            }
//            System.out.println(MessageFormat.format("次数：{0}，xpdv:{1,number,#0.0###############},xirr:{2,number,#0.0###############}", XIRR_GUESS_COUNT-guessCount,xpdv,xirrValue));
            if (Math.abs(xpdv) <= XIRR_GUESS_CRITICAL) {
//                System.out.println("逼近次数:"+guessCount+","+xpdv);
                break;
            }
        }
        return xirrValue;
    }
    
    /**
     * 多少个BP
     * @param amount
     * @return
     */
    public static double bp(int amount){
        return BASE_POINT * amount;
    }

	/**
	 * 按照一个月30天来算,相同为0,开始日期不能比结束日期大
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param method true欧洲方法 false美国方法
	 *   美国方法：如果起始日期是一个月的第31天，则将这一天视为同一个月份的第30天；如果终止日期是一个月的第31天
	 *            且起始日期早于一个月的第30天，则将这个终止日期视为下一个月的第1天，否则终止日期等于同一个月的第30天。
	 *   欧洲方法：无论起始日期还是终止日期是一个月的第31天，都视为同一个月份的第30天。
	 * @return
	 */
    public static int days360(Date startDate,Date endDate,boolean method){
		ValidateKit.isTrue(DateKit.isAfter(startDate, endDate,true),"起始日期大于结束日期");
    	if(DateKit.getDayOfMonth(endDate)==31){
    		if(!method&&DateKit.getDayOfMonth(startDate)<30)
    			endDate = DateKit.plusDays(endDate, 1);
    		else 
    			endDate = DateKit.plusDays(endDate, -1);
    	}
    	if(DateKit.getDayOfMonth(startDate)==31)
    		startDate = DateKit.plusDays(startDate, -1);
    	
    	int days = 0;
    	int mons = DateKit.getRangeMonths(startDate, endDate);
    	if(mons==0){
    		if(DateKit.getMonth(startDate)==DateKit.getMonth(endDate)){//同一个月
    			days=DateKit.getRangeDays(startDate, endDate);
    		}else{
    			days = DateKit.getDayOfMonth(endDate) + 30 - DateKit.getDayOfMonth(startDate);
    		}
    	}else{
    		days = 30 * mons + days360(DateKit.plusMonths(startDate, mons),endDate,true);
    	}
    	return days;
    }
    
//    /**
//     *
//     * @param totalAmt 含税金额
//     * @param taxRate 税率
//     * @return 不含税的金额
//     */
//    public static double calNoTaxAmt(double totalAmt, double taxRate){
//    	return NumberKit.round(totalAmt/(1+taxRate), 2);
//    }
//
//    /**
//     *
//     * @param totalAmt 含税金额
//     * @param taxRate 税率
//     * @return 税额
//     */
//    public static double calTaxAmt(double totalAmt, double taxRate){
//    	return NumberKit.round(totalAmt - calNoTaxAmt(totalAmt, taxRate), 2);
//    }

}
