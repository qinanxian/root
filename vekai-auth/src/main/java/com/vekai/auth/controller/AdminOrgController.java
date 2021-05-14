package com.vekai.auth.controller;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.TreeNodeWrapper;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.entity.Org;
import com.vekai.auth.service.OrgService;
import com.vekai.auth.model.OrgNode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "机构管理")
@RestController
@RequestMapping("/auth/admin/org/")
public class AdminOrgController {

    @Autowired
    protected OrgService orgService;
    @Autowired
    protected BeanCruder beanCruder;

    /**
     * 取机构以及下级机构列表
     *
     * @return
     */
    @GetMapping("/{orgId}/subOrgTree")
    public List<TreeNodeWrapper<Org>> getSubOrgList(@PathVariable("orgId") String orgId) {
        return orgService.getSubOrgList(orgId);
    }

    @GetMapping("/allOrgTree")
    public List<TreeNodeWrapper<Org>> getAllOrgTree() {
        return orgService.getAllOrgList();
    }


    /**
     * 关联用户至当前机构下
     *
     * @param userId
     * @param orgIdList
     * @return
     */
    @PostMapping("/relateUsers/{userId}")
    public Integer relateUser(@PathVariable("userId") String userId, @RequestBody List<String> orgIdList){
        return orgService.relateUsers(userId, orgIdList);
    }

    /**
     * 删除当前用户下的某个角色
     *
     * @param userId
     * @param orgId
     * @return
     */
    @PostMapping("/deleteUser/{orgId}/{userId}")
    public Integer deleteUser(@PathVariable("orgId") String orgId, @PathVariable("userId") String userId) {
        return orgService.deleteUser(orgId, userId);
    }

    /**
     * 获取所有机构
     * @return
     */
    @GetMapping("/getAllOrgs")
    public List<Org> getAllOrgs() {
        return beanCruder.selectList(Org.class,"select * from AUTH_ORG");
    }

    /**
     * 获取机构及其下属机构
     * @param orgId
     * @return
     */
    @GetMapping("/getSubOrg/{orgId}")
    public List<Org> getSubOrg(@PathVariable("orgId") String orgId) {
        return beanCruder.selectList(Org.class,"select * from AUTH_ORG");
    }

    @GetMapping("/getOrgByOrgId/{orgId}")
    public Org getOrgByOrgId(@PathVariable("orgId") String orgId) {
        return beanCruder.selectOneById(Org.class, MapKit.mapOf("id",orgId));
    }
}
