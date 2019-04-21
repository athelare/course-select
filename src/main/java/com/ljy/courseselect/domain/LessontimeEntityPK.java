package com.ljy.courseselect.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class LessontimeEntityPK implements Serializable {
    private String lessonId;
    private byte timeId;

    @Column(name = "lessonId", nullable = false, length = 8)
    @Id
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    @Column(name = "timeId", nullable = false)
    @Id
    public byte getTimeId() {
        return timeId;
    }

    public void setTimeId(byte timeId) {
        this.timeId = timeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessontimeEntityPK that = (LessontimeEntityPK) o;
        return timeId == that.timeId &&
                Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, timeId);
    }
}
