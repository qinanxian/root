package com.vekai.base.dict.service;

import cn.fisok.raw.kit.*;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.FisokException;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.base.autoconfigure.BaseProperties;
import com.vekai.base.dict.service.impl.po.DictItemPO;
import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.service.impl.DictServiceImpl;
import com.vekai.base.dict.service.impl.po.DictPO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Service
public class DictAdminService {
    protected static Logger logger = LoggerFactory.getLogger(DictAdminService.class);
    @Autowired
    BaseProperties baseProperties;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    DictServiceImpl dictService;

    public List<DictEntry> getAllDictList() {
        return dictService.getDictList();
//        List<DictEntry> entries = new ArrayList<>();
//        List<DictPO> dictPoList = beanCruder.selectList(DictPO.class,"select * from FOWK_DICT where 1=1 order by SORT_CODE ASC");
//        dictPoList.forEach(dictPO->{
//            DictEntry entry = new DictEntry();
//            BeanKit.copyProperties(dictPO, entry);
//            Map<String, DictItemEntry> itemMap = dictService.getDictItemMap(dictPO.getCode());
//            entry.setItemMap(itemMap);
//            entries.add(entry);
//        });
//
//        return entries;
    }

    public File getDictDataDirectory() {
        String devBaseDir = baseProperties.getDevBaseDir();
        String dataFormDataClasspath = baseProperties.getDictDataClasspath();
        File workPath = new File(this.getClass().getClassLoader().getResource("").getFile());
        String projectRoot = workPath.getParentFile().getParentFile().getParent();
        projectRoot = projectRoot.replaceAll("\\\\", "/");//对windows环境下的路径做一下替换
        ValidateKit.notBlank(devBaseDir, "开发模式下，请设置显示模板资源文件路径:devBaseDir");
        devBaseDir = devBaseDir.replaceAll("\\$\\{projectRoot\\}", projectRoot);

        String baseDirPath = FileKit.standardFilePath(devBaseDir);
        if (baseDirPath.endsWith("/")) baseDirPath = baseDirPath.substring(0, baseDirPath.length() - 1);

        File baseDir = new File(baseDirPath);
        String relativePath = dataFormDataClasspath.replaceAll("\\.", "/");
        if (!relativePath.startsWith("/")) relativePath = "/" + relativePath;
        return new File(baseDir + relativePath);
    }

    public void dbTransferFile(File dir) {
        List<DictEntry> entries = getAllDictList();
        entries.forEach(entry -> {
            File curDir = FileKit.getFile(dir, entry.getCategoryCode());
            if (!curDir.exists()) curDir.mkdirs();

            File jsonFile = FileKit.getFile(curDir, entry.getCode() + ".json");
            if (jsonFile.exists()) jsonFile.delete();
            try {
                FileKit.touchFile(jsonFile);
            } catch (IOException e) {
                throw new FisokException("创建json文件失败",e);
            }

            String jsonText = JSONKit.toJsonString(entry, true);
            try {
                FileKit.write(jsonFile, jsonText, Charset.defaultCharset(), false);
            } catch (IOException e) {
                throw new FisokException("写入json文件失败", e);
            }
        });

    }

