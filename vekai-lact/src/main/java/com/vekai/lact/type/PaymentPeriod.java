package com.vekai.lact.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 还款周期
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月8日
 */
public enum PaymentPeriod {
    M,TM,Q,FM,HY,Y;
    private static Map<PaymentPeriod,Integer> multiMap = new HashMap<PaymentPeriod,Integer>();
    static {
        multiMap.put(PaymentPeriod.M, 1);
        multiMap.put(PaymentPeriod.TM, 2);
        multiMap.put(PaymentPeriod.Q, 3);
        multiMap.put(PaymentPeriod.FM, 4);
        multiMap.put(PaymentPeriod.HY, 6);
        multiMap.put(PaymentPeriod.Y, 12);
    }
    public static int getMulti(String period){
        return PaymentPeriod.valueOf(period).getMulti();
    }
    public int getMulti(){
        return multiMap.get(this);
    }
}
