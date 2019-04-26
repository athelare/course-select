package com.ljy.courseselect.service;

import com.ljy.courseselect.domain.GradeMajorEntity;

import java.util.List;


public interface MajorListService {
    List<GradeMajorEntity> findMajorListByGradeId(String gradeId);
}
