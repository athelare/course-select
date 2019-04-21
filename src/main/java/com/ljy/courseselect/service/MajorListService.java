package com.ljy.courseselect.service;

import com.ljy.courseselect.domain.GrademajorEntity;
import java.util.List;


public interface MajorListService {
    List<GrademajorEntity> findMajorListByGradeId(String gradeId);
}
