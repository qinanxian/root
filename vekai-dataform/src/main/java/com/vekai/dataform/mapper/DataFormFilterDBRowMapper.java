package com.vekai.dataform.mapper;

import com.vekai.dataform.model.types.ElementFilterComparePattern;
import com.vekai.dataform.model.DataFormFilter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by luyu on 2018/1/17.
 */
@Component
public class DataFormFilterDBRowMapper implements RowMapper<DataFormFilter> {
    public DataFormFilter mapRow(ResultSet rs, int i) throws SQLException {
        DataFormFilter filter = new DataFormFilter();
        filter.setDataformId(rs.getString("DATAFORM_ID"));
        filter.setCode(rs.getString("CODE"));
        filter.setName(rs.getString("NAME"));
        filter.setBindFor(rs.getString("BIND_FOR"));
        filter.setEnabled("Y".equals(rs.getString("ENABLED")));
        filter.setQuick("Y".equals(rs.getString("QUICK")));
        filter.setSortCode(rs.getString("SORT_CODE"));
        String comparePattern = rs.getString("COMPARE_PATTERN");
        filter.setComparePattern(comparePattern != null ? ElementFilterComparePattern.valueOf(comparePattern) : null);
        return filter;
    }
}
