package cn.fisok.sqloy.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;

import java.util.List;

public class SQLParserTest {

//    @Test
    public void test01() {
        String sql = ""
                + "insert into tar select * from boss_table bo, ("
                + "select a.f1, ff from emp_table a "
                + "inner join log_table b "
                + "on a.f2 = b.f3"
                + ") f "
                + "where bo.f4 = f.f5 "
                + "group by bo.f6 , f.f7 having count(bo.f8) > 0 "
                + "order by bo.f9, f.f10;"
                + "select func(f) from test1; "
                + "";

        String result = SQLUtils.format(sql, JdbcConstants.MYSQL);
        System.out.println(result);

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            SQLASTVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);

        }
    }

//    @Test
    public void test02(){

        String sql = "select * from user order by id";

        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement statement = parser.parseStatement();

        // 使用visitor来访问AST
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);

        System.out.println(visitor.getColumns());
        System.out.println(visitor.getOrderByColumns());
    }

//    @Test
    public void test03(){
        String sql ="select p, s.count as views, (Select count(*) from Comments rc where rc.linkedId=p.id and rc.classcode='InfoPublishs') as commentNumber, (select count(*) from CollectIndexs rci where rci.toId=p.id and rci.classcode='InfoPublishs' and rci.type='favorite') as favorite " +
                "FROM InfoPublishs p,UserScores s ,(select * from USER_INFO) T1 " +
                "where p.id=s.linkedId " +
                "and p.userInfo.id=s.userInfo.id " +
                "and s.classCode='InfoPublishs' " +
                "AND p.status=? ORDER BY p.createtime DESC";

//        StringBuffer select = new StringBuffer();
        List<SQLSelectItem> selectItems = null;
        StringBuffer from = new StringBuffer();
        StringBuffer where = new StringBuffer();

        // parser得到AST
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);
        List<SQLStatement> stmtList = parser.parseStatementList(); //

        // 将AST通过visitor输出
        SQLASTOutputVisitor visitor = SQLUtils.createFormatOutputVisitor(from, stmtList, JdbcUtils.MYSQL);
        SQLASTOutputVisitor whereVisitor = SQLUtils.createFormatOutputVisitor(where, stmtList, JdbcUtils.MYSQL);

        for (SQLStatement stmt : stmtList) {
//       stmt.accept(visitor);
            if(stmt instanceof SQLSelectStatement){
                SQLSelectStatement sstmt = (SQLSelectStatement)stmt;
                SQLSelect sqlselect = sstmt.getSelect();
                SQLSelectQueryBlock query = (SQLSelectQueryBlock)sqlselect.getQuery();

                selectItems = query.getSelectList();
                query.getFrom().accept(visitor);
                query.getWhere().accept(whereVisitor);
            }
        }

        for(SQLSelectItem selectItem : selectItems){
            System.out.println("select:"+SQLUtils.toSQLString(selectItem, JdbcUtils.MYSQL, new SQLUtils.FormatOption(true,false)));

        }
        System.out.println("from:"+from.toString());
        System.out.println("where:"+where);
    }
}
