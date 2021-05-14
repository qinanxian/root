package com.vekai.auth.service;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.dao.SecurityDao;
import com.vekai.auth.entity.RolePermit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    SecurityDao securityDao;

    public int saveUserPilot(String roleId, @RequestBody MapObject[] nodes){
        ValidateKit.notBlank(roleId,"没有找到对应角色");
        String sql = "DELETE FROM AUTH_ROLE_PERMIT WHERE ROLE_ID=:roleId";
        beanCruder.execute(sql, MapKit.mapOf("roleId",roleId));

        if(nodes != null && nodes.length > 0){
            List<RolePermit> rolePermits = new ArrayList<>();
            flatAsRolePermitList(ListKit.listOf(nodes),rolePermits);

            rolePermits.sort(new Comparator<RolePermit>() {
                public int compare(RolePermit o1, RolePermit o2) {
                    return o1.getPermitCode().compareTo(o2.getPermitCode());
                }
            });
            rolePermits.forEach( rolePermit -> {
                rolePermit.setRoleId(roleId);
            });

            return beanCruder.save(rolePermits);
        }else{
            return 0;
        }
    }

    public List<RolePermit> getRolePermits(String roleId){
        ValidateKit.notBlank(roleId,"没有找到对应角色");
        return securityDao.getRolePermits(roleId);
    }

    /**
     * 取用户的所有权限
     * @param userId
     * @return
     */
    public List<RolePermit> getUserPermits(String userId){
        return securityDao.getUserPermits(userId);
    }

    /**
     * 把选择好的数据拉平为数据权限节点
     * @param nodeList
     * @return
     */
    private void flatAsRolePermitList(List<Map> nodeList, List<RolePermit> rolePermits){
        nodeList.forEach(node -> {
            String permitCode = (String)node.get("id");
            String ownerType = (String)node.get("selected");//Full,Half
            if(StringKit.isAnyBlank(permitCode,ownerType))return;

            RolePermit rolePermit = new RolePermit();
            rolePermit.setPermitCode(permitCode);
            rolePermit.setOwnerType(ownerType);
            rolePermits.add(rolePermit);

//            List<MapObject> children = node.getValue("children").objectArray();
            Object children = node.get("children");
            if(children==null)return;
            if(!(children instanceof ArrayList))return;
            List<Map> childrenList = (List<Map>)children;
            flatAsRolePermitList(childrenList,rolePermits);
        });
    }
}