    @Transactional
    public Integer fileTransferDB() {
        Integer result = 0;
        File dir = this.getDictDataDirectory();
        logger.info("码表目录:" + dir.getPath());
        List<DictEntry> dicts = this.getDictEntrys(dir);
        logger.info("码表JSON文件总数量:" + dicts.size());
        if (dicts.size() == 0) {
            throw new RuntimeException("码表JSON文件为空");
        }

        Map<String, DictEntry> fileDictMap = new HashMap();
        dicts.forEach((dict) -> {
            fileDictMap.put(dict.getCode(), dict);
        });
        List<DictEntry> insertDictList = ListKit.newArrayList();
        List<DictEntry> updateDictList = ListKit.newArrayList();
        List<DictEntry> deleteDictList = ListKit.newArrayList();
        List<DictEntry> dbDictList = this.getAllDictList();
        Iterator var9 = dbDictList.iterator();

        DictEntry entry;
        while (var9.hasNext()) {
            entry = (DictEntry) var9.next();
            DictEntry fileEntry = (DictEntry) fileDictMap.get(entry.getCode());
            fileDictMap.remove(entry.getCode());


            if (fileEntry == null) {
                deleteDictList.add(entry);
                continue;
            }


            /**
             * 清除namePingyin，否则每次都是更新全部
             */
            List<DictItemEntry> items = entry.getItems();
            items.forEach((item) -> {
                item.setNamePinyin(null);
            });
            String nameI18nCode = fileEntry.getNameI18nCode();
            if ("".equals(nameI18nCode)) fileEntry.setNameI18nCode(null);
            String intro = fileEntry.getIntro();
            if ("".equals(intro)) fileEntry.setIntro(null);


            String fileJsonText = JSONKit.toJsonString(fileEntry);
            String dbJsonText = JSONKit.toJsonString(entry);

//            if("IndustryTypLv4".equals(entry.getCode())){
//                /**
//                 * 设置SortCode
//                 */
//                List<DictItemEntry> tmpItems = entry.getItems();
//                for(DictItemEntry tmpItem:tmpItems){
//                    String tCode = tmpItem.getCode();
//                    tmpItem.setSortCode(tCode);
//                }
//                System.out.println(JSONKit.toJsonString(entry));
//                return null;
//            }

            if (!fileJsonText.equals(dbJsonText)) {
                updateDictList.add(fileEntry);
            }
        }

        insertDictList.addAll(fileDictMap.values());
        logger.info("码表新增数量:" + insertDictList.size());
        logger.info("码表更新数量:" + updateDictList.size());
        logger.info("码表删除数量:" + deleteDictList.size());
        DictPO dictPO;
        List dictItemPOList;
        if (!insertDictList.isEmpty()) {
            List<String> delDicts = insertDictList.stream().map(DictEntry::getCode).collect(Collectors.toList());
            this.beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)", MapKit.mapOf("dictCodes", delDicts));
            logger.info("码表新增时,删除码表数量:" + delDicts.size());
            var9 = insertDictList.iterator();

            while (var9.hasNext()) {
                entry = (DictEntry) var9.next();
                dictPO = this.dictEntryConvertDictPO(entry);
                dictItemPOList = this.getDictItemByDictEntry(entry, dictPO);
                result = result + this.beanCruder.insert(dictPO);
                this.beanCruder.insert(dictItemPOList);
            }
        }

        if (!updateDictList.isEmpty()) {
            List<String> delDicts = updateDictList.stream().map(DictEntry::getCode).collect(Collectors.toList());
            this.beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)", MapKit.mapOf("dictCodes", delDicts));
            logger.info("码表更新时,删除码表数量:" + delDicts.size());
            var9 = updateDictList.iterator();

            while (var9.hasNext()) {
                entry = (DictEntry) var9.next();
                dictPO = this.dictEntryConvertDictPO(entry);
                dictItemPOList = this.getDictItemByDictEntry(entry, dictPO);
                result = result + this.beanCruder.update(dictPO);
                this.beanCruder.insert(dictItemPOList);
            }
        }

        if (!deleteDictList.isEmpty()) {
            List<String> deleteDictCodeList = (List) deleteDictList.stream().map(DictEntry::getCode).collect(Collectors.toList());
            logger.info("码表删除时,删除码表数量:" + deleteDictCodeList.size());
            result = result + this.beanCruder.execute("delete from FOWK_DICT where CODE IN (:dictCodes)", MapKit.mapOf("dictCodes", deleteDictCodeList));
            this.beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)", MapKit.mapOf("dictCodes", deleteDictCodeList));
        }

        return result;
    }

