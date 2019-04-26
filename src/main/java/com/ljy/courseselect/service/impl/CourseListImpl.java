package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.repository.CourseDao;
import com.ljy.courseselect.repository.LessonDao;
import com.ljy.courseselect.service.CourseListService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseListImpl implements CourseListService {
    @Autowired
    CourseDao courseDao;

    @Autowired
    LessonDao lessonDao;

    @Override
    public List<CourseEntity> findCoursesByName(String name) {
        return courseDao.findCourseEntitiesByCourseNameLike('%'+name+'%');
    }

    @Override
    public List<CourseEntity> findCoursesByTeachPlan(String grade, String major, String semester) {
        if(semester.charAt(0)=='n')semester="";
        return courseDao.findCourseEntitiesByTeachPlan(grade,major,'%'+semester+'%');
    }

    @Override
    public List<CourseEntity> findCoursesByCourseType(String type,Long time) {
        time=~time;
        return courseDao.findSpecificCourseEntitiesByCourseType(time,type);
    }

    @Override
    public List<CourseEntity> findCoursesByCourseIdsWithLessons(List<String> courseId) {
        List<CourseEntity> courses = new ArrayList<>();
        for(String cid : courseId){
            CourseEntity cs=courseDao.findCourseEntityByCourseId(cid);
            Hibernate.initialize(cs.getLessonsByCourseId());
            courses.add(cs);
        }
        return courses;
    }

}
