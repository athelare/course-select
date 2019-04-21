package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class GrademajorEntityPK implements Serializable {
    private String gradeId;
    private String majorId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrademajorEntityPK that = (GrademajorEntityPK) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId);
    }
}
