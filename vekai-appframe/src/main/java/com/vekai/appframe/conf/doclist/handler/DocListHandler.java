package com.vekai.appframe.conf.doclist.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.conf.doclist.entity.ConfDocListItemPO;
import com.vekai.appframe.conf.doclist.entity.ConfDocListPO;
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
public class DocListHandler extends BeanDataListHandler<ConfDocListPO> {

    public Integer delete(DataForm dataForm, List<ConfDocListPO> dataList) {
        Integer result = 0;

        if(dataList == null) throw new BizException("删除数据出错，数据列表不存在");

        for (ConfDocListPO docListPO : dataList) {
            String doclistCode = docListPO.getDoclistCode();
            result += beanCruder.execute("DELETE FROM CONF_DOCLIST_ITEM WHERE DOCLIST_CODE=:doclistCode",MapKit.mapOf("doclistCode", doclistCode));
            result += beanCruder.execute("DELETE FROM CONF_DOCLIST WHERE DOCLIST_CODE=:doclistCode",MapKit.mapOf("doclistCode", doclistCode));
        }

        return result;
    }

    public PaginResult<ConfDocListPO> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
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
        String tplDoclistCode = object.getValue("tplDoclistCode").strValue();
        String newDoclistCode = object.getValue("newDoclistCode").strValue();
        if(StringKit.isBlank(tplDoclistCode) && StringKit.isBlank(newDoclistCode)){
            throw new BizException("复制失败，源模板号:{0}，目标模板号:{1}",tplDoclistCode,newDoclistCode);
        }
        //1.1 检查新的模板号是否存在
        List<ConfDocListPO> newDocListTmp = beanCruder.selectList(ConfDocListPO.class,
                "select * from CONF_DOCLIST WHERE DOCLIST_CODE=:docListCode",
                MapKit.mapOf("docListCode",newDoclistCode));
        if (newDocListTmp.size() > 0) throw new BizException("复制失败，目标模板号:{0}已存在",newDoclistCode);

        //2. 查询复制模板对象
        ConfDocListPO docListObject = beanCruder.selectOne(ConfDocListPO.class,
                "select * from CONF_DOCLIST WHERE DOCLIST_CODE=:docListCode",
                MapKit.mapOf("docListCode",tplDoclistCode));
        List<ConfDocListItemPO> docListItemList = beanCruder.selectList(ConfDocListItemPO.class, "SELECT * FROM" +
                " CONF_DOCLIST_ITEM WHERE DOCLIST_CODE=:doclistCode", MapKit.mapOf("doclistCode", tplDoclistCode));
        if(docListObject == null)  throw new BizException("复制失败，源模板号:{0}对应的数据不存在",tplDoclistCode);


        //3. 修改模板代码名称
        Integer result = 0;
        docListObject.setDoclistCode(newDoclistCode);
        for (ConfDocListItemPO docListItemPO: docListItemList) {
            docListItemPO.setDoclistCode(newDoclistCode);
        }

        //4. 写入数据
        result = beanCruder.insert(docListObject);
        result += beanCruder.insert(docListItemList);

        return result;
    }
}
