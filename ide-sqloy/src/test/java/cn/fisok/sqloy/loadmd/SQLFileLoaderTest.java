package cn.fisok.sqloy.loadmd;

import cn.fisok.sqloy.loadmd.impl.SQLTextLoaderImpl;
import org.junit.Test;

public class SQLFileLoaderTest {
//    @Autowired
//    protected SQLFileLoader loader;
    @Test
    public void test01(){
        SQLTextLoaderImpl loader = new SQLTextLoaderImpl();

        String res = "classpath:sql/dashboard.sql.md";
        String text = loader.parse(res)
                .sql("getUserBoards");

        System.out.println(text);
    }
}
