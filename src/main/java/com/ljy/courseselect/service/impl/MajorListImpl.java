package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.GrademajorEntity;
import com.ljy.courseselect.repository.GradeMajorDao;
import com.ljy.courseselect.service.MajorListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorListImpl implements MajorListService {
    @Autowired
    GradeMajorDao gradeMajorDao;

    @Override
    public List<GrademajorEntity> findMajorListByGradeId(String gradeId) {
        return gradeMajorDao.findGrademajorEntitiesByGradeId(gradeId);
    }
}
