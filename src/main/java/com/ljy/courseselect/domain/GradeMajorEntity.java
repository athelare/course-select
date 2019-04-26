package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "grade_major", schema = "course_select")
@IdClass(GradeMajorEntityPK.class)
public class GradeMajorEntity {
    private String gradeId;
    private String majorId;
    private String majorName;

    @Id
    @Column(name = "grade_id", nullable = false, length = 7)
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Id
    @Column(name = "major_id", nullable = false, length = 30)
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Basic
    @Column(name = "major_name", nullable = true, length = 30)
    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeMajorEntity that = (GradeMajorEntity) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, majorName);
    }
}
