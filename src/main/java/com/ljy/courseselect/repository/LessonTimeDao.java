package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.LessontimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonTimeDao extends JpaRepository<LessontimeEntity,Long> {
    List<LessontimeEntity> findLessontimeEntitiesByLessonId(String lessonId);
}
