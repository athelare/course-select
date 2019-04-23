package com.ljy.courseselect.controller;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.service.impl.CourseListImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class GetCourseList {
    @Autowired
    CourseListImpl courseList;

    @RequestMapping("/name={courseName}")
    @ResponseBody
    public List<CourseEntity>findCourseByName(@PathVariable String courseName){
        return courseList.findCoursesByName('%'+courseName+'%');
    }

    @RequestMapping("/plan/{gradeId}/{majorId}/{semester}")
    @ResponseBody
    public List<CourseEntity>findCourseByTeachPlan(
            @PathVariable String gradeId,
            @PathVariable String majorId,
            @PathVariable String semester
    ){
        if(semester.charAt(0)=='n')semester="";
        return courseList.findCoursesByTeachPlan(
                gradeId,
                majorId,
                '%'+semester+'%'
        );
    }

    @RequestMapping("/type/{type}/{time}")
    @ResponseBody
    public List<CourseEntity>findCourseByType(
            @PathVariable String type,
            @PathVariable Long time
    ){
        return courseList.findCoursesByCourseType(type,time);
    }
}
