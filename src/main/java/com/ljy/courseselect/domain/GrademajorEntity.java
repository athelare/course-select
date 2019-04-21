package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "grademajor", schema = "csdb")
@IdClass(GrademajorEntityPK.class)
public class GrademajorEntity {
    private String gradeId;
    private String majorId;
    private String majorName;

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
    @Column(name = "majorName", nullable = true, length = 20)
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
        GrademajorEntity that = (GrademajorEntity) o;
        return Objects.equals(gradeId, that.gradeId) &&
                Objects.equals(majorId, that.majorId) &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, majorId, majorName);
    }
}
