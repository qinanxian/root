package com.vekai.dataform.mapper.impl;

import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.mapper.DataFormMapper;
import com.vekai.dataform.model.DataFormFilter;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.XMLKit;
import cn.fisok.raw.lang.PairBond;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用XML文件存储DataForm实体
 * Created by tisir yangsong158@qq.com on 2017-05-22
 */
public class DataFormMapperXMLFileImpl implements DataFormMapper {
    private String basePath;
    /**版本冲突检测*/
    private boolean versionConflictDetection = true;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isVersionConflictDetection() {
        return versionConflictDetection;
    }

    public void setVersionConflictDetection(boolean versionConflictDetection) {
        this.versionConflictDetection = versionConflictDetection;
    }

    public void clearCacheAll() {
        
    }

    public void clearCacheItem(String formId) {

    }

    @Override
    public boolean exists(String id) {
        return false;
    }

    public DataForm getDataForm(String id) {
        return null;
    }

    public DataForm getDataForm(String pack, String formId) {
        File xmlFile = getStorageFile(pack,formId);
        if(!xmlFile.exists())return null;

        DataForm dataForm = null;
        try {
            dataForm = XMLKit.readFormFile(xmlFile,getAliasMap(),DataForm.class,getUseAttributes());
        } catch (IOException e) {
            throw new DataFormException("read error file:"+xmlFile.getAbsolutePath(),e);
        }
        dataForm.refreshElementMap();

        return dataForm;
    }

    public List<DataForm> getDataFormsByPack(String pack) {
        return null;
    }

    /**
     * 根据文件存储的包目录,模板ID,获取文件实际存储文件对象
     * @param pack
     * @param id
     * @return
     */
    protected File getStorageFile(String pack,String id){
        String fileName = id+".xml";
        String filePath = pack.replaceAll("\\.","/");
        if(!filePath.startsWith("/"))filePath = "/"+filePath;
        if(!filePath.endsWith("/"))filePath = filePath + "/";
        filePath += fileName;

        return new File(basePath+filePath);
    }

    protected Map<String,Class<?>> getAliasMap(){
        Map<String,Class<?>> aliasMap = MapKit.newHashMap();
        aliasMap.put("dataform",DataForm.class);
        aliasMap.put("element",DataFormElement.class);
        aliasMap.put("filter",DataFormFilter.class);
        aliasMap.put("validator",DataFormElement.FormElementValidator.class);
        return aliasMap;
    }

