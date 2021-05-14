package com.vekai.appframe.auth.service;


import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.TreeNodeKit;
import com.vekai.appframe.auth.dto.PermitNode;
import com.vekai.appframe.auth.dto.RoleOwnedPermitBean;
import com.vekai.auth.authorization.AuthorizationSupport;
import com.vekai.auth.authorization.PermissionResourceType;
import com.vekai.auth.entity.Permit;
import com.vekai.auth.entity.RolePermit;
import com.vekai.auth.event.AuthzCfgModifiedEvent;
import com.vekai.auth.service.AuthService;
import com.vekai.auth.shiro.CustomWildcardPermission;
import com.vekai.base.menu.model.MenuEntry;
import com.vekai.base.menu.service.MenuService;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.service.DataFormService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoleOwnedPermitTreeManager {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected DataFormService dataFormService;

    @Autowired
    protected AuthorizationSupport authorizationSupport;

    @Autowired
    protected RolePermissionResolver rolePermissionResolver;

    @Autowired
    protected AuthService authService;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    ApplicationContext appContext;

    private volatile List<PermitNode> urlPermitNodes;

    private final static String MENU_FAKE_ROOT_SORT_CODE = "/Menu";
    private final static String DATAFORM_FAKE_ROOT_SORT_CODE = "/DataForm";
    private final static String REST_FAKE_ROOT_SORT_CODE = "/Rest";
    private final static String UD_FAKE_ROOT_SORT_CODE = "/ud";
    private final static String[] FAKE_SORT_CODES = {MENU_FAKE_ROOT_SORT_CODE,
            DATAFORM_FAKE_ROOT_SORT_CODE, REST_FAKE_ROOT_SORT_CODE, UD_FAKE_ROOT_SORT_CODE};

    private final static String[] CANDIDATE_MAPPING_BEANS = {"requestMappingHandlerMapping", "endpointHandlerMapping"};

    private Pattern urlVariablePattern = Pattern.compile("/\\s*\\{.+?}\\s*");

    static {
        Arrays.sort(FAKE_SORT_CODES);
        Arrays.sort(CANDIDATE_MAPPING_BEANS);
    }

    public PermitNode getRoleOwnedPermitTree(String roleCode) {

        PermitNode root = new PermitNode("/","/","全部", PermissionResourceType.All);

        PermitNode menuNode = new PermitNode(root,MENU_FAKE_ROOT_SORT_CODE,"/","菜单", PermissionResourceType.Menu);
        PermitNode dataFormNode = new PermitNode(root,DATAFORM_FAKE_ROOT_SORT_CODE,"/","DataForm", PermissionResourceType.DataForm);
        PermitNode restNode = new PermitNode(root,REST_FAKE_ROOT_SORT_CODE,"/","rest", PermissionResourceType.Rest);
        PermitNode udNode = new PermitNode(root,UD_FAKE_ROOT_SORT_CODE, "/", "自定义权限", PermissionResourceType.Ud);


        Collection<Permission> permissions = rolePermissionResolver.resolvePermissionsInRole(roleCode);
        menuNode.addChilds(getMenuTreeNodes(permissions));
        dataFormNode.addChilds(getDataFormTreeNodes(permissions));
        restNode.addChilds(getWebApiTreeNodes(permissions));
        udNode.addChilds(getUserDefinedPermitTreeNodes(permissions));

        return root;
    }

    @Transactional
    public void updateRoleOwnedPermit(RoleOwnedPermitBean roleOwnedPermitBean) {
        String roleId = roleOwnedPermitBean.getRoleId();
        authService.deleteRoleOwnedPermit(roleId);
        List<PermitNode> permitNodes = roleOwnedPermitBean.getPermits();
        if (null == permitNodes || permitNodes.isEmpty()) return;
        List<RolePermit> rolePermits = constructRoleOwnedPermit(roleId, permitNodes);
        authService.addRoleOwnedPermit(rolePermits);
        applicationEventPublisher.publishEvent(new AuthzCfgModifiedEvent.RolePermissionModifiedEvent(roleId));
    }

    private List<RolePermit> constructRoleOwnedPermit(String roleId, Collection<PermitNode> permitNodes) {
        return permitNodes.stream().map(permitNode -> constructRoleOwnedPermit(roleId, permitNode))
                .collect(Collectors.toList());
    }

    private RolePermit constructRoleOwnedPermit(String roleId, PermitNode permitNode) {
        if ("/".equals(permitNode.getCode()) && (Arrays.binarySearch(FAKE_SORT_CODES, permitNode.getSortCode()) >= 0)) {
            return new RolePermit(roleId, CustomWildcardPermission.constructPermitStr(
                    permitNode.getRw(), permitNode.getType(), "/"));
        }
        return new RolePermit(roleId, CustomWildcardPermission.constructPermitStr(
                permitNode.getRw(), permitNode.getType(), permitNode.getSortCode()));
    }


    private List<PermitNode> getMenuTreeNodes(Collection<Permission> permissions){
        List<MenuEntry> menuItemList = menuService.getAllMenus();
        // can be replaced with sql if not consider wildcard
        authorizationSupport.setPermissionResourceRW(permissions, menuItemList, PermissionResourceType.Menu, false);
        //对菜单模型进行标准化处理后，变成树图结构
        List<PermitNode> nodeList = ListKit.newArrayList();
        for (MenuEntry entry : menuItemList) {
            PermitNode permitNode = new PermitNode(
                    entry.getSortCode(), entry.getId(), entry.getName(), PermissionResourceType.Menu, entry.getRw());
            PermitNode treeNode = new PermitNode(permitNode);
            nodeList.add(treeNode);
        }

        return TreeNodeKit.buildTree(nodeList, PermitNode::getSortCode, "/");
    }

    private List<PermitNode> getDataFormTreeNodes(Collection<Permission> permissions) {
        List<DataForm> dataFormList = dataFormService.getBriefDataFormList();
        if (null == dataFormList || dataFormList.isEmpty()) return Collections.emptyList();
        for(int i=0;i<dataFormList.size();i++){
            DataForm dataForm = dataFormList.get(i);
            if(dataForm == null || dataForm.getId() == null){
                System.out.println(i);
            }
        }

        List<PermitNode> permitNodes = dataFormList.stream()
                .filter(dataForm -> StringKit.isNotBlank(dataForm.getId()))
                .map(dataForm ->
                        new PermitNode(dataForm.getId().replace('-', '/'), dataForm.getId(), dataForm.getName(),PermissionResourceType.DataForm))
                .collect(Collectors.toList());

        dynamicCalculateHierarchy(permitNodes, "/");
        authorizationSupport.setPermissionResourceRW(permissions, permitNodes, PermissionResourceType.DataForm, false);
        return TreeNodeKit.buildTree(permitNodes, PermitNode::getSortCode, "/");
    }

    private List<PermitNode> getWebApiTreeNodes(Collection<Permission> permissions) {
        if (null == urlPermitNodes) {
            synchronized (this) {
                if (null == urlPermitNodes) {
                    urlPermitNodes = new ArrayList<>(128);
                    Map<String, RequestMappingHandlerMapping> beanMap = appContext.getBeansOfType(RequestMappingHandlerMapping.class);
                    beanMap.forEach((name, mappingHandlerMapping) -> {
                        if (Arrays.binarySearch(CANDIDATE_MAPPING_BEANS, name) != -1) {
                            mappingHandlerMapping.getHandlerMethods().forEach((k, v) -> {
                                Set<String> urls = k.getPatternsCondition().getPatterns();
                                urls.forEach(url -> urlPermitNodes.add(new PermitNode(normalizeRestUrl(url), url, url, PermissionResourceType.Rest)));
                            });
                        }
                    });
                    dynamicCalculateHierarchy(urlPermitNodes, "/");
                }
            }
        }
        List<PermitNode> copyUrlPermitNodes = urlPermitNodes.stream().map(PermitNode::new).collect(Collectors.toList());
        authorizationSupport.setPermissionResourceRW(permissions, copyUrlPermitNodes, PermissionResourceType.Rest, false);
        return TreeNodeKit.buildTree(copyUrlPermitNodes, PermitNode::getSortCode, "/");
    }

    private String normalizeRestUrl(String url) {
        if (!url.contains("{")) return url;
        return urlVariablePattern.matcher(url).replaceAll("/*");
    }

    private List<PermitNode> getUserDefinedPermitTreeNodes(Collection<Permission> permissions) {
        List<Permit> permits = authService.getAllUserDefinedPermits();
        if (null == permits || permits.isEmpty())
            return Collections.emptyList();
        List<PermitNode> permitNodes = permits.stream().map(permit ->
                new PermitNode(permit.getCode(), permit.getId(), permit.getName(), PermissionResourceType.Ud))
                .collect(Collectors.toList());
        authorizationSupport.setPermissionResourceRW(permissions, permitNodes, PermissionResourceType.Ud, false);
        return TreeNodeKit.buildTree(permitNodes, PermitNode::getSortCode, "/");
    }


    /**
     *
     * @param permitNodes modify this argument directly
     * @param splitter
     */
    private static void dynamicCalculateHierarchy(List<PermitNode> permitNodes, String splitter) {
        String[] sortCodeArray = permitNodes.stream().map(PermitNode::getSortCode).toArray(String[]::new);
        Set<String> sortCodes = new HashSet<>(sortCodeArray.length * 2);
        PermissionResourceType type = permitNodes.get(0).getType();
        for (String sortCode : sortCodeArray)
            sortCodes.add(sortCode);
        for (String originalSortCode : sortCodeArray) {
            int endIndex = originalSortCode.length() - 1;
            int index = 0;
            while (index < endIndex && (index = originalSortCode.indexOf(splitter, index + 1)) != -1) {
                String dynamicHierarchy = originalSortCode.substring(0, index);
                if (sortCodes.add(dynamicHierarchy)) {
                    permitNodes.add(
                            new PermitNode(dynamicHierarchy, dynamicHierarchy, dynamicHierarchy, type));
                }
            }
        }
    }

}
