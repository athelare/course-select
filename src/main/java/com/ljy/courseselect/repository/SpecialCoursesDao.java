package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialCoursesDao extends JpaRepository<CourseEntity,Long> {

}
