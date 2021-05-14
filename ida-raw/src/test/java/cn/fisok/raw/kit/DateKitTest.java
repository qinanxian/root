package cn.fisok.raw.kit;

import org.junit.Test;

import java.util.Date;

public class DateKitTest {
    @Test
    public void test1(){
        System.out.println(DateKit.format(DateKit.parse("2018/03/03")));
        System.out.println(DateKit.format(DateKit.parse("2018//03--01")));
        System.out.println(DateKit.format(DateKit.parse("699379200000")));
        System.out.println("----------------------");

        Date startDate = DateKit.parse("2016-01-01");
        int year = DateKit.getYear(startDate);
        boolean leapYear = (year%4==0&&year%100!=0||year%400==0);
        int days = leapYear?366:365;
        for(int i=0;i<days;i++){
            Date incDate = DateKit.plusDays(startDate,i);
            int dayOfWeek = DateKit.getDayOfWeek(incDate);
            if(dayOfWeek == 6 || dayOfWeek == 7){
                System.out.println(DateKit.format(incDate,"yyyy-MM-dd")+"->"+dayOfWeek);
            }

        }





    }


    @Test
    public void test2(){
        System.out.println("-------------分割线--------------");
        System.out.println(DateKit.format(DateKit.now()));
        System.out.println(DateKit.getMonth(new Date()));
        System.out.println(DateKit.format(DateKit.getLastDayOfMonth(2018,3)));

    }
}