//    @Transactional
//    public Integer fileTransferDB() {
//        Integer result = 0;
////        this.deleteExistedDic();
//        //查出从配置文件加载的数据字典
//        File dir = this.getDictDataDirectory();
//        List<DictEntry> dicts = this.getDictEntrys(dir);
//        final Map<String,DictEntry> fileDictMap = new HashMap<>();
//        dicts.forEach(dict->{
//            fileDictMap.put(dict.getCode(),dict);
//        });
//
//        //分别计算需要插入，更新，删除的三个列表
//        List<DictEntry> insertDictList = ListKit.newArrayList();
//        List<DictEntry> updateDictList = ListKit.newArrayList();
//        List<DictEntry> deleteDictList = ListKit.newArrayList();
//
////        List<DictEntry> dbDictList = beanCruder.selectList(DictEntry.class,"select * from FOWK_DICT ORDER BY SORT_CODE ASC,CODE ASC ");
//        List<DictEntry> dbDictList = getAllDictList();
//        //以DB查出来的为基础进行处理
//        for(DictEntry dbEntry : dbDictList){
//            DictEntry fileEntry = fileDictMap.get(dbEntry.getCode());
//            fileDictMap.remove(dbEntry.getCode()) ;//和DB交集之后就把文件中的移除了
//
////            if(dbEntry.getCode().equals("WorkflowTaskStatus")){
////                System.out.println();
////            }
//
//            String fileJsonText = JSONKit.toJsonString(fileEntry);
//            String dbJsonText = JSONKit.toJsonString(dbEntry);
//
//            if(fileEntry == null){  //数据库里有，但是文件里没有，则说明被删除了,则添加到删除列表中去
//                deleteDictList.add(dbEntry);
//            }else if(!fileJsonText.equals(dbJsonText)){//当文件计算出来的签名和数据库中不一至时，说明两边有有，应该做更新
//                updateDictList.add(fileEntry);
//            }
//        }
//        insertDictList.addAll(fileDictMap.values());
//
//
//        //需要插入的
//        if(!insertDictList.isEmpty()){
//            //把明细表中的无论有都删除了，后面保存明细时，只需要插入
//            beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)",
//                    MapKit.mapOf("dictCodes",insertDictList.stream().map(DictEntry::getCode).collect(Collectors.toList())));
//
//            for (DictEntry entry:insertDictList) {
//                DictPO dictPO = this.dictEntryConvertDictPO(entry);
//                List<DictItemPO> dictItemPOList = this.getDictItemByDictEntry(entry,dictPO);
//                result += beanCruder.insert(dictPO);
//                beanCruder.insert(dictItemPOList);
//            }
//        }
//        //需要更新的
//        if(!updateDictList.isEmpty()){
//            //把明细表中的无论有都删除了，后面保存明细时，只需要插入
//            beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)",
//                    MapKit.mapOf("dictCodes",updateDictList.stream().map(DictEntry::getCode).collect(Collectors.toList())));
//
//            for (DictEntry entry:updateDictList) {
//                DictPO dictPO = this.dictEntryConvertDictPO(entry);
//                List<DictItemPO> dictItemPOList = this.getDictItemByDictEntry(entry,dictPO);
//                result += beanCruder.update(dictPO);
//                beanCruder.insert(dictItemPOList);
//            }
//        }
//        //需要删除的
//        if(!deleteDictList.isEmpty()){
//            List<String> deleteDictCodeList = deleteDictList.stream().map(DictEntry::getCode).collect(Collectors.toList());
//            result += beanCruder.execute("delete from FOWK_DICT where CODE IN (:dictCodes)",MapKit.mapOf("dictCodes",deleteDictCodeList));
//            beanCruder.execute("delete from FOWK_DICT_ITEM where DICT_CODE IN (:dictCodes)",MapKit.mapOf("dictCodes",deleteDictCodeList));
//        }
//        return result;
//    }

    private List<DictItemPO> getDictItemByDictEntry(DictEntry dictEntry, DictPO dictPO) {
        List<DictItemEntry> dictItemEntries = dictEntry.getDictItemEntrys();
        List<DictItemPO> dictItemPOList = dictItemEntries.stream().map(itemEntry -> this.dictItemEntryConvertDictItemPO(itemEntry, dictPO.getCode())).collect(Collectors.toList());
        return dictItemPOList;
    }

    private DictItemPO dictItemEntryConvertDictItemPO(DictItemEntry dictItemEntry, String code) {
        DictItemPO dictItemPO = new DictItemPO();
        BeanKit.copyProperties(dictItemEntry, dictItemPO);
        dictItemPO.setDictCode(code);
        return dictItemPO;
    }

    private DictPO dictEntryConvertDictPO(DictEntry dictEntry) {
        DictPO dictPO = new DictPO();
        BeanKit.copyProperties(dictEntry, dictPO);
        return dictPO;
    }

    private void deleteExistedDic() {
        String dictSql = "DELETE FROM FOWK_DICT";
        beanCruder.execute(dictSql);
        String dictItemSql = "DELETE FROM FOWK_DICT_ITEM";
        beanCruder.execute(dictItemSql);
    }

    public List<DictEntry> getDictEntrys(File dir) {
        List<DictEntry> dicts = new ArrayList();
        if (!dir.exists()) {
            try {
                String dictPath = this.baseProperties.getDictDataClasspath();

                String jarBasePath = this.getClass().getProtectionDomain()
                        .getCodeSource().getLocation().getFile();

                jarBasePath = java.net.URLDecoder.decode(jarBasePath, "UTF-8");
                String jarPath = jarBasePath.split("!")[0]
                        .replace("file:", "");


                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> entrys = jarFile.entries();
                while (entrys.hasMoreElements()) {
                    JarEntry jar = entrys.nextElement();
                    String name = jar.getName();
                    String jarStart = "BOOT-INF/classes/" + dictPath;
                    if (!name.startsWith(jarStart) || !name.endsWith(".json") || name.endsWith(".builder.json") || name.endsWith("category.json"))
                        continue;

                    System.out.println(name);

                    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name.replace("BOOT-INF/classes/", ""));
                    File tmpFile = File.createTempFile("file", ".dict.temp");
                    FileUtils.copyInputStreamToFile(inputStream, tmpFile);

                    DictEntry dictEntry = this.getDictFromFile(tmpFile);
                    dicts.add(dictEntry);
                    tmpFile.delete();
                }


                jarFile.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            File[] dirs = dir.listFiles();

            for (int i = 0; i < dirs.length; ++i) {
                if (dirs[i].isDirectory()) {
                    File[] files = dirs[i].listFiles();

                    for (int fileIndex = 0; fileIndex < files.length; ++fileIndex) {
                        if (!"index.builder.json".equals(files[fileIndex].getName())) {
                            DictEntry dictEntry = this.getDictFromFile(files[fileIndex]);
                            dicts.add(dictEntry);
                        }
                    }
                }
            }
        }
        return dicts;
    }

//    public List<DictEntry> getDictEntrys(File dir) {
//        List<DictEntry> dicts = new ArrayList<>();
//        if (!dir.exists())
//            return dicts;
//        File[] dirs = dir.listFiles();
//        for (int i = 0; i < dirs.length; i++) {
//            if (!dirs[i].isDirectory())
//                continue;
//            File[] files = dirs[i].listFiles();
//            for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
//                if ("index.builder.json".equals(files[fileIndex].getName()))
//                    continue;
//                DictEntry dictEntry = this.getDictFromFile(files[fileIndex]);
//                dicts.add(dictEntry);
//            }
//        }
//        return dicts;
//    }

    public DictEntry getDictFromFile(File file) {
        DictEntry dict = null;
        if (!file.exists())
            return dict;
        try {
            String content = FileKit.readFileToString(file, Charset.defaultCharset());
            dict = JSONKit.jsonToBean(content, DictEntry.class);
        } catch (IOException e) {
            throw new BizException(e.getMessage());
        }
        return dict;
    }

    public void clearDictCache() {
        dictService.clearCacheAll();
    }
}
