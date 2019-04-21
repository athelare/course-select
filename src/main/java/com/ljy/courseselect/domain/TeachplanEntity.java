package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "teachplan", schema = "csdb")
@IdClass(TeachplanEntityPK.class)
public class TeachplanEntity {
    private String gradeId;
    private String majorId;
    private String majorName;
    private String semesterId;
    private String courseId;
    private String courseType;

    @Id
    @Column(name = "gradeId", nullable = false, length = 7)
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Id
    @Column(name = "majorId", nullable = false, length = 20)
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Basic
    @Column(name = "majorName", length = 20)
    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Id
    @Column(name = "semesterId", nullable = false, length = 5)
    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    @Id
    @Column(name = "courseId", nullable = false, length = 10)
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "courseType", length = 10)
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
        TeachplanEntity that = (TeachplanEntity) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(majorName, that.majorName) &&
                Objects.equals(semesterId, that.semesterId) &&
                Objects.equals(courseId, that.courseId) &&
                Objects.equals(courseType, that.courseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, majorName, semesterId, courseId, courseType);
    }
}
