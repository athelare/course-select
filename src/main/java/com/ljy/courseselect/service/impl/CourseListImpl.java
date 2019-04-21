package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.domain.TeachplanEntity;
import com.ljy.courseselect.repository.CourseDao;
import com.ljy.courseselect.repository.TeachPlanDao;
import com.ljy.courseselect.service.CourseListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseListImpl implements CourseListService {
    @Autowired
    CourseDao courseDao;

    @Autowired
    TeachPlanDao teachPlanDao;

    @Override
    public List<CourseEntity> findCoursesByName(String name) {
        return courseDao.findCourseEntitiesByNameLike(name);
    }

    @Override
    public List<CourseEntity> findCoursesByTeachPlan(String grade, String major, String semester) {
        List<TeachplanEntity> planItems=teachPlanDao.findAllByGradeIdAndMajorIdAndSemesterIdLike(grade,major,semester);
        List<CourseEntity>courseList=new ArrayList<>();
        for(TeachplanEntity planItem:planItems){
            courseList.add(courseDao.findCourseEntityByCourseId(planItem.getCourseId()));
        }
        return courseList;
    }

    @Override
    public List<CourseEntity> findCoursesByCourseType(String type) {
        return null;
    }
}
