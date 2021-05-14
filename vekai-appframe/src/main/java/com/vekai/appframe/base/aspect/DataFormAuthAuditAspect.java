package com.vekai.appframe.base.aspect;


import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import com.vekai.auth.audit.aspect.DataAuditHelper;
import com.vekai.auth.holder.AuditHolder;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.service.DictExprResolve;
import cn.fisok.web.holder.WebHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 作DataForm增，删，改操作时，审计数据还原支持
 */
@Aspect
@Component
public class DataFormAuthAuditAspect {
    @Autowired
    protected DictExprResolve dictExprResolve;

    @Before("   (execution(* com.vekai.dataform.handler.DataListHandler+.insert(..)))" +
            "|| (execution(* com.vekai.dataform.handler.DataListHandler+.update(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataListHandler+.save(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataListHandler+.delete(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataOneHandler+.delete(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataOneHandler+.update(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataOneHandler+.save(..))) " +
            "|| (execution(* com.vekai.dataform.handler.DataOneHandler+.delete(..))) "
            )
    public void doAfterModify(JoinPoint joinPoint) {
        if (!DataAuditHelper.auditEnable()) return;   //只有启用了审计，才开启这项功能
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 1) return;
        DataForm dataForm = (DataForm) args[0];
        if (dataForm == null) return;

        //记录位置以及功能点介绍
        AuditHolder.setOriginLocation(dataForm.getId());
        String actionSummary = AuditHolder.getActionSummary();
        if (StringKit.isBlank(actionSummary)) {
            AuditHolder.setActionSummary(dataForm.getName());
        }

        //把字典，显示模板名称，模板主键整理出来
        Map<String, Map<String, String>> dictMap = MapKit.newHashMap();
        Map<String, String> nameMap = MapKit.newHashMap();
        List<String> primaryKeyCodeList = ListKit.newArrayList();

        List<DataFormElement> elements = dataForm.getElements();
        elements.forEach(element -> {
            nameMap.put(element.getCode(),element.getName());
            nameMap.put(element.getColumn(),element.getName());
            if(element.getPrimaryKey()){
                primaryKeyCodeList.add(element.getCode());
            }

            DataFormElement.FormElementUIHint uiHint = element.getElementUIHint();
            if (uiHint.getDictCodeMode() == null) return;
            boolean isDictMode = false;
            switch (uiHint.getEditStyle()) {
                case Select:
                case TreeSelect:
                case Cascader:
                case CheckBox:
                case RadioBox:
                case DictCodePicker:
                    isDictMode = true;
                    break;
            }
            if(!isDictMode)return;

            HttpServletRequest request = WebHolder.getRequest();
            Object reqDataObject = request.getAttribute("DATA_OBJECT");
            Map<String,Object> dictParam = MapKit.newHashMap();
            if(reqDataObject != null && reqDataObject instanceof MapObject){
                dictParam = (MapObject)reqDataObject;
            }

            List<DictItemNode> dictItems = dictExprResolve.getDictItems(element, dictParam);
            Map<String,String> dictItemMap = MapKit.newHashMap();
            fillDictItemToMap(dictItems,dictItemMap);
            if(dictItemMap.size()>0){
                dictMap.put(element.getCode(),dictItemMap);
                dictMap.put(element.getColumn(),dictItemMap);
            }
        });

        //把代码表添加到线程绑定上去
        Map<String,Object> auditProperties = AuditHolder.getProperies();
        if(auditProperties == null) auditProperties = MapKit.newHashMap();

        auditProperties.put("dictMap",dictMap);     //传递给Audit层的aspect使用。不能传入对象，否则会导致auth模块对dataform模块的依赖
        auditProperties.put("nameMap",nameMap);
        auditProperties.put("primaryKeyCodeList",primaryKeyCodeList);

        AuditHolder.setProperies(auditProperties);
    }

    private void fillDictItemToMap(List<DictItemNode> dictItems,Map<String, String> dictItemMap){
        for(int i=0;i<dictItems.size();i++){
            DictItemNode node = dictItems.get(i);
            dictItemMap.put(node.getCode(),node.getFullName("/"));
            List<DictItemNode> children = node.getChildren();
            if(children != null && children.size()>0){
                fillDictItemToMap(children,dictItemMap);
            }
        }
    }
}
