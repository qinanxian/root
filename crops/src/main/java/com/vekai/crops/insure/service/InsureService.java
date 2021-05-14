package com.vekai.crops.insure.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.common.entity.AuthOrg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InsureService {

    protected static Logger logger = LoggerFactory.getLogger(InsureService.class);

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 获取当前保险公司已配置的网点
     * @param id
     * @return
     */
    public List<AuthOrg> getExistedNetwork(String id){
        String sql = "SELECT * FROM AUTH_ORG WHERE ID IN(" +
                "SELECT NET_ID FROM BX_NET_COMPANY_REL WHERE COM_ID = :id)";
        List<AuthOrg> existedNetwork = beanCruder.selectList(AuthOrg.class, sql,"id",id);

        return existedNetwork;
    }
    /**
     * 根据orgId、comId判断BX_NET_COMPANY_REL表里是否存在该数据
     * @param orgId
     * @param comId
     * @return
     */
    public boolean isExistNetworkByOrgId(String orgId,String comId){

        int count= 0;
        boolean result = false;
        String sql = "select * from BX_NET_COMPANY_REL where NET_ID = :orgId and COM_ID = :comId";
        count = beanCruder.selectCount(sql,"orgId", orgId,"comId", comId).intValue();

        if (count>0) result = true;
        return result;
    }

    /**
     * 根据orgId查询网点名称
     * @param orgId
     * @return
     */
    public String getNetName(String orgId){
        String netName="";
        String sql="SELECT NAME FROM AUTH_ORG WHERE ID = :orgId";
        netName = beanCruder.selectOne(String.class, sql,"orgId",orgId);
        return netName;
    }
}
