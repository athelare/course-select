package com.ljy.courseselect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {
    @RequestMapping("/select")
    public String selectPage(HttpServletRequest request){
        if(request.getSession().getAttribute("loginUser") != null){
            return "select";
        }else return "nologin";
    }

}
