package com.vekai.dataform.mapper.impl;

import cn.fisok.raw.autoconfigure.RawProperties;
import cn.fisok.raw.kit.LogKit;
import com.vekai.dataform.autoconfigure.DataFormProperties;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.service.DataFormPublicService;
import com.vekai.dataform.DataFormConsts;
import com.vekai.dataform.mapper.DataFormMapper;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by luyu on 2018/6/1.
 */
public class DataFormMapperJSONFileImpl implements DataFormMapper {

    @Autowired
    private RawProperties rawProperties;
    @Autowired
    private DataFormPublicService dataFormPublicService;
    @Autowired
    DataFormProperties dataFormProperties;
    @Lazy
    @Autowired
    DataFormMapperJSONFileImpl self;
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Override
    public void clearCacheAll() {

    }

    @Override
    public void clearCacheItem(String formId) {

    }

    @Override
    public boolean exists(String id) {
        File file = dataFormPublicService.getFileByDataFormId(id);
        if (file.exists())
            return Boolean.TRUE;
        return Boolean.FALSE;
    }


    public DataForm getDataForm(String id) {
        if(rawProperties.getRunModel() == RawProperties.RunModel.PROD){
            return self.getDataFormMybeCache(id);  //通过代理调用，可能会使用缓存
        }else{
            return this.getDataFormMybeCache(id);  //直接调用，一定不会使用缓存
        }
    }

    /**
     * 获取显示模板，可能会取缓存
     *
     * @param id id
     * @return DataForm
     */
    @Cacheable(value= DataFormConsts.CACHE_KEY,key="#id")
    public DataForm getDataFormMybeCache(String id){
        DataForm dataForm = null;
        File file = dataFormPublicService.getFileByDataFormId(id);
        if(file.exists()){
            dataForm = dataFormPublicService.getDataFormFromFile(file);
        }

        if(dataForm == null){
            String urlStr = MessageFormat.format("classpath:{0}/{1}.json"
                    ,dataFormProperties.getDataformDataClasspath()
                    ,id.replace("-","/"));
            URL url = null;
            try {
                url = ResourceUtils.getURL(urlStr);
            } catch (FileNotFoundException e) {
                LogKit.error("资源{0}未找到",urlStr);
                throw new DataFormException("",e);
            }
            dataForm = dataFormPublicService.getDataFormFrom(url);
        }

        return dataForm;
    }

    @Override
    public DataForm getDataForm(String pack, String code) {
        String dataFormId = StringKit.format("{0}-{1}", pack, code);
        return self.getDataForm(dataFormId);
    }

    public boolean isIgnoreResource(String str){
        List<String> ignoreList = dataFormProperties.getDataformIgnoreResources();
        if(ignoreList == null || ignoreList.isEmpty())return false;

        str = str.replace("\\", "/");

        for(String igItem : ignoreList){
            if(str.endsWith(igItem))return true;
        }
        return false;
    }

    @Override
    public List<DataForm> getDataFormsByPack(String pack) {
        try {
            pack = null == pack ? "" : pack;
            String directory = dataFormPublicService.getDataformDataDirectory().getAbsolutePath();
            Resource[] resources;
            if (resourcePatternResolver.getResource("file:" + directory).exists()) {
                resources = resourcePatternResolver.getResources(
                        "file:" + Paths.get(directory, pack).toString() + "/**/*.json");
            } else {
                String path = dataFormProperties.getDataformDataClasspath().replace(".", "/");
                path = path.endsWith("/") ? path : path + "/";
                resources = resourcePatternResolver.getResources(path + "**/*.json");
            }
            if (null == resources || resources.length == 0) return Collections.emptyList();
            List<DataForm> result = new ArrayList<>(resources.length);
            for (Resource jsonResource : resources) {
                if(jsonResource == null||jsonResource.getURI() == null)continue;
                String resourcePath = jsonResource.getURL().getFile();
                if(StringKit.isBlank(resourcePath)) continue;
                if(isIgnoreResource(resourcePath)) continue;

                String dataformId = getDataformId(jsonResource.getURL().getFile());
                DataForm dataForm = this.getDataForm(dataformId);

                if(dataForm == null || StringKit.isBlank(dataForm.getId()))continue;

                result.add(dataForm);
            }
            return result;
        } catch (IOException ex) {
            throw new RuntimeException("error when get dataform from json", ex);
        }
    }

    private String getDataformId(String jsonResourcePath) {
        jsonResourcePath = jsonResourcePath.replace("\\", "/");
        String[] pathSeg = jsonResourcePath.split("/");
        int len = pathSeg.length;
        return pathSeg[len - 2] + "-" + pathSeg[len - 1].replace(".json", "");
    }


    @Override
    public List<DataForm> getAllDataForms() {
        return getDataFormsByPack(null);
    }

    @Override
    public int save(DataForm dataForm) {
        self.clearCacheItem(dataForm.getId());
        String dataFormId = StringKit.format("{0}-{1}", dataForm.getPack(), dataForm.getCode());
        File jsonFile = FileKit.getFile(dataFormPublicService.getDataformDataDirectory().getAbsolutePath(),dataForm.getPack(),dataForm.getCode() + ".json");
        dataForm.getElements().stream().forEach(dataFormElement -> {
            dataFormElement.setDataformId(dataFormId);
        });
        return dataFormPublicService.saveDataForm(dataForm,jsonFile);
    }

    @Override
    public int saveDataFormElement(DataFormElement element) {
        String dataFmId = element.getDataformId();
        self.clearCacheItem(dataFmId);
        File file = dataFormPublicService.getFileByDataFormId(dataFmId);

        DataForm dataForm = self.getDataForm(dataFmId);
        List<DataFormElement> dataFormElements = dataForm.getElements();
        dataFormElements = dataFormElements.stream().filter(dataFormElement ->  !dataFormElement.getCode().equals(element.getCode())).collect(Collectors.toList());
        dataFormElements.add(element);
        dataForm.setElements(dataFormElements);
        return dataFormPublicService.saveDataForm(dataForm,file);
    }

    @Override
    public int delete(String id) {
        Integer result = 1;
        File file = dataFormPublicService.getFileByDataFormId(id);
        if (!file.exists())
            return result;
        Boolean isDeleted = file.delete();
        if (isDeleted)
            return result;
        return 0;
    }

    @Override
    public int delete(String pack, String code) {
        String dataFormId = StringKit.format("{0}-{1}", pack, code);
        return delete(dataFormId);
    }

    @Override
    public int deleteAll() {
        File directory = dataFormPublicService.getDataformDataDirectory().getAbsoluteFile();
        File[] dirs = directory.listFiles();
        if (dirs == null)
            return 1;
        for (int i = 0; i < dirs.length; i++) {
            dirs[i].delete();
        }
        return 1;
    }



}
