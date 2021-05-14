package com.vekai.crops.etl.service.kettle;

import com.vekai.crops.etl.jdbc.JDBCConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ODS
 * ERDM
 * 抽数任务状态获取
 */
@Component
public class ETLSynFlagQuery {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("odsJDBCConnector")
    protected JDBCConnector odsJDBCConnector;

    @Autowired
    @Qualifier("erdmJDBCConnector")
    protected JDBCConnector erdmJDBCConnector;


    public List<String> queryODSSuccessTables(String dataDate){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<String> tables=new ArrayList<String>();

        try {//一条sql关联User和Address
            con=odsJDBCConnector.getConnection();
            String sql = "SELECT TABLE_NAME FROM T_SYN_TABLE " +
                    "WHERE RESULT_NOTE='SUCCESS' AND DATADATE=?";
            ps=con.prepareStatement(sql);
            ps.setString(1, dataDate);
            rs=ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tables.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            odsJDBCConnector.close(rs);
            odsJDBCConnector.close(ps);
            odsJDBCConnector.close(con);
        }

        int size = tables.size();
        LOGGER.info("ODS表"+dataDate+"同步成功数量:"+size);
        if(size>100) LOGGER.error("ODS表"+dataDate+"同步数量:"+size+"请修改ETLSynFlagQuery类的查询条件");

        return tables;
    }


    public List<String> queryERDMSuccessTables(String dataDate){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<String> tables=new ArrayList<String>();

        try {//一条sql关联User和Address
            con=erdmJDBCConnector.getConnection();
            String sql = "SELECT TABLE_NAME FROM ETL_SYN_STATUS_ERDM " +
                    "WHERE STATUS='SUCCESS' AND DATADATE=?";
            ps=con.prepareStatement(sql);
            ps.setString(1, dataDate);
            rs=ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");

                tables.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            erdmJDBCConnector.close(rs);
            erdmJDBCConnector.close(ps);
            erdmJDBCConnector.close(con);
        }

        int size = tables.size();
        LOGGER.info("ERDM表"+dataDate+"同步成功数量:"+size);
        if(size>100) LOGGER.error("ERDM表"+dataDate+"同步数量:"+size+"请修改ETLSynFlagQuery类的查询条件");

        return tables;
    }
}