    protected List<PairBond<Class<?>, String>> getUseAttributes(){
        List<PairBond<Class<?>, String>> pairList = new ArrayList<PairBond<Class<?>, String>>();

        pairList.add(new PairBond(DataForm.class,"id"));
        pairList.add(new PairBond(DataForm.class,"pack"));
        pairList.add(new PairBond(DataForm.class,"code"));
        pairList.add(new PairBond(DataForm.class,"name"));
        pairList.add(new PairBond(DataForm.class,"sortCode"));
        pairList.add(new PairBond(DataForm.class,"tags"));
        pairList.add(new PairBond(DataForm.class,"classification"));
        pairList.add(new PairBond(DataForm.class,"dataModelType"));
        pairList.add(new PairBond(DataForm.class,"revision"));
        pairList.add(new PairBond(DataForm.class,"createdBy"));
        pairList.add(new PairBond(DataForm.class,"createdTime"));
        pairList.add(new PairBond(DataForm.class,"updatedBy"));
        pairList.add(new PairBond(DataForm.class,"updatedTime"));
        pairList.add(new PairBond(DataForm.DataFormUIHint.class,"columnNumber"));
        pairList.add(new PairBond(DataForm.DataFormUIHint.class,"formStyle"));

        pairList.add(new PairBond(DataFormElement.class,"code"));
        pairList.add(new PairBond(DataFormElement.class,"primaryKey"));
        pairList.add(new PairBond(DataFormElement.class,"primaryKeyGenerator"));
        pairList.add(new PairBond(DataFormElement.class,"sortCode"));
        pairList.add(new PairBond(DataFormElement.class,"name"));
        pairList.add(new PairBond(DataFormElement.class,"nameI18nCode"));
        pairList.add(new PairBond(DataFormElement.class,"column"));
        pairList.add(new PairBond(DataFormElement.class,"table"));
        pairList.add(new PairBond(DataFormElement.class,"updateable"));
        pairList.add(new PairBond(DataFormElement.class,"sortable"));
        pairList.add(new PairBond(DataFormElement.class,"persist"));
        pairList.add(new PairBond(DataFormElement.class,"dataType"));
        pairList.add(new PairBond(DataFormElement.class,"defaultValue"));
        pairList.add(new PairBond(DataFormElement.class,"enabled"));
        pairList.add(new PairBond(DataFormElement.class,"group"));
        pairList.add(new PairBond(DataFormElement.class,"groupI18nCode"));
        pairList.add(new PairBond(DataFormElement.class,"subGroup"));
        pairList.add(new PairBond(DataFormElement.class,"subGroupI18nCode"));
        pairList.add(new PairBond(DataFormElement.class,"limitedLength"));
        pairList.add(new PairBond(DataFormElement.class,"multiplier"));
        pairList.add(new PairBond(DataFormElement.class,"revision"));
        pairList.add(new PairBond(DataFormElement.class,"createdBy"));
        pairList.add(new PairBond(DataFormElement.class,"createdTime"));
        pairList.add(new PairBond(DataFormElement.class,"updatedBy"));
        pairList.add(new PairBond(DataFormElement.class,"updatedTime"));

        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"readonly"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"required"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"dataFormat"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"maskFormat"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"textAlign"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"editStyle"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"prefix"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"suffix"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"tips"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"tipsI18nCode"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"note"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"noteI18nCode"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"visible"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"rank"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"mediaQuery"));
        pairList.add(new PairBond(DataFormElement.FormElementUIHint.class,"colspan"));

        return pairList;
    }
    public List<DataForm> getAllDataForms() {
        return null;
    }

    public int save(DataForm dataForm) {
        Validate.notNull(dataForm);
        Validate.notNull(dataForm.getCode());

        String pack = StringKit.nvl(dataForm.getPack(),"");
        File xmlFile = getStorageFile(pack,dataForm.getCode());

        //目录不存在,则创建
        File parentFile = xmlFile.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        //版本冲突检测
        DataForm existsDataForm = getDataForm(pack, dataForm.getCode());
        if (existsDataForm != null) {
            if (versionConflictDetection) {
                Integer v1 = existsDataForm.getRevision();
                Integer v2 = dataForm.getRevision();
                if (v1 == null) v1 = 0;
                if (v2 == null) v2 = 0;
                if (v1 != v2) {
                    throw new DataFormException("exists version " + v1 + " and current version " + v2 + " conflict");
                }
            }else{
                //使用之前的数据
                dataForm.setRevision(existsDataForm.getRevision());
                dataForm.setCreatedTime(existsDataForm.getCreatedTime());
            }
        }
        if(dataForm.getRevision()==null){
            dataForm.setRevision(0);
        }
        dataForm.setRevision(dataForm.getRevision()+1);


        if(dataForm.getCreatedTime() == null){
            dataForm.setCreatedTime(DateKit.now());
        }
        dataForm.setUpdatedTime(DateKit.now());
        try {
            XMLKit.writeToFile(dataForm,xmlFile, getAliasMap(),getUseAttributes());
            return 1;
        } catch (IOException e) {
            throw new DataFormException("error saving file:"+xmlFile.getAbsolutePath(),e);
        }
    }

    public int saveDataFormElement(DataFormElement element) {
        throw new UnsupportedOperationException();
    }

    public int delete(String id) {
        return 0;
    }

    public int delete(String pack, String code) {
        return 0;
    }

    @Override
    public int deleteAll() {
        return 0;
    }
}
