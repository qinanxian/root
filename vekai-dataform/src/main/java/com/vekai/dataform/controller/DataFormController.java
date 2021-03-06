package com.vekai.dataform.controller;

import cn.fisok.raw.kit.*;
import cn.fisok.web.holder.WebHolder;
import cn.fisok.web.kit.HttpKit;
import com.vekai.base.dict.model.DictItemNode;
import com.vekai.dataform.validator.ValidateResult;
import com.vekai.dataform.DataFormConsts;
import com.vekai.dataform.context.ContextEnvFetcher;
import com.vekai.dataform.exception.DataFormException;
import com.vekai.dataform.handler.DataListHandler;
import com.vekai.dataform.handler.DataOneHandler;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormCombiner;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import com.vekai.dataform.model.types.ElementDataType;
import com.vekai.dataform.model.types.FormDataModelType;
import com.vekai.dataform.model.types.FormStyle;
import com.vekai.dataform.service.DataFormService;
import com.vekai.dataform.service.DictExprResolve;
import com.vekai.dataform.util.DataFormExporter;
import com.vekai.dataform.util.DataFormUtils;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ContentType;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.PaginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by tisir yangsong158@qq.com on 2017-05-30
 */
@RestController
@RequestMapping("/dataform")
public class DataFormController {
    public static class DataFormAddon {
        DataForm dataForm;
        DataListHandler<?> dataListHandler;
        DataOneHandler<?> dataOneHandler;
    }

    @Autowired
    protected DataFormService dataFormService;
    @Autowired
    protected DictExprResolve dictExprResolve;

    @Autowired
    @Qualifier(DataFormConsts.MAP_DATA_LIST_HANDLER_DEFAULT)
    protected MapDataListHandler mapDataListHandler;
    @Autowired
    @Qualifier(DataFormConsts.MAP_DATA_ONE_HANDLER_DEFAULT)
    protected MapDataOneHandler mapDataOneHandler;
    @Autowired
    @Qualifier(DataFormConsts.BEAN_DATA_LIST_HANDLER_DEFAULT)
    protected BeanDataListHandler<Object> beanDataListHandler;
    @Autowired
    @Qualifier(DataFormConsts.BEAN_DATA_ONE_HANDLER_DEFAULT)
    protected BeanDataOneHandler<Object> beanDataOneHandler;

    @Autowired
    protected DataFormController self;

    @PostConstruct
    public void init() {
//        mapDataListHandler = ApplicationContextHolder.getBean(DataFormConsts.MAP_DATA_LIST_HANDLER_DEFAULT,MapDataListHandler.class);
//        mapDataOneHandler = ApplicationContextHolder.getBean(DataFormConsts.MAP_DATA_ONE_HANDLER_DEFAULT,MapDataOneHandler.class);
//        beanDataListHandler = ApplicationContextHolder.getBean(DataFormConsts.BEAN_DATA_LIST_HANDLER_DEFAULT,BeanDataListHandler.class);
//        beanDataOneHandler = ApplicationContextHolder.getBean(DataFormConsts.BEAN_DATA_ONE_HANDLER_DEFAULT,BeanDataOneHandler.class);
    }

    public DataFormService getDataFormService() {
        return dataFormService;
    }

    public void setDataFormService(DataFormService dataFormService) {
        this.dataFormService = dataFormService;
    }

    public DictExprResolve getDictExprResolve() {
        return dictExprResolve;
    }

    public void setDictExprResolve(DictExprResolve dictExprResolve) {
        this.dictExprResolve = dictExprResolve;
    }

    @GetMapping("/ping")
    public String ping() {
        return "DataForm Is Running!";
    }

    /**
     * ??????DataForm??????????????????
     *
     * @param form DataForm????????????
     * @return DataFormCombiner
     */
    @GetMapping("/meta/{form}")
    public DataFormCombiner<Object> getDataForm(@PathVariable("form") String form) {
        return getDataFormWithParam(form, new LinkedMultiValueMap<String, Object>());
    }

