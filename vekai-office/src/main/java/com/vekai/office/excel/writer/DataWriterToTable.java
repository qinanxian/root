package com.vekai.office.excel.writer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vekai.office.excel.imports.config.ExcelImportConfig;
import com.vekai.office.excel.imports.config.ExcelImportConfig.ColumnItem;
import com.vekai.office.excel.imports.intercept.InterceptException;
import com.vekai.office.excel.imports.intercept.InterceptHelper;
import com.vekai.office.excel.reader.ExcelRowData;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


/**
 * 数据写入到数据表中
 *
 * @author yangsong
 * @since 2014/04/03
 */
public class DataWriterToTable implements DataWriter<List<ExcelRowData>> {
    protected JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DataWriterToTable() {
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private int count = 0;
    private int sum = 0;

    public void write(List<ExcelRowData> data, ExcelImportConfig config)
            throws WriterException {
        Connection connection = null;
        try {
//            connection = jdbcTemplate.getDataSource().getConnection();
            connection = ApplicationContextHolder.getBean(DataSource.class).getConnection();
        } catch (SQLException e1) {
            throw new WriterException(e1);
        }
        TableWriter writer = new TableWriter(config, connection);
        try {
            sum = data.size();
            writer.openPrepareStatement();
            for (int i = 0; i < sum; i++) {
                ExcelRowData rowData = data.get(i);
                //写拦截器-before
                beforeWriteRow(rowData, config);

                //写操作
                count += writer.writeRow(rowData);

                //写拦截器-after
                afterWriteRow(rowData, config);

                //控制批量提交粒度
                if (i % config.getCommitNumber() == 0) {
                    writer.commitBatch();
                }
            }
            writer.commitBatch();
            //写拦截器-complete
            writeComplete(data, config);
            logger.debug("插入[" + count + "]条记录");
        } catch (SQLException e) {
            throw new WriterException("写入数据时，数据库出错", e);
        } catch (Exception e) {
            throw new WriterException("写入数据时，未知错误", e);
        } finally {
            if (writer != null) {
                try {
                    writer.closePrepareStatement();
                } catch (SQLException e) {
                    throw new WriterException("关闭游标出错", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new WriterException("关闭连接出错", e);
                }
            }
        }

    }

    private void beforeWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws WriterException {
        if (config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.beforeWriteRow(config.getIntercepts(), rowData, config);
        } catch (InterceptException e) {
            throw new WriterException(e);
        }
    }

    private void afterWriteRow(ExcelRowData rowData, ExcelImportConfig config) throws WriterException {
        if (config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.afterWriteRow(config.getIntercepts(), rowData, config);
        } catch (InterceptException e) {
            throw new WriterException(e);
        }
    }

    private void writeComplete(List<ExcelRowData> data, ExcelImportConfig config) throws WriterException {
        if (config.getIntercepts().size() == 0) return;
        //拦截器beforeReadRow方法
        try {
            InterceptHelper.writeComplete(config.getIntercepts(), data, config);
        } catch (InterceptException e) {
            throw new WriterException(e);
        }
    }

    /**
     * 写入到数据表中
     *
     * @author Administrator
     */
    private class TableWriter {
        private ExcelImportConfig config;
        private Connection connection;

        private PreparedStatement psExistSelect = null;
        private PreparedStatement psDelete = null;
        private PreparedStatement psInsert = null;
        private PreparedStatement psUpdate = null;
        private Statement stQueryMetadata = null;
        private ResultSetMetaData metaData;
        private Map<String, Integer> dataTypeMap = null;

        public TableWriter(ExcelImportConfig config, Connection connection) {
            this.config = config;
            this.connection = connection;
            this.dataTypeMap = new HashMap<String, Integer>();
        }

        /**
         * 打开所有的PrepareStatement
         *
         * @throws SQLException
         */
        private void openPrepareStatement() throws SQLException {
            initMetadata();
            initPSExistsSelect();
            initPSDelete();
            initPSInsert();
            initPSUpdate();

            //擦除模式下，首先删除所有数据
            if (config.getConflictSolveModel() == ExcelImportConfig.ConflictSolveModel.Erase) {
                deleteAll();
            }
        }

        /**
         * 关闭所有的PrepareStatement
         *
         * @throws SQLException
         */
        private void closePrepareStatement() throws SQLException {
            if (psExistSelect != null) {
                psExistSelect.close();
            }
            if (psDelete != null) {
                psDelete.close();
            }
            if (psInsert != null) {
                psInsert.close();
            }
            if (psUpdate != null) {
                psUpdate.close();
            }
            if (stQueryMetadata != null) {
                stQueryMetadata.close();
            }
        }


        public int writeRow(ExcelRowData rowData) throws WriterException {
            try {
                if (config.getConflictSolveModel() == ExcelImportConfig.ConflictSolveModel.Insert) {    //插入模式，数据重复抛错
                    if (exists(rowData)) {
                        logger.trace(MessageFormat.format("当前模式“插入”，但数据已存在。数据:{0}", rowData));
                        return 0;
                    } else {
                        insert(rowData);
                    }
                } else if (config.getConflictSolveModel() == ExcelImportConfig.ConflictSolveModel.Update) {    //更新模式直接更新
                    if (exists(rowData)) {    //存在，则更新
                        logger.trace(MessageFormat.format("数据已存在，更新。数据:{0}",rowData));
                        update(rowData);
                    } else {
                        insert(rowData);    //不存在，则插入
                    }
                } else {
                    insert(rowData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new WriterException(e);
            }
            return 1;
        }

        /**
         * 批量提交
         *
         * @throws SQLException
         */
//		private int commitCount = 0;
        public void commitBatch() throws SQLException {
            psInsert.executeBatch();
            psUpdate.executeBatch();
//			commitCount ++;
//			System.out.println("Commit:"+commitCount);
        }

        private boolean exists(ExcelRowData rowData) throws SQLException {
            List<ExcelImportConfig.ColumnItem> items = config.getAllColumnItems();
            int idx = 1;
            for (int i = 0; i < items.size(); i++) {
                ExcelImportConfig.ColumnItem item = items.get(i);
                if (inKeyColumns(item.getName())) {
                    setParameterValue(psExistSelect, idx++, item, rowData.getDataElement(item.getName()));
                }
            }
            ResultSet rs = psExistSelect.executeQuery();
            if (rs.next()) return true;
            else return false;
        }

        private void deleteAll() throws SQLException {
            int deleteCount = psDelete.executeUpdate();
            logger.debug(MessageFormat.format("总共删除记录{0}条", deleteCount));
        }

        //		private int insertCount = 0;
        private void insert(ExcelRowData rowData) throws SQLException {
            List<ColumnItem> items = config.getAllColumnItems();
            int idx = 1;
//			System.out.println(insertCount+"-"+items.size()+":"+rowData);
            logger.trace("插入:" + rowData.toString());
            for (int i = 0; i < items.size(); i++) {
                ColumnItem item = items.get(i);
                if (!columnExists(item.getName())) continue;
                setParameterValue(psInsert, idx++, item, rowData.getDataElement(item.getName()));
            }
            psInsert.addBatch();
//			insertCount ++;
            //System.out.println(insertCount);
        }

        private void update(ExcelRowData rowData) throws SQLException {
            logger.trace("更新:" + rowData.toString());
            List<ColumnItem> items = config.getAllColumnItems();
            int idx = 1;
            for (int i = 0; i < items.size(); i++) {
                ColumnItem item = items.get(i);
                if (!inKeyColumns(item.getName())) {
                    if (!columnExists(item.getName())) continue;
                    setParameterValue(psUpdate, idx++, item, rowData.getDataElement(item.getName()));
                }
            }
            for (int i = 0; i < items.size(); i++) {
                ColumnItem item = items.get(i);
                if (inKeyColumns(item.getName())) {
                    if (!columnExists(item.getName())) continue;
                    setParameterValue(psUpdate, idx++, item, rowData.getDataElement(item.getName()));
                }
            }
            psUpdate.addBatch();
        }



        public java.util.Date parse(String str){
            String fixedStr = StringKit.nvl(str,"");
            //如果是数字字串，则直接转下
            if(fixedStr.matches("\\d+")){
                return DateKit.parse(Long.parseLong(str));
            }
            if(fixedStr.indexOf("/")>0){
                fixedStr = fixedStr.replaceAll("/+","-");
            }
            fixedStr = fixedStr.replaceAll("-+","-");

            try{
                DateTimeFormatter format = DateTimeFormat.forPattern(DateKit.DATE_TIME_MS_FORMAT);
                return DateTime.parse(fixedStr,format).toDate();
            }catch(IllegalArgumentException e){
                try{
                    DateTimeFormatter format = DateTimeFormat.forPattern(DateKit.DATE_TIME_FORMAT);
                    return DateTime.parse(fixedStr,format).toDate();
                }catch(IllegalArgumentException e1){
                    try{
                        DateTimeFormatter format = DateTimeFormat.forPattern(DateKit.DATE_FORMAT);
                        return DateTime.parse(fixedStr,format).toDate();
                    }catch (IllegalArgumentException e2){
                        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd");
                        return DateTime.parse(fixedStr,format).toDate();
                    }
                }
            }
        }

        /**
         * 找到参数的位置，并且设置值
         *
         * @param ps
         * @param index
         * @param item
         * @param element
         * @throws SQLException
         */
        private void setParameterValue(PreparedStatement ps, int index, ColumnItem item, ValueObject element) throws SQLException {

            int dataType = getColumnDataType(item.getName());
            switch (dataType) {
                case Types.VARCHAR:
                case Types.CHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                case Types.LONGVARBINARY:
                    ps.setString(index, element.strValue());
                    break;
                case Types.DECIMAL:
                case Types.DOUBLE:
                case Types.FLOAT:
                    ps.setDouble(index, element.doubleValue());
                    break;
                case Types.INTEGER:
                    ps.setInt(index, element.intValue());
                    break;
                case Types.DATE:
                case Types.TIMESTAMP:
                    java.util.Date elementDateValue = null;
                    try{
                        elementDateValue = element.dateValue();
                    }catch (Exception e){
                        elementDateValue = parse(element.strValue());
                    }
                    if(elementDateValue != null){
                        ps.setDate(index, new Date(elementDateValue.getTime()));
                    }
                    break;
                default:
                    ps.setString(index, element.strValue());
                    break;
            }
        }

        /**
         * 获取指定列的数据类型
         */
        public int getColumnDataType(String name) {
            return dataTypeMap.get(name.toUpperCase());
        }

        /**
         * 生成按主键查询的Where子句
         *
         * @return
         */
        private String genKeyQueryWhereClause() {
            StringBuilder sbSQL = new StringBuilder();
            String[] keyColumns = config.getKeyColumn().split(",");
            for (String keyItem : keyColumns) {
                sbSQL.append(" and ").append(keyItem).append("=?");
            }
            return sbSQL.toString();
        }

        /**
         * 判断指定的字段是否在主键字段中
         *
         * @param column
         * @return
         */
        private boolean inKeyColumns(String column) {
            String[] keyColumns = config.getKeyColumn().split(",");
            for (String keyItem : keyColumns) {
                if (keyItem.equalsIgnoreCase(column)) return true;
            }
            return false;
        }

        /**
         * 生成查询数据是否存在的查询PreparedStatement
         *
         * @throws SQLException
         */
        private void initPSExistsSelect() throws SQLException {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("select 1 from ").append(config.getTable());
//			sbSQL.append(" where 1=1 and GuarantyStatus not in ('04','06') ");//已出库和已归还的除外
            sbSQL.append(" where 1=1 ");//已出库和已归还的除外
            sbSQL.append(genKeyQueryWhereClause());
            logger.debug("PrepareSelect:" + sbSQL.toString());
            psExistSelect = connection.prepareStatement(sbSQL.toString());
        }

        /**
         * 生成删除数据PreparedStatement
         *
         * @throws SQLException
         */
        private void initPSDelete() throws SQLException {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("delete from ").append(config.getTable());
            logger.debug("PrepareDelete:" + sbSQL.toString());
            psDelete = connection.prepareStatement(sbSQL.toString());
        }

        /**
         * 生成查询数据是否存在的查询PreparedStatement
         *
         * @throws SQLException
         */
        private void initPSInsert() throws SQLException {
            List<ColumnItem> items = config.getAllColumnItems();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("insert into ").append(config.getTable());
            sbSQL.append("(");
            //字段部分
            for (int i = 0; i < items.size(); i++) {
                if (!columnExists(items.get(i).getName())) continue;
                ColumnItem item = items.get(i);
                if (i != 0) sbSQL.append(",");
                sbSQL.append(item.getName());
            }

            sbSQL.append(") values(");

            //值部分
            for (int i = 0; i < items.size(); i++) {
                if (!columnExists(items.get(i).getName())) continue;
                if (i != 0) sbSQL.append(",");
                sbSQL.append("?");
            }

            sbSQL.append(")");
            logger.debug("PrepareInsert:" + sbSQL.toString());
            psInsert = connection.prepareStatement(sbSQL.toString());
        }

        /**
         * 生成查询数据是否存在的查询PreparedStatement
         *
         * @throws SQLException
         */
        private void initPSUpdate() throws SQLException {
            List<ColumnItem> items = config.getAllColumnItems();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("update ").append(config.getTable());
            sbSQL.append(" set ");

            //生成”字段=值”部分
            boolean first = true;
            for (int i = 0; i < items.size(); i++) {
                if (!columnExists(items.get(i).getName())) continue;
                ColumnItem item = items.get(i);
                if (inKeyColumns(item.getName())) continue;

                if (!first) sbSQL.append(",");
                sbSQL.append(item.getName()).append("=?");
                first = false;
            }

            sbSQL.append(" where 1=1");
            sbSQL.append(genKeyQueryWhereClause());

            logger.debug("PrepareUpdate:" + sbSQL.toString());
            psUpdate = connection.prepareStatement(sbSQL.toString());
        }

        private void initMetadata() throws SQLException {
            stQueryMetadata = connection.createStatement();
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("select * from ").append(config.getTable()).append(" where 1=2");
            logger.debug("QueryMetadata:" + sbSQL.toString());
            metaData = stQueryMetadata.executeQuery(sbSQL.toString()).getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String name = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                dataTypeMap.put(name.toUpperCase(), columnType);
            }
        }

        /**
         * 指定的数据列是否存在
         *
         * @param colName
         * @return
         */
        private boolean columnExists(String colName) {
            return dataTypeMap.containsKey(colName.toUpperCase());
        }

    }

    public WriteResult getResult() {
        WriteResult result = new WriteResult();
        result.setReadRows(sum);    //总共读入记录{0}条
        result.setWriteRows(count); //其中写入记录{0}条
//        Map<String, Object> result = new HashMap<String, Object>();
//        result.put("Sum", MessageFormat.format("总共读入记录{0}条", sum));
//        result.put("Count", MessageFormat.format("其中写入记录{0}条", count));
        return result;
    }



}
