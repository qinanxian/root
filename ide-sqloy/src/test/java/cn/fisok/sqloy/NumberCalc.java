package cn.fisok.sqloy;

import cn.fisok.raw.kit.NumberKit;
import cn.fisok.raw.kit.StringKit;
import org.junit.Test;

public class NumberCalc {

    @Test
    public void test01(){
        Integer totalCount = 101;
        Integer pageSize = 25;
        System.out.println(totalCount/pageSize);
//        System.out.println(NumberKit.divide(totalRowCount,pageSize).intValue());
        int pageCount = NumberKit.ceil(NumberKit.divide(totalCount,pageSize),0).intValue();
        System.out.println(pageCount);

    }

    @Test
    public void test02(){
        System.out.println(StringKit.rightPad("A123",6,"0"));
        System.out.println(StringKit.rightPad("A1234",2,"0"));
        System.out.println(StringKit.rightPad("A1234",4,"0"));
    }
}