    @GetMapping("/meta/{form}/{param}")
    public DataFormCombiner<Object> getDataFormWithParam(@PathVariable("form") String form, @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix) {
        DataFormAddon addon = getDataFormAddon(form);

        DataForm dataForm = addon.dataForm;

        Map<String, Object> paramMap = MapKit.flatMultiValueMap(paramMatrix);
        fillContextEnvToQueryParam(paramMap);

        DataFormCombiner<Object> ret = new DataFormCombiner<Object>();
        fillDataFormCombiner(ret, addon.dataForm, paramMap);
        ret.setBody(null);

        return ret;
    }

    private FormStyle getFormStyle(DataForm dataForm) {
        FormStyle formStyle = dataForm.getFormUIHint().getFormStyle();
//        FormStyle formStyle = getFormStyle(dataForm);
        ValidateKit.notNull(formStyle, "????????????{0}???????????????????????????");
        return formStyle;
    }

    private boolean isListStyle(DataForm dataForm) {
        FormStyle formStyle = getFormStyle(dataForm);

        switch (formStyle) {
            case DataTable:
            case ListCard:
            case TreeTable:
                return true;
        }
        return false;
    }

    private void validateHandler(DataForm dataForm, Object handler) {
        String format = "handler???????????????formId={0},formStyle={1},dataModelType={2},????????????{3}?????????";
        String id = dataForm.getId();
        FormStyle formStyle = getFormStyle(dataForm);
        FormDataModelType dataModelType = dataForm.getDataModelType();

        if (isListStyle(dataForm)) {
            if (dataModelType == FormDataModelType.DataMap) {
                if (!(handler instanceof MapDataListHandler)) {
                    throw new DataFormException(format, id, formStyle, dataModelType, MapDataListHandler.class.getName());
                }
            } else if (dataModelType == FormDataModelType.JavaBean) {
                if (!(handler instanceof BeanDataListHandler)) {
                    throw new DataFormException(format, id, formStyle, dataModelType, BeanDataListHandler.class.getName());
                }
            }
        } else {
            //???????????????
            if (dataModelType == FormDataModelType.DataMap) {
                if (!(handler instanceof MapDataOneHandler)) {
                    throw new DataFormException(format, id, formStyle, dataModelType, MapDataOneHandler.class.getName());
                }
            } else if (dataModelType == FormDataModelType.JavaBean) {
                if (!(handler instanceof BeanDataOneHandler)) {
                    throw new DataFormException(format, id, formStyle, dataModelType, BeanDataOneHandler.class.getName());
                }
            }
        }

    }


    protected DataFormAddon getDataFormAddon(String form) {
        DataFormAddon addon = new DataFormAddon();

        DataForm dataForm = dataFormService.getDataForm(form);
        ValidateKit.notNull(dataForm, "DataForm?????????,form=" + form);

        dataForm = BeanKit.deepClone(dataForm); //???????????????
        //????????????????????????
        dataFormService.touchMustTouchFields(dataForm);
        //???handler??????????????????????????????
        Object handler = null;
        String handlerName = dataForm.getHandler();
        if (StringKit.isNotBlank(handlerName)) {
            handler = ApplicationContextHolder.getBeanByClassName(handlerName);
            validateHandler(dataForm, handler);
        }

        addon.dataForm = dataForm;
        if (isListStyle(dataForm)) {
            //???????????????
            if (dataForm.getDataModelType() == FormDataModelType.DataMap) {
                addon.dataListHandler = mapDataListHandler;
            } else if (dataForm.getDataModelType() == FormDataModelType.JavaBean) {
                addon.dataListHandler = beanDataListHandler;
            }
            //???????????????Handler?????????????????????Handler
            if (handler != null) {
                addon.dataListHandler = (DataListHandler) handler;
            }
        } else {
            //???????????????
            if (dataForm.getDataModelType() == FormDataModelType.DataMap) {
                addon.dataOneHandler = mapDataOneHandler;
            } else if (dataForm.getDataModelType() == FormDataModelType.JavaBean) {
                addon.dataOneHandler = beanDataOneHandler;
            }
            //???????????????Handler?????????????????????Handler
            if (handler != null) {
                addon.dataOneHandler = (DataOneHandler) handler;
            }
        }

        //???????????????????????????????????????????????????
        if (isListStyle(dataForm)) {
            addon.dataListHandler.initDataForm(dataForm);
        } else {
            addon.dataOneHandler.initDataForm(dataForm);
        }

        return addon;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param dataForm dataForm
     * @return DataForm
     */
    private DataForm desensitDataForm(DataForm dataForm) {
        DataForm ret = dataForm;
//        DataForm ret = BeanKit.deepClone(dataForm);

        String holder = "******";
        ret.setHandler(holder);
        ret.setQuery(null);
        List<DataFormElement> elements = ret.getElements();
        for (DataFormElement element : elements) {
            element.setColumn(holder);
            element.setTable(holder);
        }

        return ret;
    }


    private void fillDataFormCombiner(DataFormCombiner combiner, DataForm dataForm, Map<String, Object> params) {
        combiner.setMeta(desensitDataForm(dataForm));
        Map<String, List<DictItemNode>> dictItemMap = new LinkedHashMap<String, List<DictItemNode>>();
        //???????????????
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            //?????????????????????????????????????????????
            if (element.getElementUIHint().getDictCodeMode() != null) {
                List<DictItemNode> dictItems = null;
                if (element.getElementUIHint().getDictCodeLazy() == false) {
                    dictItems = dictExprResolve.getDictItems(element, params);
                    if (dictItems == null) continue;
                }
                dictItemMap.put(element.getCode(), dictItems);
            }
        }
        combiner.setDict(dictItemMap);
    }

