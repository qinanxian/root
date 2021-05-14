package com.vekai.workflow.nopub.bom;


import java.io.Serializable;
import java.util.*;

/**
 * 业务对象模型定义
 */
public class BomObject implements Serializable,Cloneable{
    private static final long serialVersionUID = -7232126599826479908L;
    private String name;
    private String label;
    private Map<String,BomAttribute> attributeMap = new LinkedHashMap<>();


    public BomObject() {
    }

    public BomObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public BomObject appendAttribute(String name){
        appendAttribute(new BomAttribute(name));
        return this;
    }
    public BomObject appendAttribute(BomAttribute attribute){
        attributeMap.put(attribute.getName(), attribute);
        attribute.setBomObject(this);

        return this;
    }

    public BomAttribute getAttribute(String attributeName){
        return attributeMap.get(attributeName);
    }
    public List<BomAttribute> getAttributes(){
        List<BomAttribute> list = new ArrayList<>();
        list.addAll(attributeMap.values());
        return list;
    }

    /**
     * 把整个对象转换为 a.b.c形式的map
     * @return
     */
    public Map<String,Object> flatValue(){
        Map<String,Object> map =new HashMap<>();
        List<BomAttribute> attributes = getAttributes();
        for(BomAttribute attr : attributes){
            map.put(attr.getFullName(), attr.getValue());
        }
        return map;
    }
}
