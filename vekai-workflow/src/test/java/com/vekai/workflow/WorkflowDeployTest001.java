package com.vekai.workflow;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.*;


public class WorkflowDeployTest001 extends BaseTest {
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected NamedParameterJdbcTemplate parameterJdbcTemplate;
    @Autowired
    protected SqlSessionFactory sqlSessionFactory;
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected RuntimeService runtimeService;

    /**
     * 流程部署
     */
    @Test
    public void deployBPMN(){
        // 流程部署绝对位置
        String rootPath = ClassUtils.getDefaultClassLoader().getResource("").getPath().replaceAll("test-classes","classes");
        Assert.assertNotNull(rootPath);
        File file = new File(rootPath+"/bpmn/parallelgateway.bpmn20.xml");
        Assert.assertTrue(file.exists());
        InputStream input = null;
        try {
            input = new FileInputStream(file);

            Assert.assertNotNull(repositoryService);
            Deployment deploy = repositoryService.createDeployment().addInputStream(file.getName(),input).deploy();
            Assert.assertNotNull(deploy.getId());

            System.out.println("流程部署ID:"+deploy.getId());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * 输出部署流程图到文件
     */
    @Test
    public void genDeployPicture(){
        Assert.assertNotNull(repositoryService);
        String depolyId = "2501";
        String resourceName = "parallelgateway.parallelgatewayTest01.png";

        InputStream ins = repositoryService.getResourceAsStream(depolyId, resourceName);

        File picFile = new File("/"+resourceName);
        try {
            OutputStream os = new FileOutputStream(picFile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
