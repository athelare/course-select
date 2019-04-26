package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "teaching_plan", schema = "course_select")
@IdClass(TeachingPlanEntityPK.class)
public class TeachingPlanEntity {
    private String gradeId;
    private String majorId;
    private String courseId;
    private String semesterId;
    private String courseType;
    private CourseEntity courseByCourseId;

    @Id
    @Column(name = "grade_id", nullable = false, length = 7)
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Id
    @Column(name = "major_id", nullable = false, length = 20)
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Id
    @Column(name = "course_id", nullable = false, length = 10)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "semester_id", nullable = true, length = 5)
    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    @Basic
    @Column(name = "course_type", nullable = true, length = 10)
    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachingPlanEntity that = (TeachingPlanEntity) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(semesterId, that.semesterId) &&
                Objects.equals(courseType, that.courseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, courseId, semesterId, courseType);
    }

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false,insertable = false,updatable = false)
    public CourseEntity getCourseByCourseId() {
        return courseByCourseId;
    }

    public void setCourseByCourseId(CourseEntity courseByCourseId) {
        this.courseByCourseId = courseByCourseId;
    }
}
