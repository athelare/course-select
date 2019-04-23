package com.ljy.courseselect.repository;

import com.ljy.courseselect.domain.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<CourseEntity,Long> {
    CourseEntity findCourseEntityByCourseId(String courseId);

    List<CourseEntity> findCourseEntitiesByNameLike(String name);

    @Query(nativeQuery = true,value = "SELECT course.* FROM course WHERE course_id in(\tSELECT course_id FROM teachplan WHERE grade_id=:grade AND major_id=:major AND semester_id LIKE CONCAT('%',:semester,'%'));")
    List<CourseEntity> findCourseEntitiesByTeachPlan(@Param("grade")String grade,@Param("major")String major,@Param("semester")String semester);

    @Query(nativeQuery = true,value = "SELECT * FROM course WHERE course_id in( SELECT specialcourses.course_id AS course_id FROM specialcourses INNER JOIN lesson ON specialcourses.course_id=lesson.course_id WHERE ((lesson.halfA|lesson.halfB)& :reqTime )=0 AND specialcourses.course_type= :courseType GROUP BY specialcourses.course_id HAVING count(*)>0); ")
    List<CourseEntity> findSpecialcoursesEntitiesByCourseType(@Param("reqTime")Long time, @Param("courseType") String type);
}
