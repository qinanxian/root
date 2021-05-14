//package com.vekai.dataform.controller;
//
//
//import com.vekai.dataform.service.JsonDBTransService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Profile;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/devtool")
//@Profile("dev")
//public class JsonDBTransController {
//
//    @Autowired
//    JsonDBTransService jsonDBTransService;
//
//    @PostMapping("/export2Json")
//    public void export2Json(HttpServletRequest request) throws IOException{
//        jsonDBTransService.export2Json(request.getContextPath());
//    }
//
//    @PostMapping("/importFromJson")
//    public void import2DB(HttpServletRequest request) throws IOException {
//        jsonDBTransService.import2DB(request.getContextPath());
//    }
//
//}
