package com.ljy.courseselect.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "lesson", schema = "course_select")
public class LessonEntity {
    private String lessonId;
    private String courseId;
    private String teacherName;
    private String advice;
    private Long firstHalf;
    private Long secondHalf;
    private CourseEntity courseByCourseId;
    private Collection<LessonTimeEntity> lessonTimesByLessonId;

    @Id
    @Column(name = "lesson_id", nullable = false, length = 10)
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    @Basic
    @Column(name="course_id", nullable = false,length = 10)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "teacher_name", nullable = true, length = 30)
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Basic
    @Column(name = "advice", nullable = true, length = 50)
    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Basic
    @Column(name = "first_half", nullable = true)
    public Long getFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(Long firstHalf) {
        this.firstHalf = firstHalf;
    }

    @Basic
    @Column(name = "second_half", nullable = true)
    public Long getSecondHalf() {
        return secondHalf;
    }

    public void setSecondHalf(Long secondHalf) {
        this.secondHalf = secondHalf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonEntity that = (LessonEntity) o;
        return Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(teacherName, that.teacherName) &&
                Objects.equals(advice, that.advice) &&
                Objects.equals(firstHalf, that.firstHalf) &&
                Objects.equals(secondHalf, that.secondHalf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, teacherName, advice, firstHalf, secondHalf);
    }

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id",insertable = false,updatable = false)
    public CourseEntity getCourseByCourseId() {
        return courseByCourseId;
    }

    public void setCourseByCourseId(CourseEntity courseByCourseId) {
        this.courseByCourseId = courseByCourseId;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "lessonByLessonId",fetch = FetchType.EAGER)
    public Collection<LessonTimeEntity> getLessonTimesByLessonId() {
        return lessonTimesByLessonId;
    }

    public void setLessonTimesByLessonId(Collection<LessonTimeEntity> lessonTimesByLessonId) {
        this.lessonTimesByLessonId = lessonTimesByLessonId;
    }


}
