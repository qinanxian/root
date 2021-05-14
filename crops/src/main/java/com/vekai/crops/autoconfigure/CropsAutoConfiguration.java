package com.vekai.crops.autoconfigure;

import com.vekai.crops.common.constant.FileCmonConsts;
import com.vekai.crops.util.mongo3.MongoDB;
import com.vekai.common.autoconfigure.CommonProperties;
import com.vekai.common.fileman.impl.FileStoreServiceImpl;
import com.vekai.common.fileman.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableConfigurationProperties(CropsProperties.class)
@ComponentScan(basePackages={"com.vekai.crops"})
public class CropsAutoConfiguration {

    private CropsProperties properties;
    @Autowired
    CommonProperties commonProperties;
    @Value("${mongo.config.fileName}")
    public String mongoConfig;

    public CropsAutoConfiguration(CropsProperties properties) {
        this.properties = properties;
    }
    @Bean
    @ConditionalOnProperty(prefix = "com.vekai.crops", name = "enableWebsocker", havingValue = "true",matchIfMissing = true)
    public ServerEndpointExporter serverEndpointExporter() {
        try {
//            MongoDB.init(mongoConfig);
        } catch (Exception e) {
            throw new RuntimeException("mongodb init fail!");
        }
        return new ServerEndpointExporter();
    }

    /**
     * Kettle文件保存
     * @return
     */
    @Bean(FileCmonConsts.STORE_SERVICE_KETTLE)
    public FileStoreService getKettleFileStoreService(){
        FileStoreServiceImpl fileStoreService = new FileStoreServiceImpl();
        fileStoreService.setStorageRoot(commonProperties.getFileStorageRoot());
        fileStoreService.setDirectory("/kettle");
        fileStoreService.setHistDirectory("/kettle-historic");
        return fileStoreService;
    }

}
