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
package cn.fisok.raw.kit;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.RawException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:30
 * @desc :
 */
public abstract class SQLKit {
    public static List<String> LOG_SQL_CTX_PARAM_LIST;

    private static Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("(^'\\w*'$)|^\\d+|^NULL$",Pattern.CASE_INSENSITIVE);
    private static Pattern SQL_SUB_SELECT_PATTERN = Pattern.compile("^\\s*\\(.*SELECT\\s+.+\\)\\s*$",Pattern.CASE_INSENSITIVE);
    private static Pattern SELECT_PATTERN = Pattern.compile("\\s*select",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    private static Pattern INSERT_PATTERN = Pattern.compile("\\s*insert",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    private static Pattern UPDATE_PATTERN = Pattern.compile("\\s*update",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    private static Pattern DELETE_PATTERN = Pattern.compile("\\s*delete",Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
    /**
     * 是否为常数列，例如 '','123' as Name，80 as NUMBER_RESULT等
     *
     * @param column column
     * @return boolean
     */
    public static boolean isConstColumn(String column){
        String col = StringKit.nvl(column,"").trim();
        return SINGLE_QUOTE_PATTERN.matcher(col).find();
    }

    /**
     * 判断是否为一个子查询语句
     *
     * @param column column
     * @return boolean
     */
    public static boolean isSubSelect(String column){
        return SQL_SUB_SELECT_PATTERN.matcher(column).find();
    }


    /**
     * 判断是否为一个查询语句
     * @param sql
     * @return
     */
    public static boolean isSelect(String sql){
        return SELECT_PATTERN.matcher(sql).find();
    }
    /**
     * 判断是否为一个插入语句
     * @param sql
     * @return
     */
    public static boolean isInsert(String sql){
        return INSERT_PATTERN.matcher(sql).find();
    }

    /**
     * 判断是否为一个更新语句
     * @param sql
     * @return
     */
    public static boolean isUpdate(String sql){
        return UPDATE_PATTERN.matcher(sql).find();
    }

    /**
     * 判断是否为一个删除语句
     * @param sql
     * @return
     */
    public static boolean isDelete(String sql){
        return DELETE_PATTERN.matcher(sql).find();
    }

    /**
     * SQL的日期转JAVA日期
     *
     * @param sqlDate sqlDate
     * @return Date
     */
    public static Date javaDate(java.sql.Date sqlDate){
        if(sqlDate==null)return null;
        Date date = new Date (sqlDate.getTime());
        return date;
    }
    public static Date javaDate(Time time){
        if(time==null)return null;
        Date date = new Date (time.getTime());
        return date;
    }
    public static Date javaDate(Timestamp time){
        if(time==null)return null;
        Date date = new Date (time.getTime());
        return date;
    }

    /**
     * JAVA日期转SQL日期
     *
     * @param javaDate javaDate
     * @return Date
     */
    public static java.sql.Date sqlDate(Date javaDate){
        if(javaDate==null)return null;
        return new java.sql.Date(javaDate.getTime());
    }

    public static String replaceSQLInjector(String str){
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        return str.replaceAll(reg, "");
    }

    public static List<MapObject> getTableMeta(Connection connection, String table){
        List<MapObject> retList = ListKit.newArrayList();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            String schema = getSchema(connection);
            ResultSet resultSet = meta.getTables(null, "%", table, new String[] { "TABLE" });

            while (resultSet.next()) {
                String tableName=resultSet.getString("TABLE_NAME");

                if(tableName.equals(table)){
                    ResultSet rs = connection.getMetaData().getColumns(null,schema,tableName.toUpperCase(), "%");

                    while(rs.next()){
                        //System.out.println("字段名："+rs.getString("COLUMN_NAME")+"--字段注释："+rs.getString("REMARKS")+"--字段数据类型："+rs.getString("TYPE_NAME"));
                        MapObject row = new MapObject();
                        String columnName = rs.getString("COLUMN_NAME");
                        String comment = StringKit.nvl(rs.getString("REMARKS"),columnName);
                        int dataType = rs.getInt("DATA_TYPE");
//                        String dbType = rs.getString("TYPE_NAME");

                        row.put("columnName", columnName);
                        row.put("comment",comment);
                        row.put("dataType",dataType);

                        retList.add(row);
                    }
                }
            }

            IOKit.close(resultSet);
        } catch (SQLException e) {
            throw new RawException(e);
        } finally {

        }

        return retList;
    }

    private static String getSchema(Connection conn) throws SQLException {
        String schema = conn.getMetaData().getUserName();
        if (StringKit.isBlank(schema)) {
            throw new RawException("数据库schema没获取到");
        }
        return schema.toUpperCase().toString();
    }

    /**
     * 取结果集中,查询出来的原始数据
     *
     * @param rs 结果集
     * @param i 指定行数据的列索引
     * @return object
     * @throws SQLException SQLException
     * @throws IOException IOException
     */
    public static Object getCellValue(ResultSet rs, int i) throws SQLException, IOException {
        ResultSetMetaData metaData = rs.getMetaData();
        int type = metaData.getColumnType(i);
        switch (type){
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NCHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARBINARY:
            case Types.NVARCHAR:
                return rs.getString(i);
            case Types.CLOB:
            case Types.NCLOB:
                return IOUtils.toString(rs.getClob(i).getCharacterStream());
            case Types.BOOLEAN:
                return rs.getBoolean(i);
            case Types.DATE:
                return SQLKit.javaDate(rs.getDate(i));
            case Types.TIME:
                return SQLKit.javaDate(rs.getTime(i));
            case Types.TIMESTAMP:
                return SQLKit.javaDate(rs.getTimestamp(i));
            case Types.INTEGER:
            case Types.TINYINT:
            case Types.SMALLINT:
                return rs.getInt(i);
            case Types.BIGINT:
                return rs.getLong(i);
            case Types.DOUBLE:
            case Types.DECIMAL:
            case Types.NUMERIC:
            case Types.FLOAT:
                return rs.getDouble(i);
            case Types.NULL:
                return null;
            default:
                rs.getString(i);
        }
        return null;
    }

    private static List<Map<String,Object>> pickUpCtxParam(List<Map<String,Object>> mapList){
        List<Map<String,Object>> retList = ListKit.newArrayList();
        for(Object object : mapList){
            if(object instanceof Map){
                Map<String,Object> mapItem = MapKit.newHashMap();
                mapItem.putAll((Map<String,Object>)object);
                Map<String,Object> pickupItem = pickUpCtxParam(mapItem);
                retList.add(pickupItem);
            }
        }
        return retList;
    }
    private static Map<String,Object> pickUpCtxParam(Map<String,Object> map){
        Map<String,Object> ctxParam = MapKit.newLinkedHashMap();
        if(LOG_SQL_CTX_PARAM_LIST!=null&&LOG_SQL_CTX_PARAM_LIST.size()>0){
            for(String name : LOG_SQL_CTX_PARAM_LIST){
                if(map.containsKey(name)){
                    Object value = map.get(name);
                    ctxParam.put(name,value);
                    map.remove(name);
                }
            }
        }
        return ctxParam;
    }

//    public static void logSQL(Logger logger, String summary, String sql, Object param, int paramSize, int result, long costTime){
//
//        //对于上下文保留变量，要作提取处理
//        Object paramShow = param;
//        Map<String,Object> mapParam = MapKit.newHashMap();
//        Map<String,Object> pickMapParam = MapKit.newHashMap();
//        List<Map<String,Object>> listMapParam = null;
//        List<Map<String,Object>> pickListMapParam = null;
//        if(param instanceof Map){
//            mapParam.putAll((Map<String,Object>)param); //复制
//            pickMapParam = pickUpCtxParam(mapParam);
//            paramShow = mapParam;
//        }else if(param instanceof List){
//            listMapParam = (List<Map<String,Object>>)param;
//            pickListMapParam = pickUpCtxParam(listMapParam);
//            paramShow = listMapParam;
//        }
//
//        logger.debug(StringKit.format("┏━━━━━━━━━━ SQL LOG [{0}] ━━━━━━━━━━",summary));
//        logger.debug(StringKit.format("┣ SQL:           {0}",sql));
//
//        if(paramSize<=1){
//            logger.debug(StringKit.format("┣ 参数:            {0}", JSONKit.toJsonString(paramShow)));
//            if(pickMapParam != null && pickMapParam.size()>0){
//                logger.debug(StringKit.format("┣ 上下文参数:         {0}", JSONKit.toJsonString(pickMapParam)));
//            }
//        }else{
//            logger.debug(StringKit.format("┣ 参数([{1}]条):    {0}", JSONKit.toJsonString(paramShow),paramSize));
//            if(pickListMapParam != null && pickListMapParam.size()>0){
//                logger.debug(StringKit.format("┣ 上下文参数([{1}]条): {0}", JSONKit.toJsonString(pickListMapParam)));
//            }
//        }
//
//        logger.debug(StringKit.format("┣ 影响记录/耗时:   [{0}]/[{1}ms]",result,costTime/100));
//        logger.debug(StringKit.format("┗━━━━━━━━━━ SQL LOG [{0}] ━━━━━━━━━━",summary));
//    }

    public static void logSQLStart(Logger logger, String summary, String sql, Object param){

        //对于上下文保留变量，要作提取处理
        Object paramShow = param;
        Map<String,Object> mapParam = MapKit.newHashMap();
        Map<String,Object> pickMapParam = MapKit.newHashMap();
        List<Map<String,Object>> listMapParam = null;
        List<Map<String,Object>> pickListMapParam = null;
        int paramSize = 1;
        if(param instanceof Map){
            mapParam.putAll((Map<String,Object>)param); //复制
            pickMapParam = pickUpCtxParam(mapParam);
            paramShow = mapParam;
        }else if(param instanceof List){
            listMapParam = (List<Map<String,Object>>)param;
            paramSize = listMapParam.size();
            pickListMapParam = pickUpCtxParam(listMapParam);
            paramShow = listMapParam;
        }

        logger.debug(StringKit.format("┏━━━━━━━━━━ SQL LOG [{0}] ━━━━━━━━━━",summary));
        logger.debug(StringKit.format("┣ SQL:           {0}",sql));

        if(paramSize<=1){
            logger.debug(StringKit.format("┣ 参数:            {0}", JSONKit.toJsonString(paramShow)));
            if(pickMapParam != null && pickMapParam.size()>0){
                logger.debug(StringKit.format("┣ 上下文参数:         {0}", JSONKit.toJsonString(pickMapParam)));
            }
        }else{
            logger.debug(StringKit.format("┣ 参数([{1}]条):    {0}", JSONKit.toJsonString(paramShow),paramSize));
            if(pickListMapParam != null && pickListMapParam.size()>0){
                logger.debug(StringKit.format("┣ 上下文参数([{1}]条): {0}", JSONKit.toJsonString(pickListMapParam)));
            }
        }
    }
    public static void logSQLEnd(Logger logger, String summary,int result, long costTime){
        String costTimeText = costTime+"毫秒";
        if(costTime > 1000){
            DecimalFormat format = new DecimalFormat("0.0#");
            costTimeText = format.format(costTime/1000.0)+"秒";
        }
        logger.debug(StringKit.format("┣ 影响记录/耗时:   [{0}条]/[{1}]",result,costTimeText));
        logger.debug(StringKit.format("┗━━━━━━━━━━ SQL LOG [{0}] ━━━━━━━━━━",summary));
    }

}
