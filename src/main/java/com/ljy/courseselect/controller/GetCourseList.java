package com.ljy.courseselect.controller;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.service.CourseListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class GetCourseList {
    @Autowired
    CourseListService courseListService;

    @RequestMapping("/name={courseName}")
    @ResponseBody
    public List<CourseEntity>findCourseByName(@PathVariable String courseName){
        return courseListService.findCoursesByName(courseName);
    }

    @RequestMapping("/plan/{gradeId}/{majorId}/{semester}")
    @ResponseBody
    public List<CourseEntity>findCourseByTeachPlan(@PathVariable String gradeId,
                                                   @PathVariable String majorId,
                                                   @PathVariable String semester){
        return courseListService.findCoursesByTeachPlan(gradeId, majorId, semester);
    }

    @RequestMapping("/type/{type}/{time}")
    @ResponseBody
    public List<CourseEntity>findCourseByType(@PathVariable String type,
                                              @PathVariable Long time){
        return courseListService.findCoursesByCourseType(type,time);
    }

    @RequestMapping("/lessons")
    public String getCourseLessons(@RequestParam List<String> courseId, Model md){
        List<CourseEntity>courses = courseListService.findCoursesByCourseIdsWithLessons(courseId);
        md.addAttribute("courses",courses);
        return "selectLesson";
    }
}
