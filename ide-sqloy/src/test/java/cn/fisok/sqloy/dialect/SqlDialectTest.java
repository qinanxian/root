package cn.fisok.sqloy.dialect;

import cn.fisok.sqloy.BaseTest;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.dialect.impl.DB2Dialect;
import cn.fisok.sqloy.dialect.impl.MySqlDialect;
import cn.fisok.sqloy.dialect.impl.OracleDialect;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.regex.Pattern;

public class SqlDialectTest extends BaseTest{

    @Autowired
    protected BeanCruder beanCruder;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Test
    public void mysqlDialectTest(){
        String sql = "select * from DEMO_PERSON where CODE > :code";
        SqlDialect sqlDialect = new MySqlDialect();
        String pagnationSql = sqlDialect.getPaginationSql(sql,0,12);
        System.out.print("MySQL:"+pagnationSql);

        DataSource dataSource = jdbcTemplate.getDataSource();
        System.out.println(dataSource);
    }

    @Test
    public void oracleDialectTest(){
        String sql = "select * from DEMO_PERSON where CODE > :code";
        SqlDialect sqlDialect = new OracleDialect();
        String pagnationSql = sqlDialect.getPaginationSql(sql,2,12);
        System.out.print("ORACLE:"+pagnationSql);
    }

    @Test
    public void db2DialectTest(){
        String sql = "select * from DEMO_PERSON where CODE > :code";
        SqlDialect sqlDialect = new DB2Dialect();
        String pagnationSql = sqlDialect.getPaginationSql(sql,0,12);
        System.out.print("MySQL:"+pagnationSql);

        DataSource dataSource = jdbcTemplate.getDataSource();
        System.out.println(dataSource);
    }

//    @Test
    public void matchTest(){
        Pattern ALL_COLUMN_PATTERN = Pattern.compile("\\s*\\*\\s*");

        System.out.println(ALL_COLUMN_PATTERN.matcher("*").find());
        System.out.println(ALL_COLUMN_PATTERN.matcher(" * ").find());
        System.out.println(ALL_COLUMN_PATTERN.matcher(" .* ").find());
        System.out.println(ALL_COLUMN_PATTERN.matcher(" ABC.* ").find());
    }
}
