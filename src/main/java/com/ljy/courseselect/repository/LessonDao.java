package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDao extends JpaRepository<LessonEntity,Long> {
    LessonEntity findLessonEntityByLessonId(String lessonId);
    List<LessonEntity> findLessonEntitiesByCourseId(String courseId);
}
