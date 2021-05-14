package com.vekai.common.autoconfigure;

import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.impl.FileStoreServiceImpl;
import com.vekai.common.fileman.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CommonProperties.class)
@ComponentScan(basePackages={"com.vekai.common"})
public class CommonAutoConfiguration {
    /**
     * 基于本地目录的存储实现
     */
    @Configuration
    @ConditionalOnProperty(prefix = "com.vekai.common", name = "fileStore", havingValue = "LocalDisk", matchIfMissing = true)
    public static class LocalDiskConfiguration {
        @Autowired
        CommonProperties commonProperties;

//        @Bean
//        public FileManService getFileManService(BeanCruder beanCruder, SerialNoGeneratorFinder serialNoGeneratorFinder){
//            FileManServiceImpl fileManService = new FileManServiceImpl();
//            fileManService.setbeanCruder(beanCruder);
//            fileManService.setFileManDao(fileManDao);
//            fileManService.setSerialNoGeneratorFinder(serialNoGeneratorFinder);
//            return fileManService;
//        }

        /**
         * 默认文件存储
         * @return
         */
        @Bean(FileManConsts.STORE_SERVICE_DEFAULT)
        public FileStoreService getDefaultFileStoreService(){
            FileStoreServiceImpl fileStoreService = new FileStoreServiceImpl();
            fileStoreService.setStorageRoot(commonProperties.getFileStorageRoot());
            fileStoreService.setDirectory("/default");
            fileStoreService.setHistDirectory("/default-historic");
            return fileStoreService;
        }

        /**
         * 文档模板
         * @return
         */
        @Bean(FileManConsts.STORE_SERVICE_DOCTPL)
        public FileStoreService getDocTplFileStoreService(){
            FileStoreServiceImpl fileStoreService = new FileStoreServiceImpl();
            fileStoreService.setStorageRoot(commonProperties.getFileStorageRoot());
            fileStoreService.setDirectory("/doctpl");
            fileStoreService.setHistDirectory("/doctpl-historic");
            return fileStoreService;
        }

        /**
         * 格式化报告文档
         * @return
         */
        @Bean(FileManConsts.STORE_SERVICE_FORMATDOC)
        public FileStoreService getFormatdocFileStoreService(){
            FileStoreServiceImpl fileStoreService = new FileStoreServiceImpl();
            fileStoreService.setStorageRoot(commonProperties.getFileStorageRoot());
            fileStoreService.setDirectory("/formatdoc");
            fileStoreService.setHistDirectory("/formatdoc-historic");
            return fileStoreService;
        }

        /**
         * 资源清单附件等
         * @return
         */
        @Bean(FileManConsts.STORE_SERVICE_DOSSIER)
        public FileStoreService getDossierFileStoreService(){
            FileStoreServiceImpl fileStoreService = new FileStoreServiceImpl();
            fileStoreService.setStorageRoot(commonProperties.getFileStorageRoot());
            fileStoreService.setDirectory("/dossier");
            fileStoreService.setHistDirectory("/dossier-historic");
            return fileStoreService;
        }


    }

    /**
     * FastDFS的存储实现
     */
    @ConditionalOnProperty(prefix = "com.vekai.common", name = "fileStore", havingValue = "FastDFS", matchIfMissing = false)
    public static class FastDFSStorageConfiguration {
        @Autowired
        CommonProperties commonProperties;


    }
}