    private void fillDictItemByValue(DataFormCombiner combiner, DataForm dataForm, Object object) {
        fillDictItemByValues(combiner, dataForm, ListKit.listOf(object));
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param combiner combiner
     * @param dataForm dataForm
     * @param objects  objects
     */
    private void fillDictItemByValues(DataFormCombiner combiner, DataForm dataForm, List<?> objects) {
        Map<String, List<DictItemNode>> dictItemMap = combiner.getDict();
        //???????????????
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            List<DictItemNode> dictItems = null;
            //?????????????????????????????????????????????
            if (element.getElementUIHint().getDictCodeLazy()) {
                List<String> fieldValueList = new ArrayList<String>();
                String propName = element.getCode();
                for (Object object : objects) {
                    if (null == object) continue;
                    Object v = BeanKit.getPropertyValue(object, propName);
                    if (v == null) continue;
                    fieldValueList.add(String.valueOf(v));
                }

                dictItems = dictExprResolve.getDictItemsByValue(element, fieldValueList);
                if (dictItems == null) continue;
            }
            if (dictItems != null) {
                dictItemMap.put(element.getCode(), dictItems);
            }
        }
    }

    /**
     * ???????????????????????????????????????MAP???????????????
     *
     * @param map      map
     * @param nodeList nodeList
     */
    private void flatMap(Map<String, DictItemNode> map, List<DictItemNode> nodeList) {
        if (nodeList == null || nodeList.isEmpty()) return;
        for (int i = 0; i < nodeList.size(); i++) {
            DictItemNode node = nodeList.get(i);
            map.put(node.getCode(), node);
            flatMap(map, node.getChildren());
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param combiner combiner
     * @param dataForm dataForm
     * @param objects  objects
     */
    private void fillDictNameByObjects(DataFormCombiner combiner, DataForm dataForm, List<?> objects) {
        Map<String, List<DictItemNode>> dictItemMap = combiner.getDict();
        Iterator<String> iterator = dictItemMap.keySet().iterator();

        while (iterator.hasNext()) {
            String fieldCode = iterator.next();
            //??????????????????MAP
            List<DictItemNode> dictItems = dictItemMap.get(fieldCode);
            Map<String, DictItemNode> dictMap = new HashMap<String, DictItemNode>();
            flatMap(dictMap, dictItems);

            //???????????????????????????????????????
            DataFormElement element = dataForm.getElement(fieldCode);
            boolean isMultiValue = false;
            boolean isTreeFullPath = false;
            if (element != null) {
                isTreeFullPath = element.getElementUIHint().getDictCodeTreeFull();
                isMultiValue = element.getElementUIHint().getEditStyle() == ElementDataEditStyle.CheckBox;
            }

            for (Object object : objects) {
                ValueObject vo = ValueObject.valueOf(BeanKit.getPropertyValue(object, fieldCode));
                if (vo.isNull()) continue;
                String strVal = vo.strValue();
                if (StringKit.isBlank(strVal)) continue;
                //???????????????
                String dictName = strVal;
                //????????????
                if (isMultiValue) {
                    List<String> strList = ListKit.listOf(strVal.split(","));
                    List<String> nameList = new ArrayList<String>(strList.size());
                    strList.forEach(strItem -> {
                        String _dictName = strItem;
                        DictItemNode itemNode = dictMap.get(strItem);
                        if (itemNode != null) {
                            _dictName = itemNode.getName();
                        }
                        nameList.add(_dictName);
                    });
                    dictName = StringKit.join(nameList, ",");
                } else {
                    DictItemNode itemNode = dictMap.get(strVal);
                    if (itemNode != null) {
                        dictName = isTreeFullPath ? itemNode.getFullName("/") : itemNode.getName();
                    }
                }
                dictName = StringKit.nvl(dictName, strVal);

                BeanKit.setPropertyValue(object, fieldCode, dictName);

            }
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param combiner combiner
     * @param dataForm dataForm
     * @param object   object
     */
    private void fillDictNameByObject(DataFormCombiner combiner, DataForm dataForm, Object object) {
        fillDictNameByObjects(combiner, dataForm, ListKit.listOf(object));
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param queryParam queryParam
     */
    protected void fillContextEnvToQueryParam(Map<String, Object> queryParam) {
        ContextEnvFetcher fetcher = null;
        try {
            fetcher = ApplicationContextHolder.getBean(ContextEnvFetcher.class);
        } catch (Exception e) {
            return;
        }
        //???????????????????????????????????????????????????????????????????????????
        if (fetcher != null) {
            Map<String, Object> params = fetcher.fetchContextEnvParams();
            queryParam.putAll(params);
        }
    }

    /**
     * <dt>????????????</dt>
     * <dd>????????????:form DataForm???{package}.{id}</dd>
     * <dd>????????????:param DataForm???????????????,?????????name1=value1;name2=value2,????????????,???none</dd>
     * <dd>????????????:sort DataForm?????????????????????,?????????name1=asc,name2=desc,????????????,???none</dd>
     * <dd>????????????:size ????????????,???????????????,???0</dd>
     * <dd>????????????:index ????????????,???0??????</dd>
     * ????????????????????????filter??????
     *
     * @param form        DataForm????????????
     * @param paramMatrix DataForm????????????
     * @param sortMatrix  DataForm????????????
     * @param size        ????????????
     * @param index       ????????????(???0??????)
     * @return DataFormCombiner
     */
    @GetMapping("/data/list/{form}/{param}/{sort}/{index:[\\d]+}-{size:[\\d]+}")
    public DataFormCombiner<PaginResult<?>> queryDataList(@PathVariable("form") String form
            , @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix
            , @MatrixVariable(pathVar = "sort") MultiValueMap<String, Object> sortMatrix
            , @PathVariable("size") Integer size, @PathVariable("index") Integer index
    ) {
        DataFormAddon addon = getDataFormAddon(form);
        FormStyle formStyle = getFormStyle(addon.dataForm);
        ValidateKit.isTrue(isListStyle(addon.dataForm), "??????????????????????????????????????????DataForm.FormStyle??????????????????formStyle={0}", formStyle);

        Map<String, Object> paramMap = MapKit.flatMultiValueMap(paramMatrix);
        fillContextEnvToQueryParam(paramMap);

        Map<String, Object> sortMap = MapKit.flatMultiValueMap(sortMatrix);
        Map<String, ?> filterMap = HttpKit.getRequestParameterMap(WebHolder.getRequest());
        paramMap.remove("1");   //??????????????????
        sortMap.remove("1");
        filterMap.remove("_");


        PaginResult<?> data = addon.dataListHandler.query(addon.dataForm, paramMap, filterMap, sortMap, size, index);

        DataFormCombiner<PaginResult<?>> ret = new DataFormCombiner<PaginResult<?>>();
        fillDataFormCombiner(ret, addon.dataForm, paramMap);
        ret.setBody(data);

        //????????????????????????????????????????????????????????????????????????????????????????????????
        fillDictItemByValues(ret, ret.getMeta(), ret.getBody().getDataList());

        return ret;
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param form        form
     * @param paramMatrix paramMatrix
     * @param sortMatrix  sortMatrix
     * @param size        size
     * @param index       index
     * @return DataFormCombiner
     */
    @GetMapping("/text/data/list/{form}/{param}/{sort}/{index:[\\d]+}-{size:[\\d]+}")
    public DataFormCombiner<PaginResult<?>> queryTextDataList(@PathVariable("form") String form
            , @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix
            , @MatrixVariable(pathVar = "sort") MultiValueMap<String, Object> sortMatrix
            , @PathVariable("size") Integer size, @PathVariable("index") Integer index
    ) {
        //??????self???????????????????????????AOP??????
        DataFormCombiner<PaginResult<?>> ret = self.queryDataList(form, paramMatrix, sortMatrix, size, index);
        //?????????????????????
        fillDictNameByObjects(ret, ret.getMeta(), ret.getBody().getDataList());

        return ret;
    }

    @GetMapping("/excel/data/list/{form}/{param}/{sort}/{index:[\\d]+}-{size:[\\d]+}")
    public void queryExcelDataList(@PathVariable("form") String form
            , @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix
            , @MatrixVariable(pathVar = "sort") MultiValueMap<String, Object> sortMatrix
            , @PathVariable("size") Integer size, @PathVariable("index") Integer index
            , @RequestParam("filename") String filename
            , HttpServletResponse response
    ) {

        DataFormCombiner<PaginResult<?>> ret = queryTextDataList(form, paramMatrix, sortMatrix, size, index);
        String downLoadName = filename;
        if(StringKit.isBlank(filename)){
            downLoadName = ret.getMeta().getCode() + "-" + System.currentTimeMillis() + ".xlsx";
        }

        DataFormExporter exporter = new DataFormExporter(ret);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("office/data-table-tpl.xlsx");
        OutputStream outputStream = null;
        try {
            response.reset();
            response.setContentType(ContentType.XLSX);
//            response.addHeader("Content-Disposition", "attachment; filename=" + downLoadName);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(downLoadName.getBytes("UTF-8"),"ISO8859-1"));
//            FileKit.touchFile("/Users/cytsir/Documents/tmp/data-list.xlsx");
//            outputStream = new FileOutputStream("/Users/cytsir/Documents/tmp/data-list.xlsx");
            outputStream = response.getOutputStream();

            exporter.exportListToExcel(inputStream, outputStream);

            response.flushBuffer();
            outputStream.flush();

        } catch (FileNotFoundException e) {
            throw new DataFormException(e);
        } catch (IOException e) {
            throw new DataFormException(e);
        } finally {
            IOKit.close(inputStream);
            IOKit.close(outputStream);
        }


    }

    /**
     * ?????????????????????????????????????????????,????????????????????????????????????????????????????????????
     *
     * @param form        form
     * @param elementCode elementCode
     * @param paramMatrix paramMatrix
     * @return list
     */
    @GetMapping("/element-dict-tree/{form}/{elementCode}/{param}")
    public List<DictItemNode> getElementDictAsTree(@PathVariable("form") String form, @PathVariable("elementCode") String elementCode
            , @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix) {
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        DataFormElement element = dataForm.getElement(elementCode);
        if (element == null) return null;

        Map<String, Object> param = MapKit.flatMultiValueMap(paramMatrix);
        fillContextEnvToQueryParam(param);

        List<DictItemNode> dictItems = dictExprResolve.getDictItems(element, param);

        return dictItems;
//        return TreeNodeKit.buildTree(dictItems, DictItemNode::getSortCode);
    }

    /**
     * ??????????????????
     *
     * @param form        DataForm????????????
     * @param paramMatrix DataForm????????????
     * @return DataFormCombiner
     */
    @GetMapping("/data/one/{form}/{param}")
    public DataFormCombiner<Object> queryDataOne(@PathVariable("form") String form, @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix) {

        DataFormAddon addon = getDataFormAddon(form);
        FormStyle formStyle = getFormStyle(addon.dataForm);
        ValidateKit.isTrue(!isListStyle(addon.dataForm), "??????????????????????????????????????????DataForm.FormStyle????????????????????????formStyle={0}", formStyle);
        ValidateKit.notNull(addon.dataOneHandler);

        //??????????????????
        DataOneHandler<Object> handler = (DataOneHandler<Object>) addon.dataOneHandler;

        Map<String, Object> param = MapKit.flatMultiValueMap(paramMatrix);
        fillContextEnvToQueryParam(param);

        Object object = handler.query(addon.dataForm, param);

        DataFormCombiner<Object> ret = new DataFormCombiner<Object>();
        fillDataFormCombiner(ret, addon.dataForm, param);
        ret.setBody(object);

        //????????????????????????????????????????????????????????????????????????????????????????????????
        fillDictItemByValue(ret, ret.getMeta(), ret.getBody());

        return ret;
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param form        form
     * @param paramMatrix paramMatrix
     * @return DataFormCombiner
     */
    @GetMapping("/text-data/one/{form}/{param}")
    public DataFormCombiner<Object> queryTextDataOne(@PathVariable("form") String form, @MatrixVariable(pathVar = "param") MultiValueMap<String, Object> paramMatrix) {
        DataFormCombiner<Object> ret = self.queryDataOne(form, paramMatrix);
        //???????????????????????????
        fillDictNameByObject(ret, ret.getMeta(), ret.getBody());

        return ret;
    }


    private void fixDate(DataForm dataForm, MapObject mapData) {
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            if (element.getDataType() == ElementDataType.Date) {
                String code = element.getCode();
                if (!mapData.containsKey(code)) continue;
                mapData.put(code, mapData.getValue(code).dateValue());
            }
        }
    }

    private void fixDate(DataForm dataForm, List<MapObject> mapDataList) {
        List<DataFormElement> elements = dataForm.getElements();
        for (DataFormElement element : elements) {
            if (element.getDataType() == ElementDataType.Date) {
                String code = element.getCode();
                for (MapObject mapData : mapDataList) {
                    if (!mapData.containsKey(code)) continue;
                    mapData.put(code, mapData.getValue(code).dateValue());
                }
            }
        }
    }

    protected List<?> saveBeanDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;

        List<Object> objectList = DataFormUtils.mapDataListToBeanDataList(dataForm, dataList);
        ValidateKit.isTrue(objectList.size() > 0, "???????????????????????????????????????????????????????????????");

        Object firstObject = objectList.get(0);
        if (isListStyle(dataForm)) {
            BeanDataListHandler handler = ((BeanDataListHandler) addon.dataListHandler);
            handler.save(dataForm, objectList);
        } else {
            BeanDataOneHandler handler = (BeanDataOneHandler) addon.dataOneHandler;
            handler.save(dataForm, firstObject);
        }

        return objectList;
    }

    protected List<?> saveMapDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;

        MapObject firstObject = dataList.get(0);
        if (isListStyle(dataForm)) {
            MapDataListHandler handler = ((MapDataListHandler) addon.dataListHandler);
            handler.save(dataForm, dataList);
        } else {
            MapDataOneHandler handler = (MapDataOneHandler) addon.dataOneHandler;
            handler.save(dataForm, firstObject);
        }
        return dataList;
    }


    public List<?> saveDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;
        ValidateKit.notEmpty(dataList, "the list data is empty");
        //????????????dataform??????????????????Handler
        FormDataModelType dataModelType = addon.dataForm.getDataModelType();
        String id = dataForm.getId();
        if (dataModelType == FormDataModelType.JavaBean) {
            return saveBeanDataList(addon, dataList);
        } else {
            return saveMapDataList(addon, dataList);
        }
    }

    /**
     * ????????????
     *
     * @param form     DataForm????????????
     * @param dataList ????????????
     * @return list
     */
    @PostMapping("/save/list/{form}")
    public List<?> saveDataList(@PathVariable("form") String form, @RequestBody List<MapObject> dataList) {
        HttpServletRequest request = WebHolder.getRequest();
        request.setAttribute("DATA_OBJECT", dataList);

        //???????????????dataform????????????????????????
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        fixDate(dataForm, dataList);

        return saveDataList(addon, dataList);
    }

    /**
     * ??????????????????
     *
     * @param form    DataForm????????????
     * @param dataOne ????????????
     * @return object
     */
    @PostMapping("/save/one/{form}")
    public Object saveDataOne(@PathVariable("form") String form, @RequestBody MapObject dataOne) {
        //????????????????????????????????????????????????????????????
        HttpServletRequest request = WebHolder.getRequest();
        request.setAttribute("DATA_OBJECT", dataOne);
        //???????????????dataform????????????????????????
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        ValidateKit.notEmpty(dataOne, "the list data is empty");
        fixDate(dataForm, dataOne);
        /*
         * ??????????????????????????????null???????????????0?????????
         * ??????????????????????????????
         * */
//        fixInitValue(dataForm, dataOne);

        List<MapObject> dataList = new ArrayList<MapObject>(1);
        dataList.add(dataOne);
        List<?> retDataList = saveDataList(addon, dataList);

        if (retDataList != null && retDataList.size() > 0) {
            return retDataList.get(0);
        } else {
            return dataOne;
        }

    }

    private void fixInitValue(DataForm dataForm, MapObject dataOne) {
        List<DataFormElement> elements = dataForm.getElements();
        elements.stream()
                .filter(element -> dataOne.get(element.getCode()) == null)
                .forEach(element -> {
                    convertInitValue(element, dataOne);
                });
    }

    private void convertInitValue(DataFormElement element, MapObject dataOne) {
        ElementDataType dataType = element.getDataType();
        String code = element.getCode();
        switch (dataType) {
            case Integer:
                dataOne.put(code, dataOne.getValue(code).intValue(0));
                break;
            case Double:
                dataOne.put(code, dataOne.getValue(code).doubleValue(0d));
                break;
            default:
                break;
        }
    }


    protected int deleteDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;
        ValidateKit.notEmpty(dataList, "the list data is empty");
        //????????????dataform??????????????????Handler
        FormDataModelType dataModelType = addon.dataForm.getDataModelType();
        String id = dataForm.getId();
        if (dataModelType == FormDataModelType.JavaBean) {
            return deleteBeanDataList(addon, dataList);
        } else {
            return deleteMapDataList(addon, dataList);
        }
    }

    protected int deleteBeanDataList(DataFormAddon addon, List<MapObject> dataList) {
        List<Object> objectList = DataFormUtils.mapDataListToBeanDataList(addon.dataForm, dataList);

        Object firstObject = objectList.get(0);
        if (isListStyle(addon.dataForm)) {
            BeanDataListHandler handler = ((BeanDataListHandler) addon.dataListHandler);
            return handler.delete(addon.dataForm, objectList);
        } else {
            BeanDataOneHandler handler = (BeanDataOneHandler) addon.dataOneHandler;
            return handler.delete(addon.dataForm, firstObject);
        }
    }

    protected int deleteMapDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;

        MapObject firstObject = dataList.get(0);
        if (isListStyle(dataForm)) {
            MapDataListHandler handler = ((MapDataListHandler) addon.dataListHandler);
            return handler.delete(dataForm, dataList);
        } else {
            MapDataOneHandler handler = (MapDataOneHandler) addon.dataOneHandler;
            return handler.delete(dataForm, firstObject);
        }
    }

    /**
     * ??????????????????
     *
     * @param form     DataForm????????????
     * @param dataList ??????????????????
     * @return int
     */
    @PostMapping("/delete/list/{form}")
    public int deleteDataList(@PathVariable("form") String form, @RequestBody List<MapObject> dataList) {
        //???????????????dataform????????????????????????
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        fixDate(dataForm, dataList);

        return deleteDataList(addon, dataList);
    }

    /**
     * ??????????????????
     *
     * @param form    DataForm????????????
     * @param dataOne ????????????
     * @return int
     */
    @PostMapping("/delete/one/{form}")
    public int deleteDataOne(@PathVariable("form") String form, @RequestBody MapObject dataOne) {
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        ValidateKit.notEmpty(dataOne, "the list data is empty");
        fixDate(dataForm, dataOne);

        List<MapObject> dataList = new ArrayList<MapObject>(1);
        dataList.add(dataOne);
        return deleteDataList(addon, dataList);

    }

    /**
     * ??????????????????DataHandler?????????
     *
     * @param form    DataForm????????????
     * @param method  DataHandler????????????
     * @param mapData ????????????
     * @return Object
     */
    @PostMapping("/invoke/{form}/{method}")
    public Object invokeDataListHandlerMethod(@PathVariable("form") String form, @PathVariable("method") String method, @RequestBody MapObject mapData) {
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        if (mapData == null) mapData = new MapObject();

        if (isListStyle(dataForm)) {
            DataListHandler handler = ((DataListHandler) addon.dataListHandler);
            return invokeHandlerMethod(handler, method, dataForm, mapData);
        } else {
            DataOneHandler handler = (DataOneHandler) addon.dataOneHandler;
            return invokeHandlerMethod(handler, method, dataForm, mapData);
        }
    }

    private Object invokeHandlerMethod(Object handler, String methodName, DataForm dataForm, MapObject param) {
        try {
            Class<?>[] parameterTypes = new Class<?>[]{DataForm.class, MapObject.class};
            Method method = handler.getClass().getMethod(methodName, parameterTypes);
            Object[] args = new Object[]{dataForm, param};
            return method.invoke(handler, args);
        } catch (NoSuchMethodException e) {
            throw new DataFormException("???????????????[{0}.{1}]???????????????", handler.getClass().getName(), methodName);
        } catch (IllegalAccessException e) {
            throw new DataFormException(e, "???????????????[{0}.{1}]??????????????????", handler.getClass().getName(), methodName);
        } catch (InvocationTargetException e) {
            throw new BizException(e.getTargetException(), e.getTargetException().getMessage());
        }
    }

    @PostMapping("/validate/list/{form}")
    public List<ValidateResult> validateDataList(@PathVariable("form") String form, @RequestBody List<MapObject> dataList) {
        //???????????????dataform????????????????????????
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        fixDate(dataForm, dataList);

        return validateDataList(addon, dataList);
    }

    @PostMapping("/validate/one/{form}")
    public ValidateResult validateDataOne(@PathVariable("form") String form, @RequestBody MapObject dataOne) {
        //???????????????dataform????????????????????????
        DataFormAddon addon = getDataFormAddon(form);
        DataForm dataForm = addon.dataForm;
        ValidateKit.notNull(dataForm, "dataform not found:" + form);
        fixDate(dataForm, dataOne);

        List<MapObject> dataList = new ArrayList<MapObject>(1);
        dataList.add(dataOne);
        List<ValidateResult> retDataList = validateDataList(addon, dataList);

        if (retDataList == null || retDataList.size() <= 0) {
            return null;
        } else {
            return retDataList.get(0);
        }
    }

    protected List<ValidateResult> validateDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;
        ValidateKit.notEmpty(dataList, "the list data is empty");
        //????????????dataform??????????????????Handler
        FormDataModelType dataModelType = addon.dataForm.getDataModelType();
        String id = dataForm.getId();
        if (dataModelType == FormDataModelType.JavaBean) {
            return validateBeanDataList(addon, dataList);
        } else {
            return validateMapDataList(addon, dataList);
        }
    }

