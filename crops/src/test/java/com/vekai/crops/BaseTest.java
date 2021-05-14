package com.vekai.crops;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.autoconfigure.SerialNumberProperties;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.PaginResult;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import cn.fisok.web.holder.WebHolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.vekai.crops.base.SerialNoGeneratorFiller;
import com.vekai.crops.business.entity.BusinessApplyPO;
import com.vekai.crops.customer.entity.CustomerInfoPO;
//import com.vekai.crops.util.FileBase64Kit;
import com.vekai.crops.util.JsonUtil;
import com.vekai.crops.util.mongo3.DBEntry;
import com.vekai.crops.util.mongo3.MongoDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.websocket.DeploymentException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = {Application.class})
//@WebAppConfiguration
//@WebAppConfiguration("src/main/webapp")
@ActiveProfiles({"dev"})
public class BaseTest {

    @Value("${server.contextPath}")
    protected String contextPath;

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationConnect;
//    @Autowired
//    protected MockHttpSession session;
//    @Autowired
//    protected MockHttpServletRequest request;
    @Value("${mongo.config.fileName}")
    public String mongoConfig;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private SerialNumberGeneratorFinder finder;
    @Autowired
    private SqloyProperties properties;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
        MockServletContext servletContext = new MockServletContext() {
            public void addEndpoint(Class<?> endpointClass) throws DeploymentException {
            }
        };
        servletContext.setContextPath(contextPath);
        WebHolder.setServletContext(servletContext);
    }

    @Test
    public void basePing() {
        Assert.assertNotNull(contextPath);
    }

    @Test
    public void test01() throws Exception{
        MongoDB.init(mongoConfig);
        JsonNode customer_info = DBEntry.findOneJsonByFilter("customer_info", "{'_id':'4ec27f2301ac4a8da7b39779d7a2d4ad'}", null);
//        System.out.println(JsonUtil.getJsonStringValue(customer_info,"object.a"));
//        JsonNode[] customer_infos = DBEntry.findJsonArrayByFilter("customer_info", null, null);
//        System.out.println(customer_infos.length);
//        JsonNode pdf_info = JsonUtil.queryJson(customer_info,"customer_info.o1");
        String pdfIds = JsonUtil.getJsonStringName(JsonUtil.queryJson(customer_info, "esign_info.i"));
        String[] ids = pdfIds.split(",");
        String pdfId = JsonUtil.getJsonStringValue(customer_info,"esign_info.i."+ids[ids.length-1]+".j");
        System.out.println(pdfId);
        GridFSFile oneGridFSByID = DBEntry.findOneGridFSByID(pdfId, null);
        String filename = oneGridFSByID.getFilename();
        FileOutputStream fos = new FileOutputStream("/Users/shibin/Downloads/nfs_share/"+filename);
        System.out.println(filename);
        DBEntry.downloadGridFS2StreamByID(pdfId,fos);
        File file = new File("/Users/shibin/Downloads/nfs_share/"+filename);
        System.out.println(file.length());
//        DBEntry.
        fos.flush();
        fos.close();


        //JsonNode pdfFile = DBEntry.findOneJsonByFilter("fs.chunks", null, null);
        //String pdfBase64 = JsonUtil.getJsonStringValue(JsonUtil.queryJson(pdfFile,"data"),"$binary");
        //File file = FileBase64Kit.convertBase64ToFile(pdfBase64, pdfId + ".pdf", "/Users/shibin/Downloads");
    }

    @Test
    public void test02() throws Exception{
        PaginResult<BusinessApplyPO> businessApplyPOPaginationData =
                beanCruder.selectListPagination(BusinessApplyPO.class, "select * from business_apply",
                        MapKit.mapOf("id","0001"), 1, 5);
        List<BusinessApplyPO> dataList = businessApplyPOPaginationData.getDataList();
        System.out.println(dataList.size());
    }

    @Test
    public void test03() throws Exception{
//        SerialNumberGenerator<String> serialNumberGenerator = finder.find("com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType.id");
//        String next = serialNumberGenerator.next("com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType.id");
//        System.out.println(next);
//        System.out.println(properties.getSerialNumber().getSnCursorRecordType());
//        System.out.println(properties.getSerialNumber().getGeneratorMap().get("com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType.id"));
//        System.out.println(properties.getSerialNumber().getTemplateMap().get("com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType.id"));

        String defaultGenerator = properties.getSerialNumber().getDefaultGenerator();
        System.out.println(defaultGenerator);
        SerialNumberProperties serialNumber = new SerialNumberProperties();
        String defaultGenerator1 = serialNumber.getDefaultGenerator();
        System.out.println(defaultGenerator1);
        String defaultTemplate1 = serialNumber.getDefaultTemplate();
        System.out.println(defaultTemplate1);
        String defaultTemplate = properties.getSerialNumber().getDefaultTemplate();
        System.out.println(defaultTemplate);

    }
}
