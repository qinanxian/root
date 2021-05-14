package cn.fisok.sqloy.parserunner;

import cn.fisok.raw.kit.IOKit;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLScriptRunnerTest {

    Connection getConnection(){
// 不同的数据库有不同的驱动
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/rober?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "Amars0ft";

        try {
            // 加载驱动
            Class.forName(driverName);
            // 设置 配置数据
            // 1.url(数据看服务器的ip地址 数据库服务端口号 数据库实例)
            // 2.user
            // 3.password
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("数据库连接成功..");
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    Statement getStatement() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        return statement;
    }

    String getSQLContent(String resource) throws IOException {
        URL url = ResourceUtils.getURL(resource);
        InputStream inputStream = url.openStream();
//        return IOKit.toString(inputStream, Charset.defaultCharset());
        return IOKit.toString(inputStream, "UTF-8");
    }

    @Test
    public void test1() throws TextParseException, IOException, SQLException {
        String content = getSQLContent("classpath:sql/runner1.sql");
//        System.out.println(content);
        Statement stmt = getStatement();
        SQLScriptRunner runner = new SQLScriptRunner(content,stmt);
        runner.setDelimiter("\\s*/\\*SQL@Run(,SkipError)?\\*/");
        runner.setSkipAllError(true);
        runner.exec();
        stmt.close();
    }

    @Test
    public void test2() throws TextParseException, IOException, SQLException {
        String content = "/Users/cytsir/Documents/ws-set/ws-rober/vekai-cabin/vekai-sql/src/test/resources/sql/pdman-v1.0.3.sql";
//        String content = "/Users/cytsir/Documents/ws-set/ws-rober/vekai-cabin/vekai-sql/src/main/resources/sql/runner1.sql";
//        System.out.println(content);
        Statement stmt = getStatement();
        SQLScriptRunner runner = new SQLScriptRunner(content,stmt);
        runner.setDelimiter("\\s*/\\*SQL@Run(,SkipError)?\\*/");
//        runner.setDelimiter(";--");
//        runner.setSkipAllError(true);
        runner.setParseType(TextParse.ParseType.File);
        runner.exec();
        stmt.close();
    }
}
