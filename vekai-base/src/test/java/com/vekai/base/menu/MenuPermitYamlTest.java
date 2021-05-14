package com.vekai.base.menu;

import com.vekai.base.pilot.model.StartNode;
import com.vekai.base.pilot.service.PilotService;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.lang.MapObject;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

public class MenuPermitYamlTest {
//    @Test
    public void testRead() throws IOException {
        Yaml yaml = new Yaml();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:menu-permit.yaml");
        MapObject root = yaml.loadAs(resource.getInputStream(), MapObject.class);
        System.out.println(JSONKit.toJsonString(root,true));
    }

    @Test
    public void testService() throws IOException {
        PilotService pilotService = new PilotService();
        StartNode startNode = pilotService.getStartNode("classpath:menu-permit.yaml");
        System.out.println("------------123-----------");
        System.out.println(JSONKit.toJsonString(startNode,true));
    }
}
