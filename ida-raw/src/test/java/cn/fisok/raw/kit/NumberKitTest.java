package cn.fisok.raw.kit;

import org.junit.Test;

import java.util.Date;

public class NumberKitTest {

//    @Test
    public void test36Radix(){
        System.out.println(NumberKit.convert36Radix(2017));
        System.out.println(NumberKit.convert36Radix(17));
        System.out.println(NumberKit.convert36Radix(37));
        System.out.println(NumberKit.convert36Radix(8));
        System.out.println(NumberKit.convert36Radix(19));
        System.out.println(NumberKit.convert36Radix(1708));
        System.out.println(NumberKit.convert36Radix(201708));
        System.out.println(NumberKit.convert36Radix(20170819));
    }

//    @Test
    public void testNanoTime(){
        Long nano = System.nanoTime();
        Long millis = nano / 1000000;
        System.out.println(nano);
        System.out.println(System.currentTimeMillis());
        System.out.println(millis);
//        Date date = DateKit.parse(System.currentTimeMillis());
        Date date = DateKit.parse(millis);
//        Date date = DateKit.now();
        System.out.println(DateKit.getYear(date));

//        Long minutes = seconds / 60;
//        Long hours = minutes / 60;
//        Long days = hours / 24;
//        Long months = days/12;
//        Long years = months/12;
    }

    @Test
    public void testRoundX(){
        System.out.println(NumberKit.round(123.45658,3));
        System.out.println(NumberKit.round(123.45638,3));
        System.out.println(NumberKit.ceil(123.45638,3));
        System.out.println(NumberKit.floor(123.45638,3));
        System.out.println(NumberKit.ceil(123.45658,3));
        System.out.println(NumberKit.floor(123.45658,3));
        System.out.println(NumberKit.ceil(123.45658,0));
        System.out.println(NumberKit.floor(123.45658,0));
        System.out.println(NumberKit.ceil(10/3,0));
    }

    @Test
    public void testChinese2double(){
//        System.out.println(NumberKit.chineseMoney2double("壹万伍仟肆佰壹拾元贰角捌分肆厘"));
//        System.out.println(NumberKit.chineseMoney2double("壹万元整"));
//        System.out.println(NumberKit.chineseMoney2double("壹拾万元整"));
//        System.out.println(NumberKit.chineseMoney2double("玖佰玖拾玖万伍仟肆佰元整"));
//        System.out.println(NumberKit.chineseMoney2double("肆佰元整"));
//        System.out.println(NumberKit.chineseMoney2double("伍仟元整"));

        String s1 = "壹万伍仟肆佰壹拾圆叁角伍分肆厘";
        String s2 = "捌万陆仟肆佰壹拾圆整";
        String s3 =  "壹万伍仟肆佰壹拾元贰角捌分肆厘";
        String s4 =  "拾壹亿壹仟万伍仟肆佰壹拾元贰角捌分肆厘";
        System.out.printf("%s = %6.3f\n",s1,NumberKit.rmbUppercase2Double(s1));
        System.out.printf("%s = %6.3f\n",s2,NumberKit.rmbUppercase2Double(s2));
        System.out.printf("%s = %6.3f\n",s3,NumberKit.rmbUppercase2Double(s3));
        System.out.printf("%s = %6.3f\n",s4,NumberKit.rmbUppercase2Double(s4));
    }

    @Test
    public void testDouble2ChineseMoney(){
//        System.out.println(NumberKit.chineseMoney2double("壹万伍仟肆佰壹拾元贰角捌分肆厘"));
//        System.out.println(NumberKit.chineseMoney2double("壹万元整"));
//        System.out.println(NumberKit.chineseMoney2double("壹拾万元整"));
//        System.out.println(NumberKit.chineseMoney2double("玖佰玖拾玖万伍仟肆佰元整"));
//        System.out.println(NumberKit.chineseMoney2double("肆佰元整"));
//        System.out.println(NumberKit.chineseMoney2double("伍仟元整"));

        // 整数
        System.out.println("----------整数-------------");
        System.out.println(NumberKit.double2RMBUppercase(0)); // 零元整
        System.out.println(NumberKit.double2RMBUppercase(123)); // 壹佰贰拾叁元整
        System.out.println(NumberKit.double2RMBUppercase(1000000)); // 壹佰万元整
        System.out.println(NumberKit.double2RMBUppercase(100000001)); // 壹亿零壹元整
        System.out.println(NumberKit.double2RMBUppercase(1000000000)); // 壹拾亿元整
        System.out.println(NumberKit.double2RMBUppercase(1234567890)); // 壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元整
        System.out.println(NumberKit.double2RMBUppercase(1001100101)); // 壹拾亿零壹佰壹拾万零壹佰零壹元整
        System.out.println(NumberKit.double2RMBUppercase(110101010)); // 壹亿壹仟零壹拾万壹仟零壹拾元整
        System.out.println();
        // 小数
        System.out.println("----------小数-------------");
        System.out.println(NumberKit.double2RMBUppercase(0.12)); // 壹角贰分
        System.out.println(NumberKit.double2RMBUppercase(123.34)); // 壹佰贰拾叁元叁角肆分
        System.out.println(NumberKit.double2RMBUppercase(97001.34)); // 壹佰贰拾叁元叁角肆分
        System.out.println(NumberKit.double2RMBUppercase(1000000.56)); // 壹佰万元伍角陆分
        System.out.println(NumberKit.double2RMBUppercase(100000001.78)); // 壹亿零壹元柒角捌分
        System.out.println(NumberKit.double2RMBUppercase(1000000000.90)); // 壹拾亿元玖角
        System.out.println(NumberKit.double2RMBUppercase(1234567890.03)); // 壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分
        System.out.println(NumberKit.double2RMBUppercase(1001100101.00)); // 壹拾亿零壹佰壹拾万零壹佰零壹元整
        System.out.println(NumberKit.double2RMBUppercase(110101010.10)); // 壹亿壹仟零壹拾万壹仟零壹拾元壹角

        // 负数
        System.out.println("----------负数-------------");
        System.out.println(NumberKit.double2RMBUppercase(-0.12)); // 负壹角贰分
        System.out.println(NumberKit.double2RMBUppercase(-123.34)); // 负壹佰贰拾叁元叁角肆分
        System.out.println(NumberKit.double2RMBUppercase(-1000000.56)); // 负壹佰万元伍角陆分
        System.out.println(NumberKit.double2RMBUppercase(-100000001.78)); // 负壹亿零壹元柒角捌分
        System.out.println(NumberKit.double2RMBUppercase(-1000000000.90)); // 负壹拾亿元玖角
        System.out.println(NumberKit.double2RMBUppercase(-1234567890.03)); // 负壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分
        System.out.println(NumberKit.double2RMBUppercase(-1001100101.00)); // 负壹拾亿零壹佰壹拾万零壹佰零壹元整
        System.out.println(NumberKit.double2RMBUppercase(-110101010.10)); // 负壹亿壹仟零壹拾万壹仟零壹拾元壹角
    }
}
