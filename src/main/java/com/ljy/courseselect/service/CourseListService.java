package com.ljy.courseselect.service;

import com.ljy.courseselect.domain.CourseEntity;

import java.util.List;

public interface CourseListService {
    List<CourseEntity>findCoursesByName(String name);
    List<CourseEntity>findCoursesByTeachPlan(String grade,String major,String semester);
    List<CourseEntity>findCoursesByCourseType(String type,Long time);

}
