/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.sqloy.parserunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 包装一条可执行SQL语句
 */
public class SQLExecItem implements Serializable {
    private static final long serialVersionUID = 318462912584071469L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * SQL语句类型
     */
    public enum SQLItemType {
        UNKNOW,

        DML_SELECT,
        DML_INSERT,
        DML_UPDATE,
        DML_DELETE,
        DML_CALL_PROC,
        DML_TRUNCATE,

        DDL_CREATE_TABLE,
        DDL_DROP_TABLE,
        DDL_CREATE_VIEW,
        DDL_DROP_VIEW,
        DDL_ALTER_TABLE,
        DDL_CREATE_PROC,
        DDL_DROP_PROC,
        DDL_CREATE_FUNCTION,
        DDL_DROP_FUNCTION,
        DDL_CREATE_INDEX,
        DDL_DROP_INDEX
    }

    ;

    public static final Pattern DML_SELECT = Pattern.compile("^\\s*select\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DML_INSERT = Pattern.compile("^\\s*insert\\s+into\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DML_UPDATE = Pattern.compile("^\\s*update\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DML_DELETE = Pattern.compile("^\\s*delete\\s+from\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DML_CALL_PROC = Pattern.compile("^\\s*call\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DML_TRUNCATE = Pattern.compile("^\\s*truncate\\s+table\\s+", Pattern.CASE_INSENSITIVE);

    public static final Pattern DDL_CREATE_TABLE = Pattern.compile("^\\s*create\\s+table\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_DROP_TABLE = Pattern.compile("^\\s*drop\\s+table\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_CREATE_VIEW = Pattern.compile("^\\s*create\\s+(or\\s+replace\\s+)*view", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_DROP_VIEW = Pattern.compile("^\\s*drop\\s+view", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_ALTER_TABLE = Pattern.compile("^\\s*alter\\s+table", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_CREATE_PROC = Pattern.compile("^\\s*create\\s+(or\\s+replace\\s+)*procedure", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_DROP_PROC = Pattern.compile("^\\s*drop\\s+(specific\\s+)*procedure", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_CREATE_FUNCTION = Pattern.compile("^\\s*create\\s+(or\\s+replace\\s+)*function", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_DROP_FUNCTION = Pattern.compile("^\\s*drop\\s+(specific\\s+)*function", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_CREATE_INDEX = Pattern.compile("^\\s*create\\s+index\\s+", Pattern.CASE_INSENSITIVE);
    public static final Pattern DDL_DROP_INDEX = Pattern.compile("^\\s*drop\\s+index\\s+", Pattern.CASE_INSENSITIVE);

    private String sql;
    private SQLItemType sqlItemType = SQLItemType.UNKNOW;

    public SQLExecItem(String sql) {
        sql = sql.replaceAll("^\\s+", "");
        this.sql = sql.replaceAll("\\s+$", "");
        analySql();
        this.sql = this.sql.replaceAll(";+$", "");
    }

    /**
     * 返回可以执行的SQL语句
     *
     * @return String
     */
    public String getExecSql() {
        return this.sql;
    }

    /**
     * 分析SQL语句的动作
     */
    private void analySql() {
        if (DML_SELECT.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_SELECT;
        } else if (DML_INSERT.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_INSERT;
        } else if (DML_UPDATE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_UPDATE;
        } else if (DML_DELETE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_DELETE;
        } else if (DML_CALL_PROC.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_CALL_PROC;
        } else if (DML_TRUNCATE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DML_TRUNCATE;
        } else if (DDL_CREATE_TABLE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_CREATE_TABLE;
        } else if (DDL_DROP_TABLE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_DROP_TABLE;
        } else if (DDL_CREATE_VIEW.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_CREATE_VIEW;
        } else if (DDL_DROP_VIEW.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_DROP_VIEW;
        } else if (DDL_ALTER_TABLE.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_ALTER_TABLE;
        } else if (DDL_CREATE_PROC.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_CREATE_PROC;
        } else if (DDL_DROP_PROC.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_DROP_PROC;
        } else if (DDL_CREATE_FUNCTION.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_CREATE_FUNCTION;
        } else if (DDL_DROP_FUNCTION.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_DROP_FUNCTION;
        } else if (DDL_CREATE_INDEX.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_CREATE_INDEX;
        } else if (DDL_DROP_INDEX.matcher(sql).find()) {
            sqlItemType = SQLItemType.DDL_DROP_INDEX;
        }
    }

    /**
     * SQL语句类型
     *
     * @return SQLItemType
     */
    public SQLItemType getSQLType() {
        return sqlItemType;
    }

    /**
     * 执行当前SQL项
     *
     * @param stmt        stmt
     * @param skipError   是否忽略错误
     * @param sqlWarmTime SQL警告时间
     * @throws SQLException SQLException
     */
    public void exec(Statement stmt, boolean skipError, long sqlWarmTime) throws SQLException {
        logger.debug("开始执行SQL:\n" + getExecSql());
        try {
            long startTime = System.currentTimeMillis();
            switch (sqlItemType) {
                case DML_SELECT:
                    execSelect(stmt);
                    break;
                case DML_INSERT:
                case DML_UPDATE:
                case DML_DELETE:
                    execUpdate(stmt);
                    break;
                case DML_CALL_PROC:
                    execProc(stmt);
                    break;
                default:
                    execDefault(stmt);
                    break;
            }
            long endTime = System.currentTimeMillis();
            if (endTime - startTime >= sqlWarmTime) {
                logger.warn("运行时长:" + DateTimeConverter.longSecond2HMS(endTime - startTime) + ",SQL:" + getExecSql());
            } else {
                logger.debug("运行时长:" + DateTimeConverter.longSecond2HMS(endTime - startTime));
            }
        } catch (SQLException e) {
            if (skipError) {
                logger.warn(MessageFormat.format("执行出错，忽略!SQL:{0}", getExecSql()));
            } else {
                throw e;
            }
        }
    }

    /**
     * 执行选择，输出选择的数据
     *
     * @param stmt stmt
     * @throws SQLException SQLException
     */
    protected void execSelect(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery(getExecSql());

        //输出选择数据的表头部分
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        StringBuffer line = new StringBuffer("");
        for (int i = 1; i <= columnCount; i++) {
            if (i != 1) line.append("\t");
            line.append(metadata.getColumnName(i));
        }
        logger.debug(line.toString());
        line = new StringBuffer("");
        //输出数据部分
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i != 1) line.append("\t");
                int colType = metadata.getColumnType(i);
                if (colType == Types.DOUBLE
                        || colType == Types.DECIMAL
                        || colType == Types.NUMERIC
                ) {
                    line.append(rs.getDouble(i));
                } else if (colType == Types.FLOAT) {
                    line.append(rs.getFloat(i));
                } else if (colType == Types.BIGINT || colType == Types.INTEGER) {
                    line.append(rs.getInt(i));
                } else {
                    line.append(rs.getString(i));
                }
            }
        }
        logger.debug(line.toString());
    }

    /**
     * 执行修改更新，包括插入，删除，更新
     *
     * @param stmt stmt
     * @throws SQLException SQLException
     */
    protected void execUpdate(Statement stmt) throws SQLException {
        int totalNumber = stmt.executeUpdate(getExecSql());
        String action = "未知动作";
        switch (sqlItemType) {
            case DML_INSERT:
                action = "成功插入";
                break;
            case DML_UPDATE:
                action = "成功更新";
                break;
            case DML_DELETE:
                action = "成功删除";
                break;
            default:
                break;
        }
        logger.debug(MessageFormat.format("{0}({1})行", action, totalNumber));
    }

    /**
     * 执行存储过程
     *
     * @param stmt stmt
     * @throws SQLException SQLException
     */
    protected void execProc(Statement stmt) throws SQLException {
        String callSQL = getExecSql();
        CallableStatement cstmt = null;
        try {
            Connection connection = stmt.getConnection();
            if (!callSQL.startsWith("{")) {
                callSQL = "{" + callSQL + "}";
            }
            cstmt = connection.prepareCall(callSQL);

            //如果有参数，则需要对存储过程的参数部分单独处理
            String args = callSQL.substring(callSQL.indexOf("(") + 1);
            args = args.substring(0, args.lastIndexOf(")"));
            args = args.replaceAll("\\s+", "");
//			if(!StringX.isEmpty(args)){
            if (args != null && args.replaceAll("\\s+", "").length() > 0) {
                String[] argItems = args.split(",");
                int argItemIdx = 1;
                for (int i = 0; i < argItems.length; i++) {
                    if (argItems[i].equals("?") || argItems[i].startsWith("@")) {
                        cstmt.registerOutParameter(argItemIdx, Types.VARCHAR);
                        argItemIdx++;
                    }
                }
            }

            cstmt.execute();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (cstmt != null) {
                cstmt.close();
            }
        }
    }

    /**
     * 其它的全部执行这个
     *
     * @param stmt stmt
     * @throws SQLException SQLException
     */
    protected void execDefault(Statement stmt) throws SQLException {
        stmt.execute(getExecSql());
        logger.debug("命令执行成功");
    }

}
