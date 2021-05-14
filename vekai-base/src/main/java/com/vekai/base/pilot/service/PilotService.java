package com.vekai.base.pilot.service;

import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.MapObject;
import com.vekai.base.pilot.model.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PilotService {
    public StartNode getStartNode(String location) throws IOException {
        //加载YAML资源
        Yaml yaml = new Yaml();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(location);
        MapObject root = yaml.loadAs(resource.getInputStream(), MapObject.class);

        //拼装节点
        //根节点
        StartNode startNode = new StartNode();
        BeanKit.copyProperties(root, startNode);
        startNode.setChildren(null);
        //菜单节点
        List<LinkedHashMap> mapList = (List<LinkedHashMap>) root.get("children");
        List<MenuNode> menuNodes = new ArrayList<MenuNode>(mapList.size());
        mapList.stream()
                .filter(mapItem -> "menu".equals(mapItem.get("type")))    //过滤掉根节点以下，type不是menu的节点
                .forEach(mapItem -> {
                    //递归构建节点
                    MenuNode node = (MenuNode)buildMenuNodeWithRecursion(mapItem);
                    //放回容器
                    menuNodes.add(node);
                });
        startNode.setChildren(menuNodes);
        //处理一级菜单及以后的节点

        return startNode;
    }

    protected BaseNode buildMenuNodeWithRecursion(LinkedHashMap mapNode) {
        BaseNode retNode = null;
        String type = StringKit.nvl(mapNode.get("type"),"");
        NodeType nodeType = NodeType.valueOf(type);

        if(NodeType.menu == nodeType){
            MenuNode menuNode = new MenuNode();
            BeanKit.copyProperties(mapNode, menuNode);
            menuNode.setChildren(null);

            //处理子节点
            List<LinkedHashMap> childMapList = (List<LinkedHashMap>) mapNode.get("children");
            if(childMapList==null||childMapList.size()==0)return menuNode;
            List<BaseNode> childBeanNodeList = new ArrayList<BaseNode>(childMapList.size());
            childMapList.forEach(childMapNode->{
                BaseNode node = buildMenuNodeWithRecursion(childMapNode);
                childBeanNodeList.add(node);
            });
            menuNode.setChildren(childBeanNodeList);

            retNode = menuNode;
        }else if(NodeType.datascope == nodeType||NodeType.action == nodeType){
            PermitNode permitNode = new PermitNode();
            BeanKit.copyProperties(mapNode, permitNode);

            //处理子节点
            List<LinkedHashMap> childMapList = (List<LinkedHashMap>) mapNode.get("children");
            if(childMapList==null||childMapList.size()==0)return permitNode;

            List<BaseNode> childBeanNodeList = new ArrayList<BaseNode>(childMapList.size());
            childMapList.forEach(childMapNode->{
                BaseNode node = buildMenuNodeWithRecursion(childMapNode);
                childBeanNodeList.add(node);
            });
            permitNode.setChildren(childBeanNodeList);

            retNode = permitNode;
        }

        return retNode;
    }
}
