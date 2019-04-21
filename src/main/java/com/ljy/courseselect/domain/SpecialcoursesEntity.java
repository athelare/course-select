package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "specialcourses", schema = "csdb")
@IdClass(SpecialcoursesEntityPK.class)
public class SpecialcoursesEntity {
    private String courseType;
    private String courseId;

    @Id
    @Column(name = "courseType", nullable = false, length = 3)
    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    @Id
    @Column(name = "courseId", nullable = false, length = 8)
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
        SpecialcoursesEntity that = (SpecialcoursesEntity) o;
        return Objects.equals(courseType, that.courseType) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseType, courseId);
    }
}
