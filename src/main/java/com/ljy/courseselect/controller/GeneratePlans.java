package com.ljy.courseselect.controller;

import com.ljy.courseselect.domain.LessonEntity;
import com.ljy.courseselect.pojo.SelectPlan;
import com.ljy.courseselect.pojo.SelectResult;
import com.ljy.courseselect.service.CourseSelectService;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GeneratePlans {
    @Autowired
    CourseSelectService courseSelectService;

    @RequestMapping("/generate")
    public String Generate(@RequestParam List<String> courseId,@RequestParam List<String> lessonId){
        SelectResult res = courseSelectService.GeneratePlans(courseId,lessonId);
        //TODO Change Following Statements.
        System.out.println(res.getPlans().size());
        List<SelectPlan> plans= res.getPlans();
        for(SelectPlan plan:plans){
            System.out.println("APlan:");
            for(LessonEntity ls:plan.getLessons()){
                System.out.println('\t'+ls.getLessonId());
            }
        }
        return "selectLesson";
    }
}
