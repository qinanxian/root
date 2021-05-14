package com.vekai.dataform.mapper;

import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataDictCodeMode;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import com.vekai.dataform.model.types.ElementDataFormat;
import com.vekai.dataform.model.types.ElementDataTextAlign;
import com.vekai.dataform.model.types.ElementDataType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by luyu on 2017/12/26.
 */
@Component
public class DataFormElementDBRowMapper implements RowMapper<DataFormElement> {

    public DataFormElement mapRow(ResultSet rs, int i) throws SQLException {
        DataFormElement element = new DataFormElement();
        DataFormElement.FormElementUIHint elementUIHint = element.getElementUIHint();
        element.setDataformId(rs.getString("DATAFORM_ID"));
        element.setCode(rs.getString("CODE"));
        element.setPrimaryKey("Y".equals(rs.getString("PRIMARY_KEY")));
        element.setPrimaryKeyGenerator(rs.getString("PRIMARY_KEY_GENERATOR"));
        element.setSortCode(rs.getString("SORT_CODE"));
        element.setName(rs.getString("NAME"));
        element.setNameI18nCode(rs.getString("NAME_I18N_CODE"));
        element.setColumn(rs.getString("COLUMN_"));
        element.setTable(rs.getString("TABLE_"));
        element.setUpdateable("Y".equals(rs.getString("UPDATEABLE")));
        element.setPersist("Y".equals(rs.getString("PERSIST")));
        String dataType = rs.getString("DATA_TYPE");
        element.setDataType(dataType != null ? ElementDataType.valueOf(dataType) : null);
        element.setDefaultValue(rs.getString("DEFAULT_VALUE"));
        element.setSummaryExpression(rs.getString("SUMMARY_EXPRESSION"));
        element.setEnabled("Y".equals(rs.getString("ENABLED")));
        element.setSortable("Y".equals(rs.getString("SORTABLE")));
        element.setGroup(rs.getString("GROUP_"));
        element.setGroupI18nCode(rs.getString("GROUP_I18N_CODE"));
        element.setSubGroup(rs.getString("SUB_GROUP"));
        element.setSubGroupI18nCode(rs.getString("SUB_GROUP_I18N_CODE"));
        element.setMultiplier(rs.getDouble("MULTIPLIER"));
        element.setLimitedLength(rs.getInt("LIMITED_LENGTH"));
        element.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));



        elementUIHint.setPlaceholder(rs.getString("PLACEHOLDER"));
        elementUIHint.setReadonly("Y".equals(rs.getString("READONLY")));
        elementUIHint.setRequired("Y".equals(rs.getString("REQUIRED")));
        elementUIHint.setDictCodeLazy("Y".equals(rs.getString("DICT_CODE_LAZY")));
        elementUIHint.setDictCodeTreeLeafOnly("Y".equals(rs.getString("DICT_CODE_TREE_LEAF_ONLY")));
        elementUIHint.setDictCodeTreeFull("Y".equals(rs.getString("DICT_CODE_TREE_FULL")));
        String dataFormat = rs.getString("DATA_FORMAT");
        elementUIHint.setDataFormat(dataFormat != null ? ElementDataFormat.valueOf(dataFormat) : null);
        elementUIHint.setMaskFormat(rs.getString("MASK_FORMAT"));
        String textAlign = rs.getString("TEXT_ALIGN");
        elementUIHint.setTextAlign(textAlign != null? ElementDataTextAlign.valueOf(textAlign) : null);
        String editStyle = rs.getString("edit_Style");
        elementUIHint.setEditStyle(editStyle != null ? ElementDataEditStyle.valueOf(editStyle) : null);
        String dictCodeMode = rs.getString("DICT_CODE_MODE");
        elementUIHint.setDictCodeMode(dictCodeMode != null ? ElementDataDictCodeMode.valueOf(dictCodeMode) : null);
        elementUIHint.setDictCodeExpr(rs.getString("DICT_CODE_EXPR"));
        elementUIHint.setPrefix(rs.getString("PREFIX"));
        elementUIHint.setSuffix(rs.getString("SUFFIX"));
        elementUIHint.setTips(rs.getString("TIPS"));
        elementUIHint.setTipsI18nCode(rs.getString("TIPS_I18N_CODE"));
        elementUIHint.setNote(rs.getString("NOTE"));
        elementUIHint.setNoteI18nCode(rs.getString("NOTE_I18N_CODE"));
        elementUIHint.setVisible("Y".equals(rs.getString("VISIBLE")));
        elementUIHint.setRank(rs.getInt("RANK"));
        elementUIHint.setMediaQuery(rs.getString("MEDIA_QUERY"));
        elementUIHint.setHtmlStyle(rs.getString("HTML_STYLE"));
        elementUIHint.setColspan(rs.getInt("COLSPAN"));
        elementUIHint.setEventExpr(rs.getString("EVENT_EXPR"));
        return element;
    }
}
