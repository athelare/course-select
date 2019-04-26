package com.ljy.courseselect.pojo;

import com.ljy.courseselect.domain.LessonEntity;

import java.util.ArrayList;
import java.util.List;

public class SelectPlan {
    private List<LessonEntity>lessons;
    private Long firstHalf,secondHalf;

    public SelectPlan(){
        lessons=new ArrayList<>();
        firstHalf=0L;
        secondHalf=0L;
    }

    public List<LessonEntity> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonEntity> lessons) {
        this.lessons = lessons;
    }

    public Long getSecondHalf() {
        return secondHalf;
    }

    public void setSecondHalf(Long secondHalf) {
        this.secondHalf = secondHalf;
    }

    public Long getFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(Long firstHalf) {
        this.firstHalf = firstHalf;
    }
}
