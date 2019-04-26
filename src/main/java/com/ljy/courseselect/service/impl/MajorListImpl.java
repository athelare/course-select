package com.ljy.courseselect.service.impl;

import com.ljy.courseselect.domain.GradeMajorEntity;
import com.ljy.courseselect.repository.GradeMajorDao;
import com.ljy.courseselect.service.MajorListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorListImpl implements MajorListService {
    @Autowired
    GradeMajorDao gradeMajorDao;

    @Override
    public List<GradeMajorEntity> findMajorListByGradeId(String gradeId) {
        return gradeMajorDao.findGradeMajorEntitiesByGradeId(gradeId);
    }
}
