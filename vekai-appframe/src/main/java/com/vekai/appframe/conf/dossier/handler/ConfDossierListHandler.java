package com.vekai.appframe.conf.dossier.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.conf.dossier.model.ConfDossier;
import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class ConfDossierListHandler extends BeanDataListHandler<ConfDossier> {

    public Integer delete(DataForm dataForm, List<ConfDossier> dataList) {
        Integer result = 0;

        if(dataList == null) throw new BizException("删除数据出错，数据列表不存在");

        for (ConfDossier confDossier : dataList) {
            String defKey = confDossier.getDossierDefKey();
            result += beanCruder.execute("DELETE FROM CONF_DOSSIER      WHERE DOSSIER_DEF_KEY=:defKey", MapKit.mapOf("defKey", defKey));
            result += beanCruder.execute("DELETE FROM CONF_DOSSIER_ITEM WHERE DOSSIER_DEF_KEY=:defKey", MapKit.mapOf("defKey", defKey));
        }

        return result;
    }

    public PaginResult<ConfDossier> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        String classification = StringKit.nvl(queryParameters.get("classification"), "_ALL_");
        String where = StringKit.nvl(dataForm.getQuery().getWhere(),"");
        if (!"_ALL_".equalsIgnoreCase(classification)) {
            String myCondition = "CLASSIFICATION=:classification";

            if(StringKit.isBlank(where)){
                dataForm.getQuery().setWhere(where +        myCondition);
            }else{
                dataForm.getQuery().setWhere(where +" AND "+myCondition);
            }
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }

    @Transactional
    public Integer clone(DataForm dataForm, MapObject object) {
        //1. 取原始模板号，新模板号
        String tplDossierDefKey = object.getValue("tplDossierDefKey").strValue();
        String newDossierDefKey = object.getValue("newDossierDefKey").strValue();
        if(StringKit.isBlank(tplDossierDefKey) && StringKit.isBlank(newDossierDefKey)){
            throw new BizException("复制失败，源模板号:{0}，目标模板号:{1}",tplDossierDefKey,newDossierDefKey);
        }
        //1.1 检查新的模板号是否存在
        List<ConfDossier> newDossierListTmp = beanCruder.selectList(ConfDossier.class,
                "select * from CONF_DOSSIER WHERE DOSSIER_DEF_KEY=:dossierDefKey",
                MapKit.mapOf("dossierDefKey",newDossierDefKey));
        if (newDossierListTmp.size() > 0) throw new BizException("复制失败，目标模板号:{0}已存在",newDossierDefKey);

        //2. 查询复制模板对象
        ConfDossier dossierListObject = beanCruder.selectOne(ConfDossier.class,
                "select * from CONF_DOSSIER WHERE DOSSIER_DEF_KEY=:dossierDefKey",
                MapKit.mapOf("dossierDefKey",tplDossierDefKey));
        List<ConfDossierItem> dossierListItemList = beanCruder.selectList(ConfDossierItem.class, "SELECT * FROM" +
                " CONF_DOSSIER_ITEM WHERE DOSSIER_DEF_KEY=:dossierDefKey", MapKit.mapOf("dossierDefKey", tplDossierDefKey));
        if(dossierListItemList == null)  throw new BizException("复制失败，源模板号:{0}对应的数据不存在",tplDossierDefKey);


        //3. 修改模板代码名称
        Integer result = 0;
        dossierListObject.setDossierDefKey(newDossierDefKey);
        for (ConfDossierItem dossierListItemPO: dossierListItemList) {
            dossierListItemPO.setDossierDefKey(newDossierDefKey);
        }

        //4. 写入数据
        result = beanCruder.insert(dossierListObject);
        result += beanCruder.insert(dossierListItemList);

        return result;
    }
}
