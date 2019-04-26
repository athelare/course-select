package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.GradeMajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMajorDao extends JpaRepository<GradeMajorEntity,Long> {
    List<GradeMajorEntity>findGradeMajorEntitiesByGradeId(String gradeId);
}
