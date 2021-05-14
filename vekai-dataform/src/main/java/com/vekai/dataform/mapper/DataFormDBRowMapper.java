package com.vekai.dataform.mapper;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.types.FormDataModelType;
import com.vekai.dataform.model.types.FormStyle;
import cn.fisok.sqloy.core.SqlQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by luyu on 2017/12/26.
 */
@Component
public class DataFormDBRowMapper implements RowMapper<DataForm> {

    public DataForm mapRow(ResultSet rs, int i) throws SQLException {
        DataForm dataForm = new DataForm();
        dataForm.setId(rs.getString("ID"));
        dataForm.setPack(rs.getString("PACK"));
        dataForm.setCode(rs.getString("CODE"));
        String dataModelType = rs.getString("DATA_MODEL_TYPE");
        dataForm.setDataModelType(dataModelType != null ? FormDataModelType.valueOf(dataModelType) : null);
        dataForm.setName(rs.getString("NAME"));
        dataForm.setDescription(rs.getString("DESCRIPTION"));
        dataForm.setTags(rs.getString("TAGS"));
        dataForm.setSortCode(rs.getString("SORT_CODE"));
        dataForm.setClassification(rs.getString("CLASSIFICATION"));
        dataForm.setDataModel(rs.getString("DATA_MODEL"));
        dataForm.setHandler(rs.getString("HANDLER"));

        SqlQuery sqlQuery = new SqlQuery();
        sqlQuery.setSelect(rs.getString("SQL_SELECT"));
        sqlQuery.setFrom(rs.getString("SQL_FROM"));
        sqlQuery.setWhere(rs.getString("SQL_WHERE"));
        sqlQuery.setGroupBy(rs.getString("SQL_GROUP"));
        sqlQuery.setHaving(rs.getString("SQL_HAVING"));
        sqlQuery.setOrderBy(rs.getString("SQL_ORDER"));
        dataForm.setQuery(sqlQuery);

        DataForm.DataFormUIHint dataFormUIHint = new DataForm.DataFormUIHint();
        dataFormUIHint.setColumnNumber(rs.getInt("COLUMN_NUMBER"));
        String formStyle = rs.getString("FORM_STYLE");
        dataFormUIHint.setFormStyle(formStyle != null? FormStyle.valueOf(formStyle) : null);
        dataForm.setFormUIHint(dataFormUIHint);

        return dataForm;
    }
}
