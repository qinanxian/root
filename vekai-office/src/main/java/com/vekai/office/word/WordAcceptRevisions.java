//package com.vekai.office.word;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//
//
//import com.aspose.words.Document;
//import com.aspose.words.DocumentBuilder;
//import com.aspose.words.HeaderFooterType;
//import com.aspose.words.License;
//import com.aspose.words.SaveFormat;
//
//
//public class WordAcceptRevisions {
//	static {
//	 	InputStream license = InstallBookMarkParameter.class.getClassLoader().getResourceAsStream("license.xml");    // license路径
//        License aposeLic = new License();
//        try {
//			aposeLic.setLicense(license);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public void acceptAllRevisions(InputStream fis,String fileName) throws Exception{
//		Document doc = new Document(fis);
//		System.out.println("文件保护状态："+doc.getProtectionType());
////		if(doc.getProtectionType()>0){//判断是否受保护
////			doc.unprotect("amarsoft");
////		}
//		doc.acceptAllRevisions();
//		doc.save(fileName,SaveFormat.DOCX);
//		//doc.save("D:/test.docx");
//	}
//
//}
