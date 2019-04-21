package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<CourseEntity,Long> {
    CourseEntity findCourseEntityByCourseId(String courseId);
    List<CourseEntity> findCourseEntitiesByNameLike(String name);
}
