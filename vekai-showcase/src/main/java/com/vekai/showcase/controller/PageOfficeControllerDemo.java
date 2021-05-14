//package com.vekai.showcase.controller;
//
//import com.zhuozhengsoft.pageoffice.FileSaver;
//import com.zhuozhengsoft.pageoffice.OpenModeType;
//import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
//import com.zhuozhengsoft.pageoffice.wordwriter.DataTag;
//import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
//import com.vekai.office.controller.PageOfficeAbstractController;
//import com.vekai.office.pageoffice.PageOfficeCtrl;
//import com.vekai.office.word.kit.WordKit;
//import com.vekai.office.word.kit.WordWatermarkMaker;
//import com.vekai.runtime.holder.WebHolder;
//import com.vekai.runtime.kit.FileKit;
//import com.vekai.runtime.kit.HttpKit;
//import com.vekai.runtime.kit.IOKit;
//import com.vekai.runtime.kit.StringKit;
//import io.swagger.annotations.Api;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URL;
//import java.util.Map;
//
//@Api(value = "PageOfficeController", description = "PageOffice安全相关")
//@RestController
//@RequestMapping(PageOfficeControllerDemo.BASE_URL)
//public class PageOfficeControllerDemo extends PageOfficeAbstractController{
//	public static final String BASE_URL = "/showcase/pageoffice";
//
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//
//    @GetMapping(value = "/index")
//    public ModelAndView showIndex() {
//        ModelAndView mv = new ModelAndView("showcase/pageoffice/Index");
//        return mv;
//    }
//
//    @GetMapping(value = "/word")
//    public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map) {
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//
//        poCtrl.addCustomToolButton("保存", "Save", 1);//添加自定义保存按钮
//        poCtrl.addCustomToolButton("另存为PDF", "SaveAsPDF", 1);//添加自定义另存为PDF按钮
//        poCtrl.addCustomToolButton("打印设置", "PrintSet", 0);
//        poCtrl.addCustomToolButton("打印", "PrintFile", 6);
//        poCtrl.addCustomToolButton("全屏/还原", "IsFullScreen", 4);
//        poCtrl.addCustomToolButton("-", "", 0);
//        poCtrl.addCustomToolButton("关闭", "Close", 21);
//
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL+"/save");//设置处理文件保存的请求方法
//        //打开word
//        //poCtrl.webOpen("D:/Amix/workspace/app/vekai-cabin/amix/src/main/java/com/vekai/showcase/docs/CASE-D10.doc", OpenModeType.docAdmin, "张三");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D10.doc";
//
//        String userName = "小安";
//        poCtrl.webOpen(url, OpenModeType.docAdmin, userName);
//
//        return createModelAndView(poCtrl,"showcase/pageoffice/Word",map);
//    }
//
//    @RequestMapping("/save/{fileName:.+}")
//    public void saveFile(@PathVariable("fileName") String fileName,HttpServletRequest request, HttpServletResponse response) {
//        FileSaver saver = new FileSaver(request, response);
//        String banner = saver.getFormField("banner");
////        long fileSize = saver.getFileSize();
//        System.out.println("--------------------------------------------");
//        System.out.println(banner);
//        System.out.println("--------------------------------------------");
//
//        InputStream inputStream = saver.getFileStream();
//        URL url = this.getClass().getClassLoader().getResource("com/vekai/showcase/docs/"+fileName);
//        String strFile = url.getFile();
//        File file = new File(strFile);
//        OutputStream outputStream = null;
//        try {
//        	outputStream = FileKit.openOutputStream(file);
//        	IOKit.copy(inputStream, outputStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally{
//			IOKit.close(inputStream);
//			IOKit.close(outputStream);
//			saver.close();
//		}
//
//    }
//
//    @GetMapping("/show/{fileName:.+}")
//    public void showFile(@PathVariable("fileName") String fileName,HttpServletResponse response, HttpServletRequest request){
//
//    	InputStream inputStream = null;
//    	URL url = this.getClass().getClassLoader().getResource("com/vekai/showcase/docs/"+fileName);
//		try {
////			inputStream = FileKit.openInputStream(new File("D:/Amix/workspace/app/vekai-cabin/amix/src/main/java/com/vekai/showcase/docs/CASE-D10.doc"));
//			inputStream = url.openStream();
//			HttpKit.renderStream(response,inputStream,"octets/stream",null);
//		} catch (Exception e) {
//			logger.error("打开文件资源失败，请检查文件资源是否存在,URL="+url,e);
//		} finally{
//			IOKit.close(inputStream);
//		}
//
//    }
//
//
//    @GetMapping("/C10WordViewTooSimple")
//    public ModelAndView c10WordViewTooSimple(HttpServletRequest request, Map<String, Object> map,@RequestParam(value="opener",required=false) String opener){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setOfficeToolbars(false);
//        poCtrl.setCustomToolbar(false);
//        poCtrl.setMenubar(false);
//        poCtrl.setTitlebar(false);
//
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D10.doc";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//
//    }
//
//    @GetMapping("/C10WordViewTooSimpleReadOnly")
//    public ModelAndView C10WordViewTooSimpleReadOnly(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setOfficeToolbars(false);
//        poCtrl.setCustomToolbar(false);
//        poCtrl.setMenubar(false);
//        poCtrl.setTitlebar(false);
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D10.doc";
//        poCtrl.webOpen(url, OpenModeType.docReadOnly, userName);
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//
//    }
//
//    @GetMapping("/C11WordToolBar")
//    public ModelAndView C11WordToolBar(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D13.doc");
//
//        poCtrl.setMenubar(false);
//        for(int i=1;i<=21;i++){
//        	poCtrl.addCustomToolButton("图标"+i, "save", i);
//        }
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D13.doc";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//
//    }
//
//    @GetMapping("/C13WordEditAndSave")
//    public ModelAndView C13WordEditAndSave(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D13.doc");
//
//        poCtrl.setMenubar(false);
//        poCtrl.addCustomToolButton("保存", "save", 1);
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D13.doc";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping("/C13WordLocalSaveAndOpen")
//    public ModelAndView C13WordLocalSaveAndOpen(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D13.doc");
//
//        poCtrl.setMenubar(false);
//        poCtrl.addCustomToolButton("打开本地文件", "localOpen", 13);
//        poCtrl.addCustomToolButton("另存本地文件", "localSaveAs", 11);
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D13.doc";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//    @GetMapping("/C14ServerMakeWaterMark")
//    public ModelAndView C14ServerMakeWaterMark(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D10-x.docx");
//
//        poCtrl.setMenubar(false);
//
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/C14ServerMakeWaterMarkShow/CASE-D10-x.docx";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping("/C14ServerMakeWaterMarkShow/{fileName:.+}")
//    public void C14ServerMakeWaterMarkShow(@PathVariable("fileName") String fileName,HttpServletResponse response, HttpServletRequest request){
//
//    	InputStream inputStream = null;
//    	URL url = this.getClass().getClassLoader().getResource("com/vekai/showcase/docs/"+fileName);
//		try {
//			inputStream = url.openStream();
//
//			//打水印
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			WordWatermarkMaker maker = new WordWatermarkMaker(inputStream,outputStream);
//			maker.appendTextWatermark("水印样例");
//			maker.appendImageWatermark(getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/docs/amarsoft-seal-demo.png"));
//			maker.make(0);
//			IOKit.close(inputStream);
//
//			inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//			HttpKit.renderStream(response,inputStream,"octets/stream",null);
//		} catch (Exception e) {
//			logger.error("打开文件资源失败，请检查文件资源是否存在,URL="+url,e);
//		} finally{
//			IOKit.close(inputStream);
//		}
//
//    }
//
//    @GetMapping("/C15ApachePOIWord")
//    public ModelAndView C15ApachePOIWord(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D10.docx");
//
//        poCtrl.setMenubar(false);
//        poCtrl.addCustomToolButton("保存", "save", 1);
//
//        String opener = WebHolder.getRequestParameter("opener").strValue();
//        String userName = StringKit.nvl(opener, "江小安");
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D10.docx";
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, userName);
//
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping(value = "/C16Blank")
//    public ModelAndView C16Blank(HttpServletRequest request, Map<String, Object> map) {
//        ModelAndView mv = new ModelAndView("showcase/pageoffice/Office1");
//        mv.addObject("pageOfficeHtml", "");
//        mv.addObject("title", "空白窗口|"+WebHolder.getRequestParameterForString("banner", ""));
//        return mv;
//    }
//
//    @GetMapping("/C20WordFillTooSimple")
//    public ModelAndView c20WordFillTooSimple(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setOfficeToolbars(false);	//隐藏Office工具条
//    	poCtrl.setCustomToolbar(false);		//隐藏自定义工具栏
//    	poCtrl.setMenubar(false);			//隐藏菜单栏
//    	poCtrl.setTitlebar(false);			//隐藏工标题栏
//
//        WordDocument woDoc = new WordDocument();
//        //设置颜色
//        DataTag deptTag = woDoc.openDataTag("{{出租人}}");
//        deptTag.setValue("示例出租人");
//        deptTag.getFont().setColor(java.awt.Color.BLUE);
//
//        woDoc.openDataTag("{{出租人住所}}").setValue("示例出租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{出租人法定代表人}}").setValue("示例出租人-法定代表人");
//        woDoc.openDataTag("{{出租人联系地址}}").setValue("示例出租人-出租人联系地址");
//
//        DataTag deptTag1 = woDoc.openDataTag("{{承租人}}");
//        deptTag1.setValue("示例承租人");
//        deptTag1.getFont().setColor(java.awt.Color.ORANGE);
//
//        woDoc.openDataTag("{{承租人住所}}").setValue("示例承租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{承租人法定代表人}}").setValue("示例承租人-法定代表人");
//        woDoc.openDataTag("{{承租人联系地址}}").setValue("示例承租人-出租人联系地址");
//
//        poCtrl.setWriter(woDoc);
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D20.doc";
//        poCtrl.webOpen(url, OpenModeType.docReadOnly, "安硕用户");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping("/C23WordViewTooSimpleTpl")
//    public ModelAndView c23WordViewTooSimpleTpl(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setOfficeToolbars(false);	//隐藏Office工具条
//    	poCtrl.setCustomToolbar(false);		//隐藏自定义工具栏
//    	poCtrl.setMenubar(false);			//隐藏菜单栏
//    	poCtrl.setTitlebar(false);			//隐藏工标题栏
//
//    	WordDocument woDoc = new WordDocument();
//        //设置颜色
//        DataTag deptTag = woDoc.openDataTag("{{出租人}}");
//        deptTag.setValue("示例出租人");
//        deptTag.getFont().setColor(java.awt.Color.BLUE);
//
//        woDoc.openDataTag("{{出租人住所}}").setValue("示例出租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{出租人法定代表人}}").setValue("示例出租人-法定代表人");
//        woDoc.openDataTag("{{出租人联系地址}}").setValue("示例出租人-出租人联系地址");
//
//        DataTag deptTag1 = woDoc.openDataTag("{{承租人}}");
//        deptTag1.setValue("示例承租人");
//        deptTag1.getFont().setColor(java.awt.Color.ORANGE);
//
//        woDoc.openDataTag("{{承租人住所}}").setValue("示例承租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{承租人法定代表人}}").setValue("示例承租人-法定代表人");
//        woDoc.openDataTag("{{承租人联系地址}}").setValue("示例承租人-出租人联系地址");
//
//        //打开数据区域，openDataRegion方法的参数代表Word文档中的书签名称
//        woDoc.openDataRegion("PO_LeaseHoldLocation").setEditing(true);
//
//        poCtrl.setWriter(woDoc);
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D23.doc";
//        poCtrl.webOpen(url, OpenModeType.docSubmitForm, "安硕用户");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping("/C26WordFillAndDataFetch")
//    public ModelAndView c26WordFillAndDataFetch(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D26.doc");
//    	poCtrl.setSaveDataPage(request.getContextPath() + BASE_URL + "/D26Fetch");
//    	poCtrl.setMenubar(false);
//    	poCtrl.setCustomToolbar(true);		//隐藏自定义工具栏
//    	poCtrl.addCustomToolButton("保存","save",1);	//添加自定义按钮
//
//    	WordDocument woDoc = new WordDocument();
//        //设置颜色
//        DataTag deptTag = woDoc.openDataTag("{{出租人}}");
//        deptTag.setValue("示例出租人");
//        deptTag.getFont().setColor(java.awt.Color.BLUE);
//
//        woDoc.openDataTag("{{出租人住所}}").setValue("示例出租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{出租人法定代表人}}").setValue("示例出租人-法定代表人");
//        woDoc.openDataTag("{{出租人联系地址}}").setValue("示例出租人-出租人联系地址");
//
//        DataTag deptTag1 = woDoc.openDataTag("{{承租人}}");
//        deptTag1.setValue("示例承租人");
//        deptTag1.getFont().setColor(java.awt.Color.ORANGE);
//
//        woDoc.openDataTag("{{承租人住所}}").setValue("示例承租人-地址-上海陆嘴");
//        woDoc.openDataTag("{{承租人法定代表人}}").setValue("示例承租人-法定代表人");
//        woDoc.openDataTag("{{承租人联系地址}}").setValue("示例承租人-出租人联系地址");
//
//     	//打开数据区域，openDataRegion方法的参数代表Word文档中的书签名称
//        woDoc.openDataRegion("PO_LeaseePostNo").setEditing(true);
//        woDoc.openDataRegion("PO_LeaseePhoneNo").setEditing(true);
//        woDoc.openDataRegion("PO_LeaseeFaxNo").setEditing(true);
//        woDoc.openDataRegion("PO_LeaseHoldLocation").setEditing(true);
//
//        poCtrl.setWriter(woDoc);
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D26.doc";
//        poCtrl.webOpen(url, OpenModeType.docSubmitForm, "安硕用户");
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping("/C27WordMergeMain")
//    public ModelAndView c27WordMergeMain(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setOfficeToolbars(false);	//隐藏Office工具条
//    	poCtrl.setCustomToolbar(false);		//隐藏自定义工具栏
//    	poCtrl.setMenubar(false);			//隐藏菜单栏
//    	poCtrl.setTitlebar(false);			//隐藏工标题栏
//
//    	WordDocument woDoc = new WordDocument();
//
//    	DataRegion data1 = woDoc.openDataRegion("PO_p1");
//    	data1.setValue("[word]"+request.getContextPath() + BASE_URL + "/show/D27-1.doc[/word]");
//    	DataRegion data2 = woDoc.openDataRegion("PO_p2");
//    	data2.setValue("[word]"+request.getContextPath() + BASE_URL + "/show/D27-2.doc[/word]");
//    	DataRegion data3 = woDoc.openDataRegion("PO_p3");
//    	data3.setValue("[word]"+request.getContextPath() + BASE_URL + "/show/D27-3.doc[/word]");
//
//        poCtrl.setWriter(woDoc);
//        String url = request.getContextPath() + BASE_URL + "/show/D27-Main.docx";
//        poCtrl.webOpen(url, OpenModeType.docSubmitForm, "安硕用户");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//    @GetMapping("/C28TaoHong")
//    public ModelAndView c28TaoHong(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setOfficeToolbars(false);	//隐藏Office工具条
//        poCtrl.setCustomToolbar(false);		//隐藏自定义工具栏
//        poCtrl.setMenubar(false);			//隐藏菜单栏
//        poCtrl.setTitlebar(false);			//隐藏工标题栏
//
//        // 填充数据和正文内容到“zhengshi.doc”
//        WordDocument woDoc = new WordDocument();
//        DataRegion copies = woDoc.openDataRegion("PO_Copies");
//        copies.setValue("6");
//
//        DataRegion docNum = woDoc.openDataRegion("PO_DocNum");
//        docNum.setValue("001");
//
//        DataRegion issueDate = woDoc.openDataRegion("PO_IssueDate");
//        issueDate.setValue("2018-11-21");
//
//        DataRegion issueDept = woDoc.openDataRegion("PO_IssueDept");
//        issueDept.setValue("技术研发部");
//
//        DataRegion sTextS = woDoc.openDataRegion("PO_STextS");
////        sTextS.setValue("[word]doc/test.doc[/word]");
//        sTextS.setValue("[word]"+request.getContextPath() + BASE_URL + "/show/D28-Content.doc[/word]");
//
//        DataRegion sTitle = woDoc.openDataRegion("PO_sTitle");
//        sTitle.setValue("苏州安硕数科红头文件");
//
//        DataRegion topicWords = woDoc.openDataRegion("PO_TopicWords");
//        topicWords.setValue("AmixOffice、 套红");
//
//        poCtrl.setWriter(woDoc);
//        String url = request.getContextPath() + BASE_URL + "/show/D28-RedHeader.doc";
//        poCtrl.webOpen(url, OpenModeType.docSubmitForm, "安硕用户");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//    @GetMapping("/C30WordCompare")
//    public ModelAndView c30WordCompare(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setCustomToolbar(true);		//隐藏自定义工具栏
//        poCtrl.setMenubar(false);
//
//        poCtrl.addCustomToolButton("显示A文档", "showDocR1", 0);
//        poCtrl.addCustomToolButton("显示B文档", "showDocR2", 0);
//        poCtrl.addCustomToolButton("显示比较结果", "showDocCompare", 0);
//
//        String prePath = request.getContextPath() + BASE_URL;
//        poCtrl.wordCompare(prePath+"/show/CASE-D30-R1.doc", prePath+"/show/CASE-D30-R2.doc", OpenModeType.docAdmin, "安硕用户");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office5",map);
//    }
//
//    @GetMapping("/C40WordImport")
//    public ModelAndView c40WordImport(HttpServletRequest request, Map<String, Object> map){
//        PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setCustomToolbar(true);		//隐藏自定义工具栏
//        poCtrl.setMenubar(false);
//
//        poCtrl.addCustomToolButton("导入文件", "importData()", 5);
//        poCtrl.addCustomToolButton("提交数据", "submitData()", 1);
//
//        poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D40.doc");
//    	poCtrl.setSaveDataPage(request.getContextPath() + BASE_URL + "/D40Fetch");
//
//        WordDocument doc = new WordDocument();
//        poCtrl.setWriter(doc);
//
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D40.doc";
//        map.put("impFlag", "true");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office5",map);
//    }
//
//    @GetMapping("/C50RevisionModelEdit")
//    public ModelAndView c50RevisionModelEdit(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setMenubar(false);					//隐藏菜单栏
//    	poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D50.docx");
//
//    	//添加自定义按钮
//    	poCtrl.setCustomMenuCaption("自定义菜单");
//    	poCtrl.addCustomToolButton("保存","save",1);
//    	poCtrl.addCustomToolButton("显示隐藏痕迹", "toggleRevisions",2);
//    	poCtrl.addCustomToolButton("圈阅", "openHandDraw",7);
//    	poCtrl.addCustomToolButton("分层显示圈阅", "showHandDraw", 8);
//    	poCtrl.addCustomToolButton("全屏/还原", "fullScreen", 9);
//
//
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D50.docx";
//        poCtrl.webOpen(url, OpenModeType.docRevisionOnly, "莎莎");
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//    @GetMapping("/C50RevisionModelEdit1")
//    public ModelAndView c50RevisionModelEdit1(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setMenubar(false);					//隐藏菜单栏
//    	poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D50.docx");
//
//    	//添加自定义按钮
//    	poCtrl.setCustomMenuCaption("自定义菜单");
//    	poCtrl.addCustomToolButton("保存","save",1);
//    	poCtrl.addCustomToolButton("显示隐藏痕迹", "toggleRevisions",2);
//    	poCtrl.addCustomToolButton("圈阅", "openHandDraw",7);
//    	poCtrl.addCustomToolButton("分层显示圈阅", "showHandDraw", 8);
//    	poCtrl.addCustomToolButton("全屏/还原", "fullScreen", 9);
//
//
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D50.docx";
//        poCtrl.webOpen(url, OpenModeType.docRevisionOnly, "小丽");
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//    @GetMapping("/C51Finalise")
//    public ModelAndView c51Finalise(HttpServletRequest request, Map<String, Object> map){
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//    	poCtrl.setTitlebar(true);          //隐藏标题栏
//    	poCtrl.setCustomToolbar(true);     //隐藏自定义工具栏
//    	poCtrl.setOfficeToolbars(false);    //隐藏Office工具条
//    	poCtrl.setMenubar(false);           //隐藏菜单栏
//    	poCtrl.setSaveFilePage(request.getContextPath() + BASE_URL + "/save/CASE-D50.docx");
//
//    	//添加自定义按钮
//    	poCtrl.addCustomToolButton("显示隐藏痕迹", "toggleRevisions",2);
//    	poCtrl.addCustomToolButton("圈阅", "openHandDraw", 6);
//    	poCtrl.addCustomToolButton("分层显示圈阅", "showHandDraw", 7);
//    	poCtrl.addCustomToolButton("接受所有修订", "acceptAllRevisions", 8);
//    	poCtrl.addCustomToolButton("全屏/还原", "fullScreen", 9);
//
//
//        String url = request.getContextPath() + BASE_URL + "/show/CASE-D50.docx";
//        poCtrl.webOpen(url, OpenModeType.docRevisionOnly, "老王");
//        map.put("banner", "冒着敌人的炮火前进");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office2",map);
//    }
//
//
//    @GetMapping("/C56WordTwoDocs")
//    public ModelAndView c56WordTwoDocs(HttpServletRequest request, Map<String, Object> map){
//
//    	String url1=request.getContextPath() + BASE_URL + "/show/CASE-D56-1.docx";
//    	String url2=request.getContextPath() + BASE_URL + "/show/CASE-D56-2.docx";
//
//    	PageOfficeCtrl poCtrl1 = createPageOfficeCtrl();
//    	poCtrl1.setCaption("安硕文档编辑器1");								//设置PageOfficeCtrl控件的标题栏名称
//    	poCtrl1.setMenubar(false);					//隐藏菜单栏
//    	poCtrl1.setSaveFilePage(url1);		//设置执行保存页面
//    	poCtrl1.setJsFunction_AfterDocumentOpened("onDocument1Opened()");
//    	poCtrl1.setTagId("PageOfficeCtrl1");
//    	map.put("pageOfficeHtml1", poCtrl1.getHtmlCode("PageOfficeCtrl1"));
//
//    	PageOfficeCtrl poCtrl2 = createPageOfficeCtrl();
//    	poCtrl2.setCaption("安硕文档编辑器2");								//设置PageOfficeCtrl控件的标题栏名称
//    	poCtrl2.setMenubar(false);					//隐藏菜单栏
//    	poCtrl2.setSaveFilePage(url2);		//设置执行保存页面
//    	poCtrl2.setTagId("PageOfficeCtrl2");
//    	map.put("pageOfficeHtml2", poCtrl2.getHtmlCode("PageOfficeCtrl2"));
//
//        map.put("openURL1", url1);
//        map.put("openURL2", url2);
//        ModelAndView mv = new ModelAndView("showcase/pageoffice/Office3");
//        return mv;
//    }
//
//    @GetMapping("/C56WordTwoDocsInIframe")
//    public ModelAndView c56WordTwoDocsInIframe(HttpServletRequest request, Map<String, Object> map){
//    	map.put("host", request.getHeader("Host"));
//        ModelAndView mv = new ModelAndView("showcase/pageoffice/Office4");
//        return mv;
//    }
//
//    @GetMapping("/C56SubPage1")
//    public ModelAndView c56SubPage1(HttpServletRequest request, Map<String, Object> map){
//    	String url = request.getContextPath() + BASE_URL + "/show/CASE-D56-1.docx";
//
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(url);
//        poCtrl.setMenubar(false);
//
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, "安硕用户1");
//
//        map.put("openURL1", url);
//        return createModelAndView(poCtrl,"showcase/pageoffice/c56subpages/SubPage1",map);
//    }
//
//    @GetMapping("/C56SubPage2")
//    public ModelAndView c56SubPage2(HttpServletRequest request, Map<String, Object> map){
//    	String url = request.getContextPath() + BASE_URL + "/show/CASE-D56-2.docx";
//
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(url);
//        poCtrl.setMenubar(false);
//
//        poCtrl.webOpen(url, OpenModeType.docNormalEdit, "安硕用户2");
//
//        map.put("openURL2", url);
//        return createModelAndView(poCtrl,"showcase/pageoffice/c56subpages/SubPage2",map);
//    }
//
//    @GetMapping("/C60ExcelEditAndSave")
//    public ModelAndView c60ExcelEditAndSave(HttpServletRequest request, Map<String, Object> map){
//    	String url = request.getContextPath() + BASE_URL + "/show/CASE-D60.xlsx";
//
//    	PageOfficeCtrl poCtrl = createPageOfficeCtrl();
//        poCtrl.setSaveFilePage(url);
//        poCtrl.setCustomToolbar(false);
//        poCtrl.setCaption("安硕文档编辑器");
//        poCtrl.setMenubar(false);
//
//        poCtrl.webOpen(url, OpenModeType.xlsNormalEdit, "安硕用户");
//        map.put("banner", "aaaa");
//        return createModelAndView(poCtrl,"showcase/pageoffice/Office1",map);
//    }
//
//    @GetMapping(value = "/showWord2PDF")
//    public void showWord2PDF(HttpServletRequest request, HttpServletResponse response) {
//        InputStream inputStream = null;
//        URL url = this.getClass().getClassLoader().getResource("com/vekai/showcase/docs/CASE-D13.doc");
//        try {
//            inputStream = url.openStream();
//            //1.转PDF
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            outputStream = new ByteArrayOutputStream();
//            WordKit.wordToPdf(inputStream,outputStream);
//            //3.输出流转输入流
//            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//
//            HttpKit.renderStream(response,inputStream,"octets/stream",null);
//        } catch (Exception e) {
//            logger.error("打开文件资源失败，请检查文件资源是否存在,URL="+url,e);
//        } finally{
//            IOKit.close(inputStream);
//        }
//    }
//
//    @GetMapping(value = "/showWord2PDFWithWaterMark")
//    public void showWord2PDFWithWaterMark(HttpServletRequest request, HttpServletResponse response) {
//        InputStream inputStream = null;
//        URL url = this.getClass().getClassLoader().getResource("com/vekai/showcase/docs/CASE-D13.doc");
//        try {
//            inputStream = url.openStream();
//            //1.打水印
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            WordWatermarkMaker maker = new WordWatermarkMaker(inputStream,outputStream);
//            maker.appendTextWatermark("水印样例");
//            maker.appendImageWatermark(getClass().getClassLoader().getResourceAsStream("com/vekai/showcase/docs/amarsoft-seal-demo.png"));
//            maker.make(0);
//            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//            IOKit.close(inputStream);
//            IOKit.close(outputStream);
//            //2.转PDF
//            outputStream = new ByteArrayOutputStream();
//            WordKit.wordToPdf(inputStream,outputStream);
//            //3.输出流转输入流
//            inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//            HttpKit.renderStream(response,inputStream,"octets/stream",null);
//        } catch (Exception e) {
//            logger.error("打开文件资源失败，请检查文件资源是否存在,URL="+url,e);
//        } finally{
//            IOKit.close(inputStream);
//        }
//    }
//
//    @PostMapping("/D26Fetch")
//    public void d26Fetch(HttpServletRequest req,HttpServletResponse rep){
//    	com.zhuozhengsoft.pageoffice.wordreader.WordDocument doc = new com.zhuozhengsoft.pageoffice.wordreader.WordDocument(req, rep);
//        String LeaseePostNo = doc.openDataRegion("PO_LeaseePostNo").getValue();
//        String leaseePhoneNo = doc.openDataRegion("PO_LeaseePhoneNo").getValue();
//        String leaseeFaxNo= doc.openDataRegion("PO_LeaseeFaxNo").getValue();
//        String leaseHoldLocation= doc.openDataRegion("PO_LeaseHoldLocation").getValue();
//
//        System.out.println("leaseePostNo:"+LeaseePostNo);
//        System.out.println("leaseePhoneNo:"+leaseePhoneNo);
//        System.out.println("leaseeFaxNo:"+leaseeFaxNo);
//        System.out.println("leaseHoldLocation:"+leaseHoldLocation);
//
//        doc.close();
//    }
//    @PostMapping("/D40Fetch")
//    public void d40Fetch(HttpServletRequest req,HttpServletResponse rep){
//    	com.zhuozhengsoft.pageoffice.wordreader.WordDocument doc = new com.zhuozhengsoft.pageoffice.wordreader.WordDocument(req, rep);
//        String PO_cause = doc.openDataRegion("PO_cause").getValue();
//        String PO_date = doc.openDataRegion("PO_date").getValue();
//        String PO_dept= doc.openDataRegion("PO_dept").getValue();
//        String PO_name= doc.openDataRegion("PO_name").getValue();
//        String PO_num= doc.openDataRegion("PO_num").getValue();
//
//        System.out.println("PO_cause:"+PO_cause);
//        System.out.println("PO_date:"+PO_date);
//        System.out.println("PO_dept:"+PO_dept);
//        System.out.println("PO_name:"+PO_name);
//        System.out.println("PO_num:"+PO_num);
//
//        doc.close();
//    }
//
//
//}
