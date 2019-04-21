package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends JpaRepository<LessonEntity,Long> {
    String findLessonEntityByLessonId(String lessonId);
    String findLessonEntitiesByCourseId(String courseId);
}
