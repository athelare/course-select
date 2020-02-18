package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.domain.LessonEntity;
import com.ljy.courseselect.pojo.SelectPlan;
import com.ljy.courseselect.pojo.SelectResult;
import com.ljy.courseselect.repository.CourseDao;
import com.ljy.courseselect.service.CourseSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseSelectImpl implements CourseSelectService {

    @Autowired
    CourseDao courseDao;

    private static int planCount;
    private static int maxConflict;
    private static SelectResult selectResult;
    private static List<LessonEntity>selectedLessons;
    private static List<CourseEntity>courseEntities;

    @Override
    public SelectResult GeneratePlans(List<String> courseIds, List<String> lessonIds, List<Long>busyTime) {
        //这个课程的列表只包含选取过的课程，不是课程所有的全部课程。
        List<CourseEntity>courses=new ArrayList<>();
        for(String courseId:courseIds){
            CourseEntity course = courseDao.findCourseEntityByCourseId(courseId);
            List<LessonEntity>lessons = new ArrayList<>();
            for(LessonEntity lesson:course.getLessonsByCourseId()){
                if(lessonIds.contains(lesson.getLessonId())){
                    lessons.add(lesson);
                }
            }
            course.setLessonsByCourseId(lessons);
            courses.add(course);
        }

        /*测试：是否只抽取了选择了的开课班级
        for(CourseEntity course:courses){
            System.out.println(course.getCourseName());
            for(LessonEntity lesson:course.getLessonsByCourseId()){
                System.out.println('\t'+lesson.getLessonId());
            }
        }*/
        Long allBusyTime=0L;
        for(Long oneBusyTime:busyTime){
            allBusyTime|=oneBusyTime;
        }
        //Initiate Variables.
        courseEntities=courses;
        selectResult = new SelectResult();
        planCount=0;
        maxConflict=-1;
        selectedLessons=new ArrayList<>();
        do{
            maxConflict++;
            if(maxConflict>8){
                selectResult.setTooManyConflicts(true);
                break;
            }
            SearchPlans(
                    0,
                    allBusyTime,
                    allBusyTime,
                    0
            );
        }while(selectResult.getPlans().size()==0);
        System.out.println("Plan generating finished.");
        System.out.println("Total plans:"+selectResult.getPlans().size());
        System.out.println("Conflict status:"+selectResult.getConflict());
        System.out.println("isTooManyPlans:"+selectResult.isTooManyPlans());
        System.out.println("isTooManyConflicts:"+selectResult.isTooManyConflicts());
        return selectResult;
    }
     private void SearchPlans(int index,
                            Long currentFirstHalf,
                            Long currentSecondHalf,
                            int currentConflict){
        //If too many plans
        if(planCount>=100) {
            selectResult.setTooManyPlans(true);
            return;
        }
        //If too many conflicts
        else if(currentConflict>maxConflict)
            return;

        if(index>=courseEntities.size()){
            selectResult.setConflict(currentConflict);
            SelectPlan newPlan = new SelectPlan();
            newPlan.setLessons(new ArrayList<>(selectedLessons));
            newPlan.setFirstHalf(currentFirstHalf);
            newPlan.setSecondHalf(currentSecondHalf);
            selectResult.addNewPlan(newPlan);
            planCount++;
        }else{
            CourseEntity currentCourse = courseEntities.get(index);
            for(LessonEntity lesson:currentCourse.getLessonsByCourseId()){
                selectedLessons.add(selectedLessons.size(),lesson);
                SearchPlans(
                        index+1,
                        lesson.getFirstHalf() | currentFirstHalf,
                        lesson.getSecondHalf() | currentSecondHalf,
                        currentConflict +
                                countBit(lesson.getFirstHalf() & currentFirstHalf) +
                                countBit(lesson.getSecondHalf() & currentSecondHalf)
                );
                selectedLessons.remove(selectedLessons.size()-1);
            }
        }

    }

    private int countBit(Long num){
        int count=0;
        while(num!=0){
            num&=(num-1);
            count++;
        }
        return count;
    }

}
