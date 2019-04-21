package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.GrademajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeMajorDao extends JpaRepository<GrademajorEntity,Long> {
    List<GrademajorEntity>findGrademajorEntitiesByGradeId(String gradeId);
}
