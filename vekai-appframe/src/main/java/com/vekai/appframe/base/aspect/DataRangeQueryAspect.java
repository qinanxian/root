package com.vekai.appframe.base.aspect;

import com.vekai.appframe.base.pilot.service.MyPilotService;
import com.vekai.auth.entity.Org;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.OrgService;
import com.vekai.base.pilot.model.*;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class DataRangeQueryAspect {
    @Autowired
    MyPilotService myPilotService;
    @Autowired
    OrgService orgService;

    public static final String K_RANGE_USER = "USER";
    public static final String K_RANGE_ORG = "ORG";
    public static final String V_RANGE_USER = "RANGE_USER";
    public static final String V_RANGE_ORG = "RANGE_ORG";

    /**
     * 列表查询处理结果中的用户ID部分
     * @param joinPoint
     */
    @Before("beforeQuery()")
    public void doBeforeListQuery(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        DataForm dataForm = null;
        Map<String, ?> queryParameters = null;
        if(args[0] instanceof DataForm){
            dataForm = (DataForm)args[0];
        }
        if(args[1] instanceof Map){
            queryParameters = (Map<String, ?>)args[1];
        }
        if(dataForm == null || queryParameters == null)return;

        //1.把下层的数据权限节点全部找出来
        StartNode startNode = myPilotService.getMyPilot();
        if(startNode == null) return;
        Map<DataQueryScope, PermitNode> queryScopeMap = MapKit.newHashMap();
        fillDataRangePermits(startNode.getChildren(),queryScopeMap);

        //从显示模板取数据权限语句，如果没有，则不控制
        String permitHint = dataForm.getQuery().getPermitHint();
        if(StringKit.isBlank(permitHint))return;
        Map<String,String> rangeMap = parseRangeClause(permitHint);

        String whereClause = "";
        Map<String,Object> params = MapKit.newHashMap();
        /** 2. 逐一判断权限 */
        if(queryScopeMap.containsKey(DataQueryScope.AllDataList)){                  //1.是否所有数据权限

        }else if(queryScopeMap.containsKey(DataQueryScope.MyOrgBranchDataList)){    //2.是否是本机构及下属权限
            Org org = AuthHolder.getUser().getOrg();
            String orgId = org!=null?org.getId():"";
            List<Org> orgList = orgService.querySubOrgList(orgId);
            List<String> orgIdList = orgList.stream().map(row -> row.getId()).collect(Collectors.toList());

            whereClause = rangeMap.get(K_RANGE_ORG);
            params = MapKit.mapOf(V_RANGE_ORG, orgIdList);
        }else if(queryScopeMap.containsKey(DataQueryScope.MyOrgDataList)){          //3.是否是本机构权限
            Org org = AuthHolder.getUser().getOrg();
            String orgId = org!=null?org.getId():"";

            whereClause = rangeMap.get(K_RANGE_ORG);
            params = MapKit.mapOf(V_RANGE_ORG, ListKit.listOf(orgId));
        }else if(queryScopeMap.containsKey(DataQueryScope.OnlyMyDataList)){         //4.是否是本人权限
            whereClause = rangeMap.get(K_RANGE_USER);
            params = MapKit.mapOf(V_RANGE_USER, AuthHolder.getUser().getId());
        }

        if(StringKit.isNotBlank(whereClause) && params.size()>0){
            String where = dataForm.getQuery().getWhere();
            StringBuffer sbWhere = new StringBuffer();
            if(StringKit.isNotBlank(where)){
                sbWhere.append(where).append(" AND ");
            }
            sbWhere.append(whereClause);
            dataForm.getQuery().setWhere(sbWhere.toString());
            MapKit.merge(queryParameters,params);
        }

    }

    /**
     * 解析为范围查询语句
     * @param permitHint
     * @return
     */
    private Map<String,String> parseRangeClause(String permitHint){
        Map<String,String> clauseMap  = MapKit.newHashMap();
        String[] pairs = permitHint.split(",");
        if(pairs==null||pairs.length == 0)return clauseMap;
        for(String pair : pairs){
            String[] kv = pair.split("->");
            if(kv != null || kv.length == 2){
                clauseMap.put(kv[0],kv[1]);
            }
        }

        return clauseMap;
    }

    /**
     * 把起始节点下面的数据范围节点全部找出来
     * @param nodes
     * @param permitNodes
     */
    private void fillDataRangePermits(List<? extends BaseNode> nodes, Map<DataQueryScope, PermitNode> permitNodes){
        if(nodes == null || nodes.size() ==0 )return;
        for(int i=0;i<nodes.size();i++){
            BaseNode node = nodes.get(i);
            if(node instanceof MenuNode){
                MenuNode menuNode = (MenuNode)node;
                fillDataRangePermits(menuNode.getChildren(),permitNodes);
            }else if(node instanceof PermitNode){
                PermitNode permitNode = (PermitNode)node;
                List<? extends BaseNode> children = permitNode.getChildren();
                if(children == null || children.size()==0){
                    if(StringKit.isNotBlank(permitNode.getPermitCode()) && permitNode.getType() == NodeType.datascope){
                        DataQueryScope dataQueryScope = DataQueryScope.valueOf(permitNode.getPermitCode());
                        permitNodes.put(dataQueryScope,permitNode);
                    }
                }else{
                    fillDataRangePermits(children,permitNodes);
                }
            }
        }
    }

    @Pointcut("execution(* com.vekai.dataform.handler.DataListHandler+.query(..))")
    public void beforeQuery() {
    }
}
