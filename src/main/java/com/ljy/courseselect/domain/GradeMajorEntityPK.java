package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class GradeMajorEntityPK implements Serializable {
    private String gradeId;
    private String majorId;

    @Column(name = "grade_id", nullable = false, length = 7)
    @Id
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    @Column(name = "major_id", nullable = false, length = 30)
    @Id
    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeMajorEntityPK that = (GradeMajorEntityPK) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId);
    }
}
