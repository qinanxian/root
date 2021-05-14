package com.vekai.appframe.base.aspect;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormCombiner;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.lang.PairBond;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataFormVirNameConverter {
    public static final String VIR_FIELD_SUFFIX = "VirName";
    public static final String CLOSE_NAME_AUTO_CONVERT = "closeNameAutoConvert";

    /**
     * 检查是否有需要转换的字段
     * @param dataForm
     * @param fields
     * @return
     */
    protected boolean hasConvertFields(DataForm dataForm, List<String> fields){
        boolean has = false;
        for(String field:fields){
            DataFormElement element = dataForm.getElement(field);
            if(element != null)return true;
        }
        return has;
    }

    /**
     * 把逗号的数组解析合并成为数组
     * @param strings
     * @return
     */
    protected List<String> parseMergeAsList(String... strings){
        Set<String> fieldSet = new HashSet<String>();
        for(String str:strings){
            String[] fieldsArray = str.split(",");
            for(String f:fieldsArray){
                fieldSet.add(f);
            }
        }
        //变为列表后返回
        List<String> fieldList = new ArrayList<String>();
        fieldList.addAll(fieldSet);
        return fieldList;
    }

    protected void patchVir4Meta(DataFormCombiner<?> dataFormCombiner,List<String> fields){
        DataForm dataForm = dataFormCombiner.getMeta();
        if(dataForm == null){
            return ;
        }

        //如果关闭了自动处理，则不需要处理了
        Object closeConvert = dataForm.getProperties().get(CLOSE_NAME_AUTO_CONVERT);
        if("Y".equals(closeConvert)){
            return;
        }
        if(!hasConvertFields(dataForm,fields))return;//如果没有需要转换的字段，就不要浪费时间了

        (new DataFormCombinerVirFieldExpansion(){
            public void fillBody(DataFormCombiner<?> dataFormCombiner,List<PairBond> codePairBonds) {
            }
        }).exec(fields,dataFormCombiner);
    }

    /**
     * 数据根据虚字段进行扩大
     */
    public static abstract class DataFormCombinerVirFieldExpansion{
        public void exec(List<String> fieldList,DataFormCombiner<?> dataFormCombiner){
            DataForm dataForm = dataFormCombiner.getMeta();

            List<PairBond> codePairBonds= ListKit.newArrayList();
            List<DataFormElement> elements = dataForm.getElements();
            for(String field:fieldList){
                DataFormElement element = dataForm.getElement(field);
                if(element == null)continue;
                int elementIndex = elements.indexOf(element);

                //如果实际字段不可见，则不需要虚字段
                if(!element.getElementUIHint().getVisible())continue;

                //1.复制一个字段，把原来的字段隐藏
//            BeanKit.copyProperties(element,newElement);
                DataFormElement newElement = new DataFormElement();
                DataFormElement.FormElementUIHint newUIHint = newElement.getElementUIHint();
                String virElementCode = element.getCode()+VIR_FIELD_SUFFIX;

                BeanKit.copyProperties(element.getElementUIHint(),newUIHint);
                newElement
                        .setDataformId(element.getDataformId())
                        .setName(element.getName())
                        .setGroup(element.getGroup())
                        .setGroupI18nCode(element.getGroupI18nCode())
                        .setSubGroup(element.getSubGroup())
                        .setSubGroupI18nCode(element.getSubGroupI18nCode())
                        .setSortCode(element.getSortCode())
                        .setCode(virElementCode)
                        .setColumn(null)
                        .setTable(null)
                        .setUpdateable(false)
                        .setPersist(false)
                ;
                newUIHint
                        .setEditStyle(ElementDataEditStyle.Text)
                        .setReadonly(true)
                        .setReading(true)
                        .setVisible(true)
                        .setHtmlStyle(element.getElementUIHint().getHtmlStyle())
                        ;

                element.getElementUIHint().setVisible(false);
                elements.add(elementIndex+1,newElement);

                codePairBonds.add(new PairBond(field, virElementCode));
            }
            //2.结数据列表添加一个名称字段，并且在数据中做好名称值转换
            fillBody(dataFormCombiner,codePairBonds);
        }

        public abstract void fillBody(DataFormCombiner<?> dataFormCombiner,List<PairBond> codePairBonds);
    }
}
