package com.vekai.auth.service;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.TreeNodeWrapper;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.dao.SecurityDao;
import com.vekai.auth.entity.Org;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.model.OrgNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrgService {

    @Autowired
    protected BeanCruder beanCruder;
    @Autowired
    protected SecurityDao securityDao;

    public List<TreeNodeWrapper<Org>> getAllOrgList() {
        return getSubOrgList("_ALL_");
    }



    public List<TreeNodeWrapper<Org>> getSubOrgList(String orgId) {
        User user = AuthHolder.getUser();
        String userOrgId = user.getOrgId();
        StringBuilder sqlBuilder = new StringBuilder("select * from AUTH_ORG M");
        if (!"_ALL_".equals(orgId)) {
            sqlBuilder.append(
                    " where exists (select 1  from AUTH_ORG C where C.ID=:orgId and M.SORT_CODE like CONCAT(C.SORT_CODE, '%'))");
        }else{
            sqlBuilder.append(
                    " WHERE LOCATE(CONCAT(CONCAT('/',:userOrgId),'/'),CONCAT(CONCAT('/',M.SORT_CODE),'/'))>0");
        }
        sqlBuilder.append(" ORDER BY SORT_CODE ASC,CODE ASC,ID ASC");

        List<Org> orgList =
                beanCruder.selectList(Org.class, sqlBuilder.toString(), "orgId", orgId,"userOrgId",userOrgId);
        List<TreeNodeWrapper<Org>> orgTrees = null;
        if (null != orgList && !orgList.isEmpty()) {
            orgTrees = buildOrgAsTree(orgList);
        }

        return orgTrees;

    }


    public List<Org> querySubOrgList(String orgId){
        return securityDao.getSubOrgList(orgId);
    }

    /**
     * 根据排序号，把机构列表组装成为机构树图
     *
     * @param orgList
     * @return
     */
    private List<TreeNodeWrapper<Org>> buildOrgAsTree(List<Org> orgList) {
        List<TreeNodeWrapper<Org>> orgTrees = new ArrayList<>();
        List<String> usedSortCodes = new ArrayList<>();
        TreeNodeWrapper<Org> orgTree;
        for (int i = 0; i < orgList.size(); i++) {
            Org curOrg = orgList.get(i);
            String curSortCode = curOrg.getSortCode();

            if (usedSortCodes.contains(curSortCode)) {
                continue;
            }
            usedSortCodes.add(curSortCode);

            orgTree = new TreeNodeWrapper<>();
            orgTree.setValue(curOrg);

            List<TreeNodeWrapper<Org>> childrenOrgTrees =
                    getChildrenOrgTree(orgList, usedSortCodes, orgTree, curSortCode);
            if (!childrenOrgTrees.isEmpty()) {
                orgTree.setChildren(childrenOrgTrees);
            }

            orgTrees.add(orgTree);
        }

        return orgTrees;
    }


    private List<TreeNodeWrapper<Org>> getChildrenOrgTree(List<Org> orgList, List<String> usedSortCodes,
                                                          TreeNodeWrapper<Org> parent, String sortCode) {
        List<TreeNodeWrapper<Org>> result = new ArrayList<>();
        TreeNodeWrapper<Org> orgTree;
        for (int i = 0; i < orgList.size(); i++) {
            Org curOrg = orgList.get(i);
            String curSortCode = curOrg.getSortCode();

            if (curOrg.getSortCode().equals(sortCode) || !curSortCode.startsWith(sortCode)) {
                continue;
            }

            if (usedSortCodes.contains(curSortCode)) {
                continue;
            }
            usedSortCodes.add(curSortCode);

            orgTree = new TreeNodeWrapper<>();
            //orgTree.setParent(parent);
            orgTree.setValue(curOrg);

            List<TreeNodeWrapper<Org>> childrenOrgTrees =
                    getChildrenOrgTree(orgList, usedSortCodes, orgTree, curSortCode);
            if (!childrenOrgTrees.isEmpty()) {
                orgTree.setChildren(childrenOrgTrees);
            }

            result.add(orgTree);
        }

        return result;
    }


    /**
     * 关联用户至当前机构下
     *
     * @param orgId
     * @param userIdList
     * @return
     */
    @Transactional
    public Integer relateUsers(String orgId, List<String> userIdList){
        Integer result = 0;
        if (null == userIdList || userIdList.isEmpty() || StringKit.isBlank(orgId)) {
            return result;
        }

        for(String userId : userIdList){
//            UserOrg userOrg = beanCruder.selectOne(UserOrg.class, "SELECT * FROM AUTH_USER_ORG WHERE USER_ID=:userId and ORG_ID=:orgId",
//                    MapKit.mapOf("userId", userId, "orgId",  orgId));
//            if(userOrg == null){
//                userOrg = new UserOrg();
//                userOrg.setUserId(userId);
//                userOrg.setOrgId(orgId);
//                result = result + beanCruder.save(userOrg);
//            }
            User user = beanCruder.selectOneById(User.class, MapKit.mapOf("id", userId));
            if(user!=null){
                user.setOrgId(orgId);
                result = result + beanCruder.save(user);
            }
        }
        return result;
    }

    /**
     * 删除当前用户下的某个角色
     *
     * @param orgId
     * @param userId
     * @return
     */
    @Transactional
    public Integer deleteUser(String orgId, String userId){
        Integer result = 0;
        if (StringKit.isBlank(orgId) || StringKit.isBlank(userId)) {
            return result;
        }

        result = beanCruder.execute("DELETE FROM AUTH_USER_ORG WHERE ORG_ID=:orgId and USER_ID=:userId",
                MapKit.mapOf("orgId" , orgId , "userId", userId));

        return result;
    }


}
