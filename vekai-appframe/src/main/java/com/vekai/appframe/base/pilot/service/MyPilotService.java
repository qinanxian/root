package com.vekai.appframe.base.pilot.service;

import com.vekai.auth.entity.RolePermit;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.RoleService;
import com.vekai.base.pilot.model.BaseNode;
import com.vekai.base.pilot.model.MenuNode;
import com.vekai.base.pilot.model.PermitNode;
import com.vekai.base.pilot.model.StartNode;
import com.vekai.base.pilot.service.PilotService;
import cn.fisok.raw.lang.FisokException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyPilotService {
    public static final String PILOT_RESOURCE = "classpath:/app-pilot.yaml";
    private PilotService pilotService = new PilotService();
    @Autowired
    RoleService roleService;

    public StartNode getMyPilot(){
        // 1.获取系统中所有的菜单数据以及权限项
        StartNode startNode = getSysPilot();
        // 2.根据用户规则，过滤掉没有权限的节点项
        String userId = AuthHolder.getUser().getId();
        List<RolePermit> rolePermits = roleService.getUserPermits(userId);

        Set<String> remains =rolePermits
                .stream()
                .map(item -> item.getPermitCode())
                .collect(Collectors.toSet());

        filterNode(startNode.getChildren(),remains);
        return startNode;
    }

    private void filterNode(List<? extends BaseNode> menuNodes, Set<String> remains){
        if(menuNodes == null || menuNodes.size() == 0)return;
        Iterator<? extends BaseNode> iterator = menuNodes.iterator();
        while(iterator.hasNext()){
            BaseNode node = iterator.next();
            if(!remains.contains(node.getPath())){
                iterator.remove();
                menuNodes.remove(node);
            }
        }
        for(BaseNode node : menuNodes){
            if(node instanceof MenuNode){
                List<BaseNode> children  = ((MenuNode)node).getChildren();
                filterNode(children,remains);
            }else if(node instanceof PermitNode){
                List<BaseNode> children  = ((PermitNode)node).getChildren();
                filterNode(children,remains);
            }
        }

    }

    public StartNode getSysPilot(){
        StartNode startNode = null;
        try {
            startNode = pilotService.getStartNode(PILOT_RESOURCE);
        } catch (IOException e) {
            throw new FisokException("读取资源文件出错",e);
        }
        return startNode;
    }
}
