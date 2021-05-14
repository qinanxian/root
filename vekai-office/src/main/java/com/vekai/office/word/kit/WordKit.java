package com.vekai.office.word.kit;


//import com.aspose.words.Document;
//import com.aspose.words.License;
//import com.aspose.words.PdfSaveOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class WordKit {
    private static Logger logger = LoggerFactory.getLogger(WordKit.class);
    private static boolean licenseLoaded = false;
    static{
        loadLicense();
    }
    public static void loadLicense(){
//        if(licenseLoaded)return;
//        InputStream inputStream = WordKit.class.getClassLoader().getResourceAsStream("aspose-license.xml");
//        License asposeLicense = new License();
//        try {
//            asposeLicense.setLicense(inputStream);
//        } catch (Exception e) {
//            logger.error("加载aspose授权文件出错",e);
//        }
        licenseLoaded = true;
    }
    public static void wordToPdf(InputStream inputStream, OutputStream outputStream) throws Exception {
//        Document document = new Document(inputStream);
//        PdfSaveOptions pdfSaveOptions = new PdfSaveOptions();
//        pdfSaveOptions.getOutlineOptions().setHeadingsOutlineLevels(5);
//        document.save(outputStream,pdfSaveOptions);
    }
}
