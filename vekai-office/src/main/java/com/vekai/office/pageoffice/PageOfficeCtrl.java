////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.vekai.office.pageoffice;
//
//import com.zhuozhengsoft.pageoffice.BorderStyleType;
//import com.zhuozhengsoft.pageoffice.DocumentVersion;
//import com.zhuozhengsoft.pageoffice.OfficeVendorType;
//import com.zhuozhengsoft.pageoffice.OpenModeType;
//import com.zhuozhengsoft.pageoffice.PoSysInfo;
//import com.zhuozhengsoft.pageoffice.ThemeType;
//import com.zhuozhengsoft.pageoffice.excelwriter.Workbook;
//import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
//import java.awt.Color;
//import java.io.*;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//public class PageOfficeCtrl {
//    private Document document;
//    private Element b;
//    private Element c;
//    private HttpServletRequest request;
//    private String objectId;
//    private String caption;
//    private String serverPage;
//    private BorderStyleType borderStyleType;
//    private Color borderColor;
//    private ThemeType theme;
//    private boolean titlebar;
//    private Color titlebarColor;
//    private Color titlebarTextColor;
//    private boolean menubar;
//    private Color menubarColor;
//    private Color menubarTextColor;
//    private boolean customToolbar;
//    private boolean officeToolbars;
//    private boolean allowCopy;
//    private boolean disableCopyOnly;
//    private String fileTitle;
//    private String saveFilePage;
//    private String saveDataPage;
//    private int saveFileMaxSize;
//    private boolean compressDocument;
//    private String httpBasic_userName;
//    private String httpBasic_password;
//    private int timeSlice;
//    private String C;
//    private String D;
//    private String E;
//    private String F;
//    private String G;
//    private String H;
//    private String I;
//    private String customMenuCaption;
//    private String customRibbon;
//    private String protectPassword;
//    private OfficeVendorType officeVendorType;
//    private boolean N;
//    private String O;
//    private boolean visible;
//    private String ocxVersion;
//    private String zoomSealServer;
//    private boolean enableUserProtection;
//    private RibbonToolbar ribbonToolbar;
//    private String setupTips;
//
//    public PageOfficeCtrl(HttpServletRequest request) {
//        this.request = request;
//        this.objectId = "";
//        this.borderStyleType = BorderStyleType.BorderFlat;
//        this.borderColor = null;
//        this.theme = ThemeType.Office2007;
//        this.titlebar = true;
//        this.titlebarColor = null;
//        this.titlebarTextColor = null;
//        this.menubar = true;
//        this.menubarColor = null;
//        this.menubarTextColor = null;
//        this.customToolbar = true;
//        this.officeToolbars = true;
//        this.allowCopy = true;
//        this.disableCopyOnly = false;
//        this.caption = "";
//        this.fileTitle = "";
//        this.serverPage = "";
//        this.saveFilePage = "";
//        this.saveDataPage = "";
//        this.saveFileMaxSize = 0;
//        this.compressDocument = false;
//        this.httpBasic_userName = "";
//        this.httpBasic_password = "";
//        this.timeSlice = 0;
//        this.C = "";
//        this.D = "";
//        this.E = "";
//        this.F = "";
//        this.G = "";
//        this.H = "";
//        this.I = "";
//        this.b = null;
//        this.c = null;
//        this.customMenuCaption = "";
//        this.officeVendorType = OfficeVendorType.MSOffice;
//        this.customRibbon = "";
//        this.protectPassword = "";
//        this.visible = true;
//        this.ocxVersion = "";
//        this.zoomSealServer = "";
//        this.enableUserProtection = false;
//        this.ribbonToolbar = null;
//        this.setupTips = "";
//        this.N = false;
//        this.O = "";
//
//        try {
//            String var4 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PageOfficeCtrl></PageOfficeCtrl>";
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(var4.getBytes());
//            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            this.document = documentBuilder.parse(byteArrayInputStream);
//        } catch (Exception ex) {
//            ;
//        }
//    }
//
//    private static String getOcxVersion(InputStream inputStream) {
//        byte[] var1 = new byte[]{(byte)86, (byte)0, (byte)101, (byte)0, (byte)114, (byte)0, (byte)115, (byte)0, (byte)105, (byte)0, (byte)111, (byte)0, (byte)110, (byte)0, (byte)58, (byte)0, (byte)32, (byte)0, (byte)66, (byte)0, (byte)117, (byte)0, (byte)105, (byte)0, (byte)108, (byte)0, (byte)100, (byte)0, (byte)32, (byte)0};
//        byte[] var2 = new byte[]{(byte)-95, (byte)32, (byte)-128, (byte)30};
//        String var3 = "";
//        try {
//            long length = (long) inputStream.available();
//            inputStream.skip((long) ((int) length - 6000));
//            boolean var9 = false;
//            byte[] var5 = new byte[6000];
//            int var8 = inputStream.read(var5, 0, 5990);
//
//            for (int var6 = 0; var6 < var8; ++var6) {
//                int var7 = 0;
//
//                while (var5[var6] == var1[var7]) {
//                    ++var7;
//                    ++var6;
//                    if (var7 == 29) {
//                        for (var3 = ""; var5[var6] != var2[0]; ++var6) {
//                            if (var5[var6] != 32 && var5[var6] != 0) {
//                                var3 = var3 + (char) var5[var6];
//                            }
//                        }
//                        var9 = true;
//                        break;
//                    }
//                }
//
//                if (var9) {
//                    break;
//                }
//            }
//
//            return var3.replace(".", ",");
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    public void setTagId(String var1) {
//        this.request.setAttribute("PO_" + var1, this.getHtmlCode(var1));
//    }
//
//    public String getHtmlCode(String objectId) {
//        this.objectId = objectId;
//        if(this.serverPage.equals("")) {
//            throw new RuntimeException("在调用 getCtrlCode 方法之前，您必须调用 setServerPage 方法设置 ServerPage 属性。 ");
//        } else {
//            if((objectId = this.request.getServletContext().getInitParameter("posetup_downloadurl")) != null && !objectId.equals("")) {
//
//            } else {
//                objectId = this.request.getScheme() + "://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath() + "/posetup.exe";
//            }
//
//            InputStream posetupExeStream;
//            if((posetupExeStream = this.getClass().getResourceAsStream("/com/zhuozhengsoft/pageoffice/poserver/posetup.exe")) != null) {
//                this.ocxVersion = getOcxVersion(posetupExeStream);
//                try {
//                    posetupExeStream.close();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                if(this.ocxVersion.equals("")) {
//                    this.ocxVersion = "0,0,0,0";
//                }
//
//                this.setupDom(this.document);
//                ByteArrayOutputStream var7 = new ByteArrayOutputStream();
//                (new h(var7)).a(this.document);
//                String var8 = var7.toString();
//                var8 = "<input type=\"hidden\" name=\"__VIEWSTATEPOCTRL\" id=\"__VIEWSTATEPOCTRL\" value=\"" + h.c(var8) + "\" />" + this.O + "\r\n\r\n";
//                if(this.request.getRequestURL().toString().endsWith("/")) {
//                    var8 = var8 + "<div align=center style=\"color:red;\">为了能够正确显示 PageOfficeCtrl 控件，当前页面的 URL 不能以 \"/\" 字符结尾。</div>\r\n";
//                } else {
//                    String var3;
//                    if(this.visible) {
//                        var3 = "height=\"100%\" width=\"100%\"";
//                    } else {
//                        var3 = "height=\"0\" width=\"0\"";
//                    }
//
//                    boolean var4;
//                    if(this.request.getHeader("User-Agent").indexOf("MSIE") <= 0 && this.request.getHeader("User-Agent").indexOf("Trident") <= 0) {
//                        var4 = false;
//                    } else {
//                        var4 = true;
//                    }
//
//                    if(this.setupTips.equals("")) {
//                        if(var4) {
//                            this.setupTips = "<div align=center style=\"color:red;font-size:14px;font-family:微软雅黑;\"><a href=\"" + objectId + "\" style=\"text-decoration:none;height: 30px;padding-top: 0px;border: 1px solid #AECDE7;border-top: 1px solid #AECDE7;border-left: 1px solid #AECDE7;background-color: #EEEEEE;\">&nbsp;&nbsp;请点此安装 PageOffice 控件&nbsp;&nbsp;</a><br />安装完毕后请重启浏览器浏览本页即可。如果出现黄色提示条，请点击允许运行。</div>";
//                        } else {
//                            this.setupTips = "<div align=center style=\"color:red;font-size:14px;font-family:微软雅黑;\"><a href=\"" + objectId + "\" style=\"text-decoration:none;height: 30px;padding-top: 0px;border: 1px solid #AECDE7;border-top: 1px solid #AECDE7;border-left: 1px solid #AECDE7;background-color: #EEEEEE;\">&nbsp;&nbsp;请点此安装 PageOffice 控件&nbsp;&nbsp;</a><br />安装完毕后请重启浏览器浏览本页即可。</div>";
//                        }
//                    } else {
//                        this.setupTips = this.setupTips.replace("<a href=\"posetup.exe\">", "<a href=\"" + objectId + "\">");
//                    }
//
//                    if(var4) {
//                        var8 = var8 + "<object id=\"" + this.objectId + "\" " + var3 + " classid=\"clsid:F2852C85-C2FC-4c86-8D6B-E4E97C92F821\">" + this.setupTips + "\r\n";
//                        var8 = var8 + "<!--Server-Version=" + PoSysInfo.getServerVersion() + " OCX-Version=" + this.ocxVersion + "-->";
//                        var8 = var8 + "</object>\r\n";
//                    } else {
//                        boolean var9 = true;
//                        String var10;
//                        String var5 = var10 = this.request.getHeader("User-Agent");
//                        boolean var6 = false;
//                        if(var5.toLowerCase().indexOf("chrome") > 0 && Integer.parseInt((var5 = var5.substring(var5.toLowerCase().indexOf("chrome/") + 7)).substring(0, var5.indexOf(46))) >= 42) {
//                            var6 = true;
//                        }
//
//                        if(var6) {
//                            if(var10.toLowerCase().indexOf("edge/") > 0) {
//                                var9 = false;
//                                var8 = var8 + "<p style=\'color:#FF0000;\'>加载PageOffice控件失败。<br/>当前浏览器是Edge浏览器，建议采用POBrowser技术打开PageOffice即可。</p>";
//                            } else if(var10.substring(var10.toLowerCase().indexOf("safari/")).indexOf(" ") < 0) {
//                                var9 = false;
//                                var8 = var8 + "<p style=\'color:#FF0000;\'>加载PageOffice控件失败。<br/>当前浏览器是42版本以上的谷歌浏览器，建议采用POBrowser技术打开PageOffice即可。</p>";
//                            }
//                        }
//
//                        var6 = false;
//                        if(var10.toLowerCase().indexOf("firefox") > 0 && Integer.parseInt((var5 = var10.substring(var10.toLowerCase().indexOf("firefox/") + 8)).substring(0, var5.indexOf(46))) >= 52) {
//                            var6 = true;
//                        }
//
//                        if(var6) {
//                            var9 = false;
//                            var8 = var8 + "<p style=\'color:#FF0000;\'>加载PageOffice控件失败。<br/>当前浏览器是Firefox浏览器，建议采用POBrowser技术打开PageOffice即可。</p>";
//                        }
//
//                        if(var9) {
//                            var8 = var8 + "<object id=\"" + this.objectId + "\" " + var3 + " type=\"application/x-pageoffice-plugin\" clsid=\"{F2852C85-C2FC-4c86-8D6B-E4E97C92F821}\">" + this.setupTips + "\r\n";
//                            var8 = var8 + "<!--Server-Version=" + PoSysInfo.getServerVersion() + " OCX-Version=" + this.ocxVersion + "-->";
//                            var8 = var8 + "</object>\r\n";
//                        }
//                    }
//                }
//
//                return var8 + "&nbsp;";
//            } else {
//                throw new RuntimeException("文件 posetup.exe不存在，PageOffice 无法运行。请确保jar文件没有被破坏。");
//            }
//        }
//    }
//
//    public void setBorderStyle(BorderStyleType borderStyleType) {
//        this.borderStyleType = borderStyleType;
//    }
//
//    public void setBorderColor(Color borderColor) {
//        this.borderColor = borderColor;
//    }
//
//    public void setTheme(ThemeType theme) {
//        this.theme = theme;
//    }
//
//    public void setTitlebar(boolean titlebar) {
//        this.titlebar = titlebar;
//    }
//
//    public void setTitlebarColor(Color titlebarColor) {
//        this.titlebarColor = titlebarColor;
//    }
//
//    public void setTitlebarTextColor(Color color) {
//        this.titlebarTextColor = color;
//    }
//
//    public void setMenubar(boolean menubar) {
//        this.menubar = menubar;
//    }
//
//    public void setMenubarColor(Color menubarColor) {
//        this.menubarColor = menubarColor;
//    }
//
//    public void setMenubarTextColor(Color color) {
//        this.menubarTextColor = color;
//    }
//
//    public void setCustomToolbar(boolean customToolbar) {
//        this.customToolbar = customToolbar;
//    }
//
//    public void setOfficeToolbars(boolean officeToolbars) {
//        this.officeToolbars = officeToolbars;
//    }
//
//    public void setCaption(String caption) {
//        this.caption = caption;
//    }
//
//    public void setAllowCopy(boolean allowCopy) {
//        this.allowCopy = allowCopy;
//    }
//
//    public void setDisableCopyOnly(boolean disableCopyOnly) {
//        this.disableCopyOnly = disableCopyOnly;
//    }
//
//    public void setFileTitle(String fileTitle) {
//        this.fileTitle = fileTitle;
//    }
//
//    public void setServerPage(String serverPage) {
//        this.serverPage = serverPage;
//    }
//
//    public void setSaveFilePage(String saveFilePage) {
//        this.saveFilePage = saveFilePage;
//    }
//
//    public void setSaveDataPage(String saveDataPage) {
//        this.saveDataPage = saveDataPage;
//    }
//
//    public void setSaveFileMaxSize(int saveFileMaxSize) {
//        this.saveFileMaxSize = saveFileMaxSize;
//    }
//
//    public void setCompressDocument(boolean compressDocument) {
//        this.compressDocument = compressDocument;
//    }
//
//    public void setHTTPBasic_UserName(String httpBasic_userName) {
//        this.httpBasic_userName = httpBasic_userName;
//    }
//
//    public void setHTTPBasic_Password(String httpBasic_password) {
//        this.httpBasic_password = httpBasic_password;
//    }
//
//    public void setTimeSlice(int timeSlice) {
//        this.timeSlice = timeSlice;
//    }
//
//    public void setJsFunction_AfterDocumentOpened(String var1) {
//        this.C = var1;
//    }
//
//    public void setJsFunction_AfterDocumentClosed(String var1) {
//        this.D = var1;
//    }
//
//    public void setJsFunction_AfterDocumentSaved(String var1) {
//        this.E = var1;
//    }
//
//    public void setJsFunction_BeforeDocumentClosed(String var1) {
//        this.F = var1;
//    }
//
//    public void setJsFunction_BeforeDocumentSaved(String var1) {
//        this.G = var1;
//    }
//
//    public void setJsFunction_OnWordDataRegionClick(String var1) {
//        this.H = var1;
//    }
//
//    public void setJsFunction_OnExcelCellClick(String var1) {
//        this.I = var1;
//    }
//
//    public void setCustomMenuCaption(String customMenuCaption) {
//        this.customMenuCaption = customMenuCaption;
//    }
//
//    public void setCustomRibbon(String customRibbon) {
//        this.customRibbon = customRibbon;
//    }
//
//    public void setProtectPassword(String protectPassword) {
//        this.protectPassword = protectPassword;
//    }
//
//    public void setOfficeVendor(OfficeVendorType officeVendorType) {
//        this.officeVendorType = officeVendorType;
//    }
//
//    public void setVisible(boolean visible) {
//        this.visible = visible;
//    }
//
//    public void setZoomSealServer(String zoomSealServer) {
//        this.zoomSealServer = zoomSealServer;
//    }
//
//    public void setEnableUserProtection(boolean enableUserProtection) {
//        this.enableUserProtection = enableUserProtection;
//    }
//
//    public RibbonToolbar getRibbonBar() {
//        if(this.ribbonToolbar == null) {
//            this.ribbonToolbar = new RibbonToolbar();
//        }
//
//        return this.ribbonToolbar;
//    }
//
//    public void setSetupTips(String setupTips) {
//        this.setupTips = setupTips;
//    }
//
//    private static int color2Int(Color var0) {
//        return var0.getRed() + 256 * var0.getGreen() + 65536 * var0.getBlue();
//    }
//
//    private void setupDom(Document document) {
//        Element documentElement = document.getDocumentElement();
//        Element childElement;
//        if(this.borderStyleType != BorderStyleType.BorderFlat) {
//            (childElement = document.createElement("BorderStyle")).appendChild(document.createTextNode(String.valueOf(this.borderStyleType.ordinal())));
//            documentElement.appendChild(childElement);
//        }
//
//        int colorInt;
//        if(this.borderColor != null) {
//            colorInt = color2Int(this.borderColor);
//            (childElement = document.createElement("BorderColor")).appendChild(document.createTextNode(String.valueOf(colorInt)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.theme != ThemeType.Office2007) {
//            String var10 = "1";
//            switch(f.a[this.theme.ordinal()]) {
//                case 1:
//                    var10 = "0";
//                    break;
//                case 2:
//                    var10 = "1";
//                    break;
//                case 3:
//                    var10 = "2";
//            }
//
//            Element var8;
//            (var8 = document.createElement("Theme")).appendChild(document.createTextNode(var10));
//            documentElement.appendChild(var8);
//        }
//
//        if(!this.titlebar) {
//            (childElement = document.createElement("Titlebar")).appendChild(document.createTextNode("0"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.titlebarColor != null) {
//            colorInt = color2Int(this.titlebarColor);
//            (childElement = document.createElement("TitlebarColor")).appendChild(document.createTextNode(String.valueOf(colorInt)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.titlebarTextColor != null) {
//            colorInt = color2Int(this.titlebarTextColor);
//            (childElement = document.createElement("TitlebarTextColor")).appendChild(document.createTextNode(String.valueOf(colorInt)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.menubar) {
//            (childElement = document.createElement("Menubar")).appendChild(document.createTextNode("0"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.menubarColor != null) {
//            colorInt = color2Int(this.menubarColor);
//            (childElement = document.createElement("MenubarColor")).appendChild(document.createTextNode(String.valueOf(colorInt)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.menubarTextColor != null) {
//            colorInt = color2Int(this.menubarTextColor);
//            (childElement = document.createElement("MenubarTextColor")).appendChild(document.createTextNode(String.valueOf(colorInt)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.customToolbar) {
//            (childElement = document.createElement("CustomToolbar")).appendChild(document.createTextNode("0"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.officeToolbars) {
//            (childElement = document.createElement("OfficeToolbars")).appendChild(document.createTextNode("0"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.allowCopy) {
//            (childElement = document.createElement("AllowCopy")).appendChild(document.createTextNode("0"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.disableCopyOnly) {
//            (childElement = document.createElement("DisableCopyOnly")).appendChild(document.createTextNode("1"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.caption.equals("")) {
//            (childElement = document.createElement("Caption")).appendChild(document.createTextNode(h.encodeBase64(this.caption)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.fileTitle.equals("")) {
//            (childElement = document.createElement("FileTitle")).appendChild(document.createTextNode(h.encodeBase64(this.fileTitle)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.serverPage.equals("")) {
//            (childElement = document.createElement("ServerPage")).appendChild(document.createTextNode(this.serverPage));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.saveFilePage.equals("")) {
//            (childElement = document.createElement("SaveFilePage")).appendChild(document.createTextNode(this.saveFilePage));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.saveDataPage.equals("")) {
//            (childElement = document.createElement("SaveDataPage")).appendChild(document.createTextNode(this.saveDataPage));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.saveFileMaxSize > 0) {
//            (childElement = document.createElement("SaveFileMaxSize")).appendChild(document.createTextNode(String.valueOf(this.saveFileMaxSize)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.compressDocument) {
//            (childElement = document.createElement("CompressDocument")).appendChild(document.createTextNode("1"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.httpBasic_userName.equals("")) {
//            (childElement = document.createElement("HTTPBasic_UserName")).appendChild(document.createTextNode(this.httpBasic_userName));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.httpBasic_password.equals("")) {
//            (childElement = document.createElement("HTTPBasic_Password")).appendChild(document.createTextNode(this.httpBasic_password));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.timeSlice > 0) {
//            (childElement = document.createElement("TimeSlice")).appendChild(document.createTextNode(String.valueOf(this.timeSlice)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.C.equals("")) {
//            (childElement = document.createElement("JsFunction_AfterDocumentOpened")).appendChild(document.createTextNode(this.C));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.D.equals("")) {
//            (childElement = document.createElement("JsFunction_AfterDocumentClosed")).appendChild(document.createTextNode(this.D));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.E.equals("")) {
//            (childElement = document.createElement("JsFunction_AfterDocumentSaved")).appendChild(document.createTextNode(this.E));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.F.equals("")) {
//            (childElement = document.createElement("JsFunction_BeforeDocumentClosed")).appendChild(document.createTextNode(this.F));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.G.equals("")) {
//            (childElement = document.createElement("JsFunction_BeforeDocumentSaved")).appendChild(document.createTextNode(this.G));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.H.equals("")) {
//            (childElement = document.createElement("JsFunction_OnWordDataRegionClick")).appendChild(document.createTextNode(this.H));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.I.equals("")) {
//            (childElement = document.createElement("JsFunction_OnExcelCellClick")).appendChild(document.createTextNode(this.I));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.customMenuCaption.equals("")) {
//            (childElement = document.createElement("CustomMenuCaption")).appendChild(document.createTextNode(h.encodeBase64(this.customMenuCaption)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.customRibbon.equals("")) {
//            (childElement = document.createElement("CustomRibbon")).appendChild(document.createTextNode(h.encodeBase64(this.customRibbon)));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.protectPassword.equals("")) {
//            (childElement = document.createElement("ProtectPassword")).appendChild(document.createTextNode(this.protectPassword));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.officeVendorType != OfficeVendorType.MSOffice) {
//            (childElement = document.createElement("OfficeVendor")).appendChild(document.createTextNode(String.valueOf(this.officeVendorType.ordinal())));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.ocxVersion.equals("")) {
//            (childElement = document.createElement("NewOCXVersion")).appendChild(document.createTextNode(this.ocxVersion));
//            documentElement.appendChild(childElement);
//        }
//
//        if(!this.zoomSealServer.equals("")) {
//            (childElement = document.createElement("ZoomSealServer")).appendChild(document.createTextNode(this.zoomSealServer));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.enableUserProtection) {
//            (childElement = document.createElement("EnableUserProtection")).appendChild(document.createTextNode("1"));
//            documentElement.appendChild(childElement);
//        }
//
//        if(this.ribbonToolbar != null) {
//            (childElement = document.createElement("RibbonEncodeXML")).appendChild(document.createTextNode(this.ribbonToolbar.a()));
//            documentElement.appendChild(childElement);
//        }
//
//        Cookie[] var12;
//        if(this.request != null && (var12 = this.request.getCookies()) != null) {
//            String var9 = "";
//
//            for(int var5 = 0; var5 < var12.length; ++var5) {
//                Cookie var6 = var12[var5];
//                var9 = var9 + var6.getName() + "=" + var6.getValue() + ";";
//            }
//
//            if(var9 != "") {
//                Element var11;
//                (var11 = document.createElement("CookiesHttpOnly")).appendChild(document.createTextNode(var9));
//                documentElement.appendChild(var11);
//            }
//        }
//
//    }
//
//    public void addCustomMenuItem(String var1, String var2, boolean var3) {
//        if(this.b == null) {
//            this.b = this.document.createElement("CustomMenuItems");
//            this.document.getDocumentElement().appendChild(this.b);
//        }
//
//        Element var4;
//        (var4 = this.document.createElement("MenuItem")).setAttribute("Caption", h.encodeBase64(var1));
//        var4.setAttribute("JsFunction", String.valueOf(var2));
//        var4.setAttribute("Enabled", var3?"1":"0");
//        this.b.appendChild(var4);
//    }
//
//    public void addCustomToolButton(String var1, String var2, int var3) {
//        if(this.c == null) {
//            this.c = this.document.createElement("CustomToolButtons");
//            this.document.getDocumentElement().appendChild(this.c);
//        }
//
//        Element var4;
//        (var4 = this.document.createElement("ButtonItem")).setAttribute("Caption", h.encodeBase64(var1));
//        var4.setAttribute("JsFunction", String.valueOf(var2));
//        var4.setAttribute("IconIndex", String.valueOf(var3));
//        this.c.appendChild(var4);
//    }
//
//    public void setWriter(Object var1) {
//        String var2;
//        if((var2 = var1.getClass().getName()) == "com.zhuozhengsoft.pageoffice.excelwriter.Workbook") {
//            this.O = ((Workbook)var1).toString("3D44AFF7A708");
//        } else if(var2 == "com.zhuozhengsoft.pageoffice.wordwriter.WordDocument") {
//            this.O = ((WordDocument)var1).toString("3D44AFF7A708");
//        } else {
//            throw new RuntimeException("setWriter 方法不支持 \"" + var1.getClass().getName() + "\" 类型。");
//        }
//    }
//
//    public void webOpen(String filePath, OpenModeType openModeType, String var3) {
//        if(filePath != null && !filePath.equals("")) {
//            if(this.serverPage.equals("")) {
//                throw new RuntimeException("在调用 setTagId 方法之前，您必须调用 setServerPage 方法设置 ServerPage 属性。");
//            } else {
//                int index;
//                if((index = filePath.indexOf(":\\")) < 0) {
//                    index = filePath.indexOf("file://");
//                }
//
//                boolean isRelative; // todo
//                String fileExt;
//                if(index >= 0) {
//                    isRelative = false;
//                    String file = filePath;
//                    if(filePath.indexOf("file://") >= 0) {
//                        file = filePath.substring(7);
//                    }
//
//                    if(!(new File(file)).exists()) {
//                        throw new RuntimeException("服务器磁盘文件 \"" + file + "\" 不存在。");
//                    }
//
//                    if(!(fileExt = h.d(file).toLowerCase()).equals(".doc") && !fileExt.equals(".docx") && !fileExt.equals(".rtf") && !fileExt.equals(".wps") && !fileExt.equals(".xls") && !fileExt.equals(".xlsx") && !fileExt.equals(".et") && !fileExt.equals(".ppt") && !fileExt.equals(".pptx") && !fileExt.equals(".xml") && !fileExt.equals(".vsd") && !fileExt.equals(".mpp")) {
//                        throw new RuntimeException("当前待打开的服务器磁盘文档\"" + file + "\" 的文件扩展名尚不支持。");
//                    }
//                } else {
//                    isRelative = true;
//                }
//
//                if(!filePath.toLowerCase().endsWith(".doc") && !filePath.toLowerCase().endsWith(".docx") && !filePath.toLowerCase().endsWith(".rtf") && !filePath.toLowerCase().endsWith(".wps")) {
//                    if(!filePath.toLowerCase().endsWith(".xls") && !filePath.toLowerCase().endsWith(".xlsx") && !filePath.toLowerCase().endsWith(".et")) {
//                        if(!filePath.toLowerCase().endsWith(".ppt") && !filePath.toLowerCase().endsWith(".pptx")) {
//                            if(filePath.toLowerCase().endsWith(".vsd")) {
//                                if(openModeType != OpenModeType.vsdNormalEdit) {
//                                    throw new RuntimeException("请确保 OpenMode 和 \"" + filePath + "\" 的文档类型完全一致。");
//                                }
//                            } else if(filePath.toLowerCase().endsWith(".mpp") && openModeType != OpenModeType.mppNormalEdit) {
//                                throw new RuntimeException("请确保 OpenMode 和 \"" + filePath + "\" 的文档类型完全一致。");
//                            }
//                        } else if(openModeType != OpenModeType.pptNormalEdit && openModeType != OpenModeType.pptReadOnly) {
//                            throw new RuntimeException("请确保 OpenMode 和 \"" + filePath + "\" 的文档类型完全一致。");
//                        }
//                    } else if(openModeType != OpenModeType.xlsNormalEdit && openModeType != OpenModeType.xlsReadOnly && openModeType != OpenModeType.xlsSubmitForm) {
//                        throw new RuntimeException("请确保 OpenMode 和 \"" + filePath + "\" 的文档类型完全一致。");
//                    }
//                } else if(openModeType != OpenModeType.docAdmin && openModeType != OpenModeType.docRevisionOnly && openModeType != OpenModeType.docNormalEdit && openModeType != OpenModeType.docReadOnly && openModeType != OpenModeType.docSubmitForm && openModeType != OpenModeType.docHandwritingOnly) {
//                    throw new RuntimeException("请确保 OpenMode 和 \"" + filePath + "\" 的文档类型完全一致。");
//                }
//
//                if(!this.N) {
//                    Element webOpenElement = this.document.createElement("WebOpen");
//                    if(isRelative) {
//                        webOpenElement.setAttribute("FileName", filePath);
//                    } else {
//                        fileExt = filePath;
//                        String fileName;
//                        if(filePath.indexOf("file://") >= 0) {
//                            fileName = (fileExt = filePath.substring(7)).substring(fileExt.lastIndexOf("/") + 1);
//                        } else {
//                            fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
//                        }
//
//                        filePath = h.c("file=" + fileExt + "&contenttype=application/octet-stream&filename=" + fileName).replace("+", "-A").replace("/", "-S").replace("=", "-X");
//                        webOpenElement.setAttribute("FileName", this.serverPage + "?pgop=opendiskdoc&id=" + filePath);
//                    }
//
//                    webOpenElement.setAttribute("OpenMode", String.valueOf(openModeType));
//                    webOpenElement.setAttribute("UserName", h.encodeBase64(var3));
//                    this.document.getDocumentElement().appendChild(webOpenElement);
//                    this.N = true;
//                }
//            }
//        } else {
//            throw new RuntimeException("documentURL 参数不能为空，必须传值。");
//        }
//    }
//
//    public void webCreateNew(String var1, DocumentVersion var2) {
//        if(this.serverPage.equals("")) {
//            throw new RuntimeException("在调用 setTagId 方法之前，您必须调用 setServerPage 方法设置 ServerPage 属性。");
//        } else if(!this.N) {
//            Element var3 = this.document.createElement("WebOpen");
//            String var4 = "file001.doc";
//            OpenModeType var5 = OpenModeType.docNormalEdit;
//            String var6 = "newword.doc";
//            String var7 = "application/msword";
//            switch(f.b[var2.ordinal()]) {
//                case 1:
//                    var5 = OpenModeType.docNormalEdit;
//                    var4 = "file001.doc";
//                    var6 = "newword.doc";
//                    var7 = "application/msword";
//                    break;
//                case 2:
//                    var5 = OpenModeType.xlsNormalEdit;
//                    var4 = "file002.xls";
//                    var6 = "newexcel.xls";
//                    var7 = "application/x-excel";
//                    break;
//                case 3:
//                    var5 = OpenModeType.pptNormalEdit;
//                    var4 = "file003.ppt";
//                    var6 = "newppt.ppt";
//                    var7 = "application/ms-powerpoint";
//                    break;
//                case 4:
//                    var5 = OpenModeType.docNormalEdit;
//                    var4 = "file004.docx";
//                    var6 = "newword.docx";
//                    var7 = "application/msword";
//                    break;
//                case 5:
//                    var5 = OpenModeType.xlsNormalEdit;
//                    var4 = "file005.xlsx";
//                    var6 = "newexcel.xlsx";
//                    var7 = "application/x-excel";
//                    break;
//                case 6:
//                    var5 = OpenModeType.pptNormalEdit;
//                    var4 = "file006.pptx";
//                    var6 = "newppt.pptx";
//                    var7 = "application/ms-powerpoint";
//            }
//
//            String var8 = h.c("file=" + var4 + "&contenttype=" + var7 + "&filename=" + var6).replace("+", "-A").replace("/", "-S").replace("=", "-X");
//            var3.setAttribute("FileName", this.serverPage + "?pgop=createnewdoc&id=" + var8);
//            var3.setAttribute("OpenMode", String.valueOf(var5));
//            var3.setAttribute("UserName", h.encodeBase64(var1));
//            this.document.getDocumentElement().appendChild(var3);
//            this.N = true;
//        }
//    }
//
//    public void wordCompare(String var1, String var2, OpenModeType var3, String var4) {
//        if(var1 != null && !var1.equals("")) {
//            if(var2 != null && !var2.equals("")) {
//                if(var3 != OpenModeType.docAdmin && var3 != OpenModeType.docReadOnly) {
//                    throw new RuntimeException("这里的 openMode 只有 docAdmin 和 docReadOnly 有效。并且请确保要打开的文档是Word文档。");
//                } else if(this.serverPage.equals("")) {
//                    throw new RuntimeException("在调用 setTagId 方法之前，您必须调用 setServerPage 方法设置 ServerPage 属性。");
//                } else {
//                    int var5;
//                    if((var5 = var1.indexOf(":\\")) < 0) {
//                        var5 = var1.indexOf("file://");
//                    }
//
//                    boolean var6;
//                    String var10;
//                    if(var5 >= 0) {
//                        var6 = false;
//                        String var7 = var1;
//                        if(var1.indexOf("file://") >= 0) {
//                            var7 = var1.substring(7);
//                        }
//
//                        if(!(new File(var7)).exists()) {
//                            throw new RuntimeException("服务器磁盘文件 \"" + var7 + "\" 不存在。");
//                        }
//
//                        if(!(var10 = h.d(var7).toLowerCase()).equals(".doc") && !var10.equals(".docx")) {
//                            throw new RuntimeException("当前待打开的服务器磁盘文档\"" + var7 + "\" 的文件扩展名不是Word类型。");
//                        }
//                    } else {
//                        var6 = true;
//                    }
//
//                    if((var5 = var2.indexOf(":\\")) < 0) {
//                        var5 = var2.indexOf("file://");
//                    }
//
//                    String var9;
//                    boolean var11;
//                    if(var5 >= 0) {
//                        var11 = false;
//                        String var8 = var2;
//                        if(var2.indexOf("file://") >= 0) {
//                            var8 = var2.substring(7);
//                        }
//
//                        if(!(new File(var8)).exists()) {
//                            throw new RuntimeException("服务器磁盘文件 \"" + var8 + "\" 不存在。");
//                        }
//
//                        if(!(var9 = h.d(var8).toLowerCase()).equals(".doc") && !var9.equals(".docx")) {
//                            throw new RuntimeException("当前待打开的服务器磁盘文档\"" + var8 + "\" 的文件扩展名不是Word类型。");
//                        }
//                    } else {
//                        var11 = true;
//                    }
//
//                    if(!this.N) {
//                        Element var12 = this.document.createElement("WordCompare");
//                        if(var6) {
//                            var12.setAttribute("FileName", var1);
//                        } else {
//                            var9 = var1;
//                            if(var1.indexOf("file://") >= 0) {
//                                var10 = (var9 = var1.substring(7)).substring(var9.lastIndexOf("/") + 1);
//                            } else {
//                                var10 = var1.substring(var1.lastIndexOf("\\") + 1);
//                            }
//
//                            var1 = h.c("file=" + var9 + "&contenttype=application/octet-stream&filename=" + var10).replace("+", "-A").replace("/", "-S").replace("=", "-X");
//                            var12.setAttribute("FileName", this.serverPage + "?pgop=opendiskdoc&id=" + var1);
//                        }
//
//                        if(var11) {
//                            var12.setAttribute("FileName2", var2);
//                        } else {
//                            var9 = var2;
//                            if(var2.indexOf("file://") >= 0) {
//                                var10 = (var9 = var2.substring(7)).substring(var9.lastIndexOf("/") + 1);
//                            } else {
//                                var10 = var2.substring(var2.lastIndexOf("\\") + 1);
//                            }
//
//                            var1 = h.c("file=" + var9 + "&contenttype=application/octet-stream&filename=" + var10).replace("+", "-A").replace("/", "-S").replace("=", "-X");
//                            var12.setAttribute("FileName2", this.serverPage + "?pgop=opendiskdoc&id=" + var1);
//                        }
//
//                        var12.setAttribute("OpenMode", String.valueOf(var3));
//                        var12.setAttribute("UserName", h.encodeBase64(var4));
//                        this.document.getDocumentElement().appendChild(var12);
//                        this.N = true;
//                    }
//                }
//            } else {
//                throw new RuntimeException("documentURL2 参数不能为空，必须传值。");
//            }
//        } else {
//            throw new RuntimeException("documentURL 参数不能为空，必须传值。");
//        }
//    }
//}
//
