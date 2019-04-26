package com.ljy.courseselect.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "course", schema = "course_select")
public class CourseEntity {
    private String courseId;
    private String courseName;
    private Double credit;
    private String school;
    private Collection<LessonEntity> lessonsByCourseId;
    private Collection<SpecificCourseEntity> specificCoursesByCourseId;
    private Collection<TeachingPlanEntity> teachingPlansByCourseId;

    @Id
    @Column(name = "course_id", nullable = false, length = 10)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "course_name", nullable = true, length = 50)
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Basic
    @Column(name = "credit", nullable = true, precision = 1)
    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    @Basic
    @Column(name = "school", nullable = true, length = 50)
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseEntity that = (CourseEntity) o;
        return Objects.equals(courseId, that.courseId) &&
                Objects.equals(courseName, that.courseName) &&
                Objects.equals(credit, that.credit) &&
                Objects.equals(school, that.school);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, credit, school);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "courseByCourseId")
    public Collection<LessonEntity> getLessonsByCourseId() {
        return lessonsByCourseId;
    }

    public void setLessonsByCourseId(Collection<LessonEntity> lessonsByCourseId) {
        this.lessonsByCourseId = lessonsByCourseId;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "courseByCourseId")
    public Collection<SpecificCourseEntity> getSpecificCoursesByCourseId() {
        return specificCoursesByCourseId;
    }

    public void setSpecificCoursesByCourseId(Collection<SpecificCourseEntity> specificCoursesByCourseId) {
        this.specificCoursesByCourseId = specificCoursesByCourseId;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "courseByCourseId")
    public Collection<TeachingPlanEntity> getTeachingPlansByCourseId() {
        return teachingPlansByCourseId;
    }

    public void setTeachingPlansByCourseId(Collection<TeachingPlanEntity> teachingPlansByCourseId) {
        this.teachingPlansByCourseId = teachingPlansByCourseId;
    }
}
