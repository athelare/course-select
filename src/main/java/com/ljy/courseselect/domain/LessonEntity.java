package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lesson", schema = "csdb")
@IdClass(LessonEntityPK.class)
public class LessonEntity {
    private String courseId;
    private String lessonId;
    private String recommend;
    private Long halfA;
    private Long halfB;
    private String teacher;
    private CourseEntity course;


    @Id
    @Column(name = "courseId", nullable = false, length = 8)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "lessonId", nullable = false, length = 8)
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    @Basic
    @Column(name = "recommend", length = 20)
    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Basic
    @Column(name = "halfA")
    public Long getHalfA() {
        return halfA;
    }

    public void setHalfA(Long halfA) {
        this.halfA = halfA;
    }

    @Basic
    @Column(name = "halfB")
    public Long getHalfB() {
        return halfB;
    }

    public void setHalfB(Long halfB) {
        this.halfB = halfB;
    }

    @Basic
    @Column(name = "teacher", length = 15)
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonEntity that = (LessonEntity) o;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(recommend, that.recommend) &&
                Objects.equals(halfA, that.halfA) &&
                Objects.equals(halfB, that.halfB) &&
                Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, lessonId, recommend, halfA, halfB, teacher);
    }
}
