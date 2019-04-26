package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "specific_course", schema = "course_select")
@IdClass(SpecificCourseEntityPK.class)
public class SpecificCourseEntity {
    private String courseType;
    private String courseId;
    private CourseEntity courseByCourseId;

    @Id
    @Column(name = "course_type", nullable = false, length = 3)
    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    @Id
    @Column(name = "course_id", nullable = false, length = 10)
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
        SpecificCourseEntity that = (SpecificCourseEntity) o;
        return Objects.equals(courseType, that.courseType) &&
                Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseType, courseId);
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
