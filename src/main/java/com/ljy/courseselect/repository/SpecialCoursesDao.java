package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.SpecialcoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialCoursesDao extends JpaRepository<SpecialcoursesEntity,Long> {
    List<SpecialCoursesDao> findSpecialcoursesEntitiesByCourseType(String type);
}
