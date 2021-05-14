package com.vekai.fnat.kit;

import cn.fisok.raw.kit.DateKit;
import org.junit.Test;

import java.util.Date;

public class FinancialKitTest {

    @Test
    public void xirr(){
//    	String dates[] = {"2017-06-01","2017-07-01","2017-08-01"};
//    	Double values[] = {-100d,51d,52d};
        String strDates[] = {
                "2017-08-23","2017-10-23",  "2017-10-24",
                "2017-09-23", "2017-10-24", "2018-07-23", "2017-09-23",
                "2017-10-23", "2017-11-23", "2017-12-23", "2018-01-23",
                "2018-02-23", "2018-03-23", "2018-04-23", "2018-05-23",
                "2018-06-23", "2018-07-23", "2018-07-23"};
        Double values[] = {
                72792.5,-1000000.0, 150000.0,
                100.0, 200000.0, -200000.0, 72792.5,
                72792.5,72792.5, 72792.5, 72792.5,
                72792.5, 72792.5, 72792.5, 72792.5,
                72792.5, 72792.5, 0.0};

        Date dates[] = new Date[strDates.length];
        for(int i=0;i<strDates.length;i++){
            dates[i] = DateKit.parse(strDates[i]);
        }
        double a = FinancialKit.xirr(dates, values, 365);
        System.out.println(a);
    }

    @Test
    public void test01(){
        Double[] values = {-10000d,175000d,175000d,175000d};
        System.out.println(FinancialKit.irr(values));
    }
    @Test
    public void test02(){
        Object[][] cashFlows = new Object[][]{
                {"2016-5-2",-4998000.00d},
                {"2016-10-9",222844.27},
                {"2016-11-9",222844.27d},
                {"2016-12-9",222844.27d},
                {"2017-1-9",222844.27d},
                {"2017-2-9",222844.27d},
                {"2017-3-9",222844.27d},
                {"2017-4-9",222844.27d},
                {"2017-5-9",222844.27d},
                {"2017-6-9",222844.27d},
                {"2017-7-9",222844.27d},
                {"2017-8-9",222844.27d},
                {"2017-9-9",222844.27d},
                {"2017-10-9",222844.27d},
                {"2017-11-9",222844.27d},
                {"2017-12-9",222844.27d},
                {"2018-1-9",222844.27d},
                {"2018-2-9",222844.27d},
                {"2018-3-9",222844.27d},
                {"2018-4-9",222844.27d},
                {"2018-5-9",222844.27d},
                {"2018-6-9",222844.27d},
                {"2018-7-9",222844.27d},
                {"2018-8-9",222844.27d},
                {"2018-9-9",222944.27d}
        };

        Date[] dates = new Date[cashFlows.length];
        Double[] values = new Double[cashFlows.length];
        for(int i=0;i<cashFlows.length;i++){
            dates[i]=DateKit.parse((String)cashFlows[i][0]);
            values[i]=(Double)cashFlows[i][1];
        }
        System.out.println(FinancialKit.xirr(dates, values, 365));
    }
    @Test
    public void test03(){
        Object[][] cashFlows = new Object[][]{
                {"2014-01-01",-10000d},
                {"2015-01-01",1000d},
                {"2016-01-01",11000d},
        };

        Date[] dates = new Date[cashFlows.length];
        Double[] values = new Double[cashFlows.length];
        for(int i=0;i<cashFlows.length;i++){
            dates[i]=DateKit.parse((String)cashFlows[i][0]);
            values[i]=(Double)cashFlows[i][1];
        }
        System.out.println(FinancialKit.xirr(dates, values, 365));
    }
    @Test
    public void test04(){
        Object[][] cashFlows = new Object[][]{
                {"2014-01-01",-10000d},
                {"2014-06-30",500d},
                {"2015-01-01",10500d},
        };

        Date[] dates = new Date[cashFlows.length];
        Double[] values = new Double[cashFlows.length];
        for(int i=0;i<cashFlows.length;i++){
            dates[i]=DateKit.parse((String)cashFlows[i][0]);
            values[i]=(Double)cashFlows[i][1];
        }
        System.out.println(FinancialKit.xirr(dates, values, 365));
    }
}
