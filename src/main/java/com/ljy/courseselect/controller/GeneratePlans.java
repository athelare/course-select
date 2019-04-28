package com.ljy.courseselect.controller;

import com.ljy.courseselect.pojo.SelectResult;
import com.ljy.courseselect.service.CourseSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GeneratePlans {
    @Autowired
    CourseSelectService courseSelectService;

    @RequestMapping("/generate")
    public String Generate(@RequestParam List<String> courseId,
                           @RequestParam List<String> lessonId,
                           @RequestParam List<Long> busyTime,
                           Model md){
        SelectResult res = courseSelectService.GeneratePlans(courseId,lessonId,busyTime);
        md.addAttribute("results",res);
        return "showPlan";
    }
}
