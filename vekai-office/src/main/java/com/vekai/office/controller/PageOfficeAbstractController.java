package com.vekai.office.controller;

import com.vekai.office.autoconfigure.OfficeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PageOfficeAbstractController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String OFFICE_DEFAULT = "office/pageoffice/OfficeDefault";

    @Autowired
    private OfficeProperties properties;

	public OfficeProperties getProperties() {
		return properties;
	}

	public void setProperties(OfficeProperties properties) {
		this.properties = properties;
	}
    
//	public PageOfficeCtrl createPageOfficeCtrl(){
//		HttpServletRequest request = WebHolder.getRequest();
//    	String staticResourceUrl = properties.getPageOffice().getStaticResourceUrl();
//
//        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
//        poCtrl.setServerPage(request.getContextPath() + staticResourceUrl +"/poserver.zz");//设置服务页面
//        return  poCtrl;
//	}
//
//	public ModelAndView createModelAndView(PageOfficeCtrl poCtrl,String viewName,Map<String, Object> map){
//
//		String title = WebHolder.getRequestParameter("title").strValue("文档编辑");
////		String openWindowURL = PageOfficeLink.openWindow(WebHolder.getRequest(),"http://_REPLACE_","");
//        map.put("pageOfficeHtml", poCtrl.getHtmlCode("PageOfficeCtrl"));
//        map.put("title", title);
//        if(!map.containsKey("banner")){
//		    map.put("banner", "文档处理");
//        }
//
//        ModelAndView mv = new ModelAndView(viewName);
//        return mv;
//
//	}
//
//	public ModelAndView createModelAndView(PageOfficeCtrl poCtrl,String viewName){
//		return createModelAndView(poCtrl,viewName,MapKit.newEmptyMap());
//	}


 

}
