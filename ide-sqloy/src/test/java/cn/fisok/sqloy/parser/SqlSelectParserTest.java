package cn.fisok.sqloy.parser;

import cn.fisok.sqloy.core.DBType;
import cn.fisok.raw.lang.PairBond;
import org.junit.Test;

import java.util.List;

public class SqlSelectParserTest {

    @Test
    public void testParse(){
        String sql ="select p, s.count as views, (Select count(*) from Comments rc where rc.linkedId=p.id and rc.classcode='InfoPublishs') as commentNumber, (select count(*) from CollectIndexs rci where rci.toId=p.id and rci.classcode='InfoPublishs' and rci.type='favorite') as favorite " +
                "FROM InfoPublishs p,UserScores s ,(select * from USER_INFO) T1 " +
                "where p.id=s.linkedId " +
                "and p.userInfo.id=s.userInfo.id " +
                "and s.classCode='InfoPublishs' " +
                "AND p.status=:status ORDER BY p.createtime DESC";
        sql = "select * from DEMO_PERSON order by CREATE_TIME ASC";
//        sql = "select * from DEMO_PERSON";

        SqlSelectParser parser = new SqlSelectParser(sql, DBType.MYSQL);
        parser.setUpperCase(true);
        parser.setPrettyFormat(true);
        parser.parse();

        List<PairBond<String,String>> orderItems = parser.getOrderItems();

//        System.out.println("SELECT:"+parser.getSelect());
//        System.out.println("FROM:"+parser.getFrom());
//        System.out.println("COLUMNS:"+parser.getColumns());
//        System.out.println("FROM:"+parser.getFrom());
//        System.out.println("FROM-TABLE:"+parser.getFromTableAlias());
//        System.out.println("WHERE:"+parser.getWhere());
        System.out.println("----------------------");
//        System.out.println(parser.format());
        System.out.println(parser.sql());
        System.out.println("----------------------");
    }

//    @Test
    public void testParsePaginationSQL(){
        String sql = "select * from DEMO_PERSON,AUTH_USER U where CODE > :code";
//        String sql = "select DEMO_PERSON.* from DEMO_PERSON where CODE > :code";
//        String sql = "select * from DEMO_PERSON where CODE > :code";
//        String sql = "select * from DEMO_PERSON A where CODE > :code";

        SqlSelectParser parser = new SqlSelectParser(sql, DBType.MYSQL);
        parser.setUpperCase(true);
        parser.setPrettyFormat(true);
        parser.parse();

        System.out.println("SELECT:"+parser.getSelect());
        System.out.println("FROM:"+parser.getFrom());
        System.out.println("COLUMNS:"+parser.getColumns());
        System.out.println("FROM:"+parser.getFrom());
        System.out.println("FROM-TABLE:"+parser.getFromTableAlias());
        System.out.println("WHERE:"+parser.getWhere());
        System.out.println("----------------------");
//        System.out.println(parser.format());
        System.out.println("----------------------");
    }
}
