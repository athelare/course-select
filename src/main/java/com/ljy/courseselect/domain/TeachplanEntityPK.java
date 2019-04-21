package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TeachplanEntityPK implements Serializable {
    private String gradeId;
    private String majorId;
    private String semesterId;
    private String courseId;

    @Column(name = "gradeId", nullable = false, length = 7)
    @Id
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Column(name = "majorId", nullable = false, length = 20)
    @Id
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Column(name = "semesterId", nullable = false, length = 5)
    @Id
    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    @Column(name = "courseId", nullable = false, length = 10)
    @Id
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeachplanEntityPK that = (TeachplanEntityPK) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(semesterId, that.semesterId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, semesterId, courseId);
    }
}
