package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.TeachplanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachPlanDao extends JpaRepository<TeachplanEntity,Long> {
    List<TeachplanEntity> findAllByGradeIdAndMajorId(String gradeId, String majorId);
}
