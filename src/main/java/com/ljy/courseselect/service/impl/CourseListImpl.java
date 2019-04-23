package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.repository.CourseDao;
import com.ljy.courseselect.repository.LessonDao;
import com.ljy.courseselect.service.CourseListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseListImpl implements CourseListService {
    @Autowired
    CourseDao courseDao;

    @Autowired
    LessonDao lessonDao;

    @Override
    public List<CourseEntity> findCoursesByName(String name) {
        return courseDao.findCourseEntitiesByNameLike(name);
    }

    @Override
    public List<CourseEntity> findCoursesByTeachPlan(String grade, String major, String semester) {
        return courseDao.findCourseEntitiesByTeachPlan(grade,major,semester);
    }

    @Override
    public List<CourseEntity> findCoursesByCourseType(String type,Long time) {
        time=~time;
        return courseDao.findSpecialcoursesEntitiesByCourseType(time,type);
    }
}
