package cn.fisok.sqloy.kit;

import cn.fisok.raw.kit.SQLKit;
import org.junit.Test;

public class SQLKitTest {
    @Test
    public void test1(){
        String[] datas = new String[]{
                "CustName",
                "CUST_NAME",
                "_NAME",
                "NAME_",
                "name",
                "''",
                "'10'",
                "null",
                "Null",
                "NULL",
                "'A10'",
                "10A",
                "A10",
        };
//        for(String s:datas){
//            System.out.println(s+"->"+SQLKit.isConstColumn(s));
//        }

        System.out.println(SQLKit.isSubSelect("(select count(*) from DEMO_PERSON_BANK_CARD X where X.PERSON_ID=DEMO_PERSON.ID) "));
        System.out.println(SQLKit.isSubSelect("(select count(*) from DEMO_PERSON_BANK_CARD X where X.PERSON_ID=DEMO_PERSON.ID) AS REVISION"));
        System.out.println(SQLKit.isSubSelect("select count(*) from DEMO_PERSON_BANK_CARD X where X.PERSON_ID=DEMO_PERSON.ID"));
        System.out.println(SQLKit.isSubSelect("insert select count(*) from DEMO_PERSON_BANK_CARD X where X.PERSON_ID=DEMO_PERSON.ID"));
        System.out.println(SQLKit.isSubSelect("update count(*) from DEMO_PERSON_BANK_CARD X where X.PERSON_ID=DEMO_PERSON.ID"));
    }
}
