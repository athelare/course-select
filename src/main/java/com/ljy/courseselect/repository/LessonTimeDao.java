package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.LessonTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LessonTimeDao extends JpaRepository<LessonTimeEntity,Long> {
}
