package com.vekai.dataform.mapper;

import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementValidatorMode;
import com.vekai.dataform.model.types.ElementValidatorRunAt;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by luyu on 2018/1/17.
 */
@Component
public class DataFormValidatorRowMapper implements RowMapper<DataFormElement.FormElementValidator> {
    @Override
    public DataFormElement.FormElementValidator mapRow(ResultSet rs, int i) throws SQLException {
        DataFormElement.FormElementValidator validator = new DataFormElement.FormElementValidator ();

        validator.setCode(rs.getString("CODE"));
        validator.setRunAt(ElementValidatorRunAt.valueOf(rs.getString("RUN_AT")));
        validator.setMode(ElementValidatorMode.valueOf(rs.getString("MODE")));
        validator.setExpr(rs.getString("EXPR"));
        validator.setTriggerEvent(rs.getString("TRIGGER_EVENT"));
        validator.setDefaultMessage(rs.getString("MESSAGE"));
        validator.setDefaultMessageI18nCode(rs.getString("MESSAGE_I18N_CODE"));

        return validator;
    }
}
