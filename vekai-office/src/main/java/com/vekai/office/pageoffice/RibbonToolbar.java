//package com.vekai.office.pageoffice;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//public class RibbonToolbar {
//    private String a = "<ribbon startFromScratch=\"true\">\r\n<qat>\r\n <sharedControls>\r\n    <control idMso=\"FileSave\" visible=\"false\"/>\r\n    <control idMso=\"Undo\" visible=\"true\"/>\r\n    <control idMso=\"Redo\" visible=\"true\"/>\r\n </sharedControls>\r\n</qat>\r\n\t<tabs>\r\n\t\t<tab idMso=\"TabHome\" visible=\"true\"><group idMso=\"GroupClipboard\" visible=\"true\"></group></tab>\r\n\t\t<tab idMso=\"TabInsert\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabPageLayoutWord\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabPageLayoutExcel\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabReferences\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabMailings\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabReviewWord\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabFormulas\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabData\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabReview\" visible=\"true\"></tab>\r\n\t\t<tab idMso=\"TabView\" visible=\"true\"></tab>\r\n\t</tabs>\r\n</ribbon>\r\n";
//    private Document b;
//
//    final String a() {
//        ByteArrayOutputStream var1 = new ByteArrayOutputStream();
//        (new h(var1)).a(this.b);
//        String var3 = var1.toString();
//        return h.encodeBase64(("<customUI xmlns=\"http://schemas.microsoft.com/office/2006/01/customui\" onLoad=\"OnRibbonLoaded\">\r\n" + var3 + "</customUI>").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""));
//    }
//
//    RibbonToolbar() {
//        try {
//            ByteArrayInputStream var1 = new ByteArrayInputStream(this.a.getBytes());
//            DocumentBuilder var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            this.b = var2.parse(var1);
//        } catch (Exception var3) {
//            throw new RuntimeException("Failed to decode data from WordDocument object.");
//        }
//    }
//
//    public void setSharedVisible(String var1, boolean var2) {
//        NodeList var3 = this.b.getDocumentElement().getElementsByTagName("control");
//
//        for(int var4 = 0; var4 < var3.getLength(); ++var4) {
//            Element var5;
//            if((var5 = (Element)var3.item(var4)).getAttribute("idMso").equals(var1)) {
//                var5.setAttribute("visible", String.valueOf(var2).toLowerCase());
//            }
//        }
//
//    }
//
//    public void setTabVisible(String var1, boolean var2) {
//        NodeList var3 = this.b.getDocumentElement().getElementsByTagName("tab");
//
//        for(int var4 = 0; var4 < var3.getLength(); ++var4) {
//            Element var5;
//            if((var5 = (Element)var3.item(var4)).getAttribute("idMso").equals(var1)) {
//                var5.setAttribute("visible", String.valueOf(var2).toLowerCase());
//            }
//        }
//
//    }
//
//    public void setGroupVisible(String var1, boolean var2) {
//        NodeList var3 = this.b.getDocumentElement().getElementsByTagName("group");
//
//        for(int var4 = 0; var4 < var3.getLength(); ++var4) {
//            Element var5;
//            if((var5 = (Element)var3.item(var4)).getAttribute("idMso").equals(var1)) {
//                var5.setAttribute("visible", String.valueOf(var2).toLowerCase());
//            }
//        }
//
//    }
//}
