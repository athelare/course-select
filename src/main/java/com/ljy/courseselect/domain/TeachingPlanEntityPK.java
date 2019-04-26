package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TeachingPlanEntityPK implements Serializable {
    private String gradeId;
    private String majorId;
    private String courseId;

    @Column(name = "grade_id", nullable = false, length = 7)
    @Id
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Column(name = "major_id", nullable = false, length = 20)
    @Id
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Column(name = "course_id", nullable = false, length = 10)
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
        TeachingPlanEntityPK that = (TeachingPlanEntityPK) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, courseId);
    }
}
