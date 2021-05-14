package com.vekai.dataform.service;

import cn.fisok.raw.kit.*;
import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.base.dict.service.DictService;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataDictCodeMode;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DictExprResolve {
    @Autowired
    protected DictService dictService;
    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    protected MapObjectCruder mapObjectCruder;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public DictService getDictService() {
        return dictService;
    }

    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 这个API只取值对应的代码项
     *
     * @param element element
     * @param valueList valueList
     * @return list
     */
    public List<DictItemNode> getDictItemsByValue(DataFormElement element,List<String> valueList){
        List<DictItemNode> itemEntries = null;
        if(element.getElementUIHint().getDictCodeMode() == ElementDataDictCodeMode.DictCode){
            itemEntries = ListKit.newArrayList();
            //1.找到形结构图
            List<DictItemNode> nodeList = dictService.getDictTree(element.getElementUIHint().getDictCodeExpr());

            //1.把找到的节点放到独立的对象中去
            for(String v : valueList){
                DictItemNode findNode = lookupNode(nodeList,v);
                if(findNode==null) continue;
                itemEntries.add(findNode);
            }

            //2.重新排序
            sort(itemEntries);

            //3.清除树上没有被引用的节点
            itemEntries = findRootAndClean(itemEntries);
        }

        return itemEntries;

    }

    private void sort(List<DictItemNode> nodes){
        nodes.sort(new Comparator<DictItemNode>() {
            public int compare(DictItemNode o1, DictItemNode o2) {
                String s1 = StringKit.nvl(o1.getSortCode(),"");
                String s2 = StringKit.nvl(o2.getSortCode(),"");
                return s1.compareTo(s2);
            }
        });
    }

    private void increment(Map<DictItemNode,Integer> referCountMap,DictItemNode node){
        Integer count = referCountMap.get(node);
        if(count==null)count = 0;
        count ++;
        referCountMap.put(node,count);
    }

    private List<DictItemNode> findRootAndClean(List<DictItemNode> nodeList){
        Set<DictItemNode> rootNodeSet = new HashSet<DictItemNode>();
        //1.进行引用计数计算,并且取到他的分枝的根节点
        Map<DictItemNode,Integer> referCountMap = new HashMap<DictItemNode,Integer>();
        for(DictItemNode node:nodeList){
            DictItemNode ref = node;
            DictItemNode branchRoot = ref;
            while(ref != null){
                increment(referCountMap,ref);
                ref = ref.getParent();
                if(ref!=null)branchRoot = ref;
            }
            rootNodeSet.add(branchRoot);
        }
        //2.转移到列表中，并且重新排序
        List<DictItemNode> rootNodeList = new ArrayList<DictItemNode>();
        rootNodeList.addAll(rootNodeSet);
        sort(rootNodeList);
        //3.清除没有引用计数的节点
        cleanNoRefNodes(rootNodeList,referCountMap);

        return rootNodeList;
    }

    private void cleanNoRefNodes(List<DictItemNode> nodeList,Map<DictItemNode,Integer> referCountMap){
        if(nodeList==null||nodeList.size()==0)return;
        for(int i=0;i<nodeList.size();i++){
            DictItemNode node = nodeList.get(i);
            if(!referCountMap.containsKey(node)){
                nodeList.remove(i);
                i--;
            }
            cleanNoRefNodes(node.getChildren(),referCountMap);
        }
    }

    public List<DictItemNode> getDictItemByValue(DataFormElement element,String value){
        return getDictItemsByValue(element,ListKit.listOf(value));
    }


    protected DictItemNode lookupNode(List<DictItemNode> nodeList,String findCode){
        if(nodeList==null)return null;
        for(DictItemNode node : nodeList){
            if(findCode.equals(node.getCode()))return node;
            DictItemNode finded = lookupNode(node.getChildren(),findCode);
            if(finded != null)return finded;
        }
        return null;
    }

    private List<DictItemNode> toDictItemNodes(List<DictItemEntry> items){
        if (null == items || items.isEmpty()) return Collections.emptyList();
        List<DictItemNode> dictItems = new ArrayList<>(items.size());
        for(DictItemEntry item : items){
            DictItemNode node = new DictItemNode();
            node.setValues(item);
            dictItems.add(node);
        }
        return dictItems;
    }

    public List<DictItemNode> getDictItems(DataFormElement element,Map<String, Object> params){
        List<DictItemNode> dictItems = null;
        //代码表直接处理
        if(ElementDataDictCodeMode.DictCode==element.getElementUIHint().getDictCodeMode()){
            DictEntry dictEntry = dictService.getDict(element.getElementUIHint().getDictCodeExpr());
            if(dictEntry == null)return dictItems;
            List<DictItemEntry> items = dictEntry.getItems();
            dictItems = toDictItemNodes(items);
        }else if(ElementDataDictCodeMode.SQLQuery==element.getElementUIHint().getDictCodeMode()){
            String sql = element.getElementUIHint().getDictCodeExpr();
            if(StringKit.isBlank(sql))return dictItems;

            if(params==null)params = MapKit.newEmptyMap();

            List<MapObject> dataList = mapObjectCruder.selectList(sql,params);
            if(dataList==null)return dictItems;

            List<DictItemEntry> items = new ArrayList<DictItemEntry>();
            for(MapObject row : dataList){
                DictItemEntry entry = new DictItemEntry();
                entry.setCode(row.getValue("code").strValue());
                entry.setName(row.getValue("name").strValue());
                entry.setSortCode(row.getValue("sortCode").strValue());
                entry.setHotspot(row.getValue("hotspot").intValue(0));
                entry.setSummary(row.getValue("summary").strValue());
                items.add(entry);
            }
            dictItems = toDictItemNodes(items);
        }else if(ElementDataDictCodeMode.JSON==element.getElementUIHint().getDictCodeMode()){
            String jsonText = element.getElementUIHint().getDictCodeExpr();
            try{
                dictItems = JSONKit.jsonToBeanList(jsonText,DictItemNode.class);
            }catch(Exception e){
                logger.error("JSON解析为数据字典对象出错。JSON["+jsonText+"]",e);
            }
        }

        ElementDataEditStyle editStyle = element.getElementUIHint().getEditStyle();
        if(editStyle == ElementDataEditStyle.TreeSelect
                ||editStyle == ElementDataEditStyle.Cascader){
            dictItems = TreeNodeKit.buildTree(dictItems, DictItemNode::getSortCode);
        }

        return dictItems;
    }
}
