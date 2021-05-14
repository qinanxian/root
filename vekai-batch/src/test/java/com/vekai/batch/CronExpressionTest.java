package com.vekai.batch;

import cn.fisok.raw.kit.DateKit;
import org.junit.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

public class CronExpressionTest {

    @Test
    public void test1() throws ParseException {
        CronExpression cronExpression = new CronExpression("0/10 * * * * ? ");

//        System.out.println(cronExpression.getExpressionSummary());
        Date nextTime  = DateKit.now();
        for(int i=0;i<5;i++){
            nextTime = cronExpression.getTimeAfter(nextTime);
            System.out.println(DateKit.format(nextTime));
        }
    }
}