    protected List<ValidateResult> validateBeanDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;

        List<Object> objectList = DataFormUtils.mapDataListToBeanDataList(dataForm, dataList);
        ValidateKit.isTrue(objectList.size() > 0, "???????????????????????????????????????????????????????????????");

        if (isListStyle(dataForm)) {
            BeanDataListHandler handler = ((BeanDataListHandler) addon.dataListHandler);
            return handler.validate(dataForm, objectList);
        } else {
            List<ValidateResult> results = new ArrayList<ValidateResult>();
            BeanDataOneHandler handler = (BeanDataOneHandler) addon.dataOneHandler;
            Object firstObject = objectList.get(0);
            ValidateResult vr = handler.validate(dataForm, firstObject);
            results.add(vr);
            return results;
        }

    }

    protected List<ValidateResult> validateMapDataList(DataFormAddon addon, List<MapObject> dataList) {
        DataForm dataForm = addon.dataForm;

        if (isListStyle(dataForm)) {
            MapDataListHandler handler = ((MapDataListHandler) addon.dataListHandler);
            return handler.validate(dataForm, dataList);
        } else {
            List<ValidateResult> results = new ArrayList<ValidateResult>();
            MapDataOneHandler handler = (MapDataOneHandler) addon.dataOneHandler;
            MapObject firstObject = dataList.get(0);
            ValidateResult vr = handler.validate(dataForm, firstObject);
            results.add(vr);
            return results;
        }
    }


}
