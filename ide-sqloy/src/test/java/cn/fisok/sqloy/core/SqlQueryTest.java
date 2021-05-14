package cn.fisok.sqloy.core;

import cn.fisok.raw.lang.PairBond;
import org.junit.Test;

public class SqlQueryTest {
    @Test
    public void test1(){
        SqlQuery select = new SqlQuery("DEMO_PERSON");
        select.setWhere("CODE>:code and NAME LIKE %:name%");

        System.out.println(select.buildQuerySql());

        SqlQuery select1 = new SqlQuery();
        select1.setFrom("DEMO_PERSON");
        select1.setSelect("SELECT DISTINCT");
        select1.getSelectItems().add(new PairBond("ID",""));
        select1.getSelectItems().add(new PairBond("NAME","myName"));
        select1.getSelectItems().add(new PairBond("Gender",""));
        select1.getSelectItems().add(new PairBond("Favor","favor"));
        select1.setWhere("CODE>:code and NAME LIKE %:name%");
        System.out.println(select1.buildQuerySql());
    }
}
