package com.vekai.appframe.base.aspect;

import cn.fisok.sqloy.core.PaginResult;
import com.vekai.auth.entity.Org;
import com.vekai.auth.service.AuthService;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormCombiner;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.PairBond;
import cn.fisok.raw.lang.ValueObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 在DataForm中，机构ID转用户名
 */
@Aspect
@Component
public class DataFormControllerOrgNameAspect extends DataFormVirNameConverter{

    @Autowired
    protected AuthService authService;

    /**
     * 需要转换为用户名的字段
     */
    public static final String ORG_FIELDS = "orgFields";
    private String orgDefaultFields = "createdOrg,CREATED_ORG";


    @AfterReturning(pointcut = "execution(* com.vekai.dataform.controller.DataFormController.getDataForm(..))", returning = "retData")
    public void doAfterQueryMeta(JoinPoint joinPoint, Object retData){
        DataFormCombiner<?> dataFormCombiner = null;

        if(retData instanceof DataFormCombiner){
            dataFormCombiner = (DataFormCombiner<?>)retData;
        }
        DataForm dataForm = dataFormCombiner.getMeta();
        if(dataForm == null){
            return ;
        }

        //如果关闭了自动处理，则不需要处理了
        Object closeConvert = dataForm.getProperties().get(CLOSE_NAME_AUTO_CONVERT);
        if("Y".equals(closeConvert)){
            return;
        }
        List<String> userFields = getOrgFields(dataForm);

        patchVir4Meta(dataFormCombiner,userFields);

    }

    /**
     * 列表查询处理结果中的用户ID部分
     * @param joinPoint
     * @param retData
     */
    @AfterReturning(pointcut = "execution(* com.vekai.dataform.controller.DataFormController.queryDataList(..))", returning = "retData")
    public void doAfterListQuery(JoinPoint joinPoint, Object retData){
        DataFormCombiner<PaginResult<?>> dataFormCombiner = null;

        if(retData instanceof DataFormCombiner){
            dataFormCombiner = (DataFormCombiner<PaginResult<?>>)retData;
        }
        DataForm dataForm = dataFormCombiner.getMeta();
        if(dataForm == null){
            return ;
        }

        //如果关闭了自动处理，则不需要处理了
        Object closeConvert = dataForm.getProperties().get(CLOSE_NAME_AUTO_CONVERT);
        if("Y".equals(closeConvert)){
            return;
        }
        List<String> orgFields = getOrgFields(dataForm);
        if(!hasConvertFields(dataForm,orgFields))return;//如果没有需要转换的字段，就不要浪费时间了



        //把以前的数据模型重新组装重新处理
        PaginResult<?> body = dataFormCombiner.getBody();
        final PaginResult<Map<String,Object>> retBody = new PaginResult<Map<String,Object>>();    //创建一个Map为数据的分页对象
        BeanKit.copyProperties(body,retBody);                                                           //把原数据复制到新的分页对象上
        dataFormCombiner.setBody(retBody);                                                              //重做分页对象


        (new DataFormCombinerVirFieldExpansion(){
            public void fillBody(DataFormCombiner<?> dataFormCombiner,List<PairBond> codePairBonds) {
                List<?> dataList = body.getDataList();
                List<Map<String,Object>> mapDataList = new ArrayList<Map<String,Object>>(dataList.size());
                retBody.setDataList(mapDataList);

                for(int i=0;i<dataList.size();i++){
                    Object rowObject = dataList.get(i);
                    Map<String,Object> rowMap = null;
                    if(rowObject instanceof Map){
                        rowMap = (Map<String,Object>)rowObject;
                    }else{
                        rowMap = BeanKit.bean2Map(rowObject);
                    }

                    for(PairBond codePairBond:codePairBonds){
                        String orgName = "";
                        String orgId = StringKit.nvl(rowMap.get(codePairBond.getLeft()),"");
                        if(StringKit.isNotBlank(orgId)){
                            orgName = StringKit.nvl(getOrgName(orgId),orgId);
                        }
                        String virElementCode = ValueObject.valueOf(codePairBond.getRight()).strValue();
                        rowMap.put(virElementCode,orgName);
                    }
                    mapDataList.add(rowMap);
                }
            }
        }).exec(orgFields,dataFormCombiner);


    }

    /**
     * 列表查询处理结果中的用户ID部分
     * @param joinPoint
     * @param retData
     */
    @AfterReturning(pointcut = "execution(* com.vekai.dataform.controller.DataFormController.queryDataOne(..))", returning = "retData")
    public void doAfterInfoQuery(JoinPoint joinPoint, Object retData){
        DataFormCombiner<Object> dataFormCombiner = null;

        if(retData instanceof DataFormCombiner){
            dataFormCombiner = (DataFormCombiner<Object>)retData;
        }
        DataForm dataForm = dataFormCombiner.getMeta();
        if(dataForm == null){
            return ;
        }

        //如果关闭了自动处理，则不需要处理了
        Object closeConvert = dataForm.getProperties().get(CLOSE_NAME_AUTO_CONVERT);
        if("Y".equals(closeConvert)){
            return;
        }
        List<String> orgFields = getOrgFields(dataForm);
        if(!hasConvertFields(dataForm,orgFields))return;//如果没有需要转换的字段，就不要浪费时间了

        //把以前的数据模型重新组装重新处理
        Object body = dataFormCombiner.getBody();
        if(body == null)return;
        Map<String,Object> mapData = null;
        if(body instanceof Map){
            mapData = (Map<String,Object>)body;
        }else{
            mapData = BeanKit.bean2Map(body);
        }
        dataFormCombiner.setBody(mapData);  //把数据换成Map，好加虚字段

        (new DataFormCombinerVirFieldExpansion(){
            public void fillBody(DataFormCombiner<?> dataFormCombiner,List<PairBond> codePairBonds) {
                Map<String,Object> rowObject = (Map<String,Object>)dataFormCombiner.getBody();
                for(PairBond codePairBond:codePairBonds){
                    String orgName = "";
                    String orgId = StringKit.nvl(rowObject.get(codePairBond.getLeft()),"");
                    if(StringKit.isNotBlank(orgId)){
                        orgName = StringKit.nvl(getOrgName(orgId),orgId);
                    }
                    String virElementCode = ValueObject.valueOf(codePairBond.getRight()).strValue();
                    rowObject.put(virElementCode,orgName);
                }
            }
        }).exec(orgFields,dataFormCombiner);

    }

    /**
     * 取需要转换为用户名的字段
     * @param dataForm
     * @return
     */
    public List<String> getOrgFields(DataForm dataForm){
        String fields1 = StringKit.nvl(dataForm.getProperties().get(ORG_FIELDS),"");
        return parseMergeAsList(fields1,orgDefaultFields);
    }


    private String getOrgName(String orgId){
        Org org = authService.getOrg(orgId);
        if(org != null){
            return org.getName();
        }else{
            return null;
        }
    }
}
