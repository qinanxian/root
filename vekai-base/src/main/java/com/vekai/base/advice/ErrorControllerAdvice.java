package com.vekai.base.advice;

import cn.fisok.raw.lang.FisokException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ErrorControllerAdvice {
    public static final String HTTP_500_VIEW = "base/500";
    public static final String HTTP_404_VIEW = "base/404";

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        if(e instanceof FisokException){
            return notExistErrorHandler(request,response,e);
        }
        mav.addObject("e", e);
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("request", request);
        mav.addObject("response", response);
        mav.addObject("response", response);
        mav.addObject("statusText", "");
        mav.setViewName(HTTP_500_VIEW);
        return mav;
    }

    @ExceptionHandler(value = FisokException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView notExistErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {

        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ModelAndView mav = new ModelAndView();
        mav.addObject("e", e);
        mav.addObject("errorMessage", e.getMessage());
        mav.addObject("request", request);
        mav.addObject("response", response);
        mav.addObject("statusText", httpStatus.getReasonPhrase());
        mav.setViewName(HTTP_404_VIEW);

        return mav;
    }
}
