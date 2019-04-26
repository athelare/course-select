package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class SpecificCourseEntityPK implements Serializable {
    private String courseType;
    private String courseId;

    @Column(name = "course_type", nullable = false, length = 3)
    @Id
    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
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
        SpecificCourseEntityPK that = (SpecificCourseEntityPK) o;
        return Objects.equals(courseType, that.courseType) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseType, courseId);
    }
}
