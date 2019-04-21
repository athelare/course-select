package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lessontime", schema = "csdb")
@IdClass(LessontimeEntityPK.class)
public class LessontimeEntity {
    private String lessonId;
    private byte timeId;
    private String time;
    private String place;
    private String week;
    private Long week1;
    private Long week2;

    @Id
    @Column(name = "lessonId", nullable = false, length = 8)
    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    @Id
    @Column(name = "timeId", nullable = false)
    public byte getTimeId() {
        return timeId;
    }

    public void setTimeId(byte timeId) {
        this.timeId = timeId;
    }

    @Basic
    @Column(name = "time", length = 30)
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Basic
    @Column(name = "place", length = 12)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Basic
    @Column(name = "week", length = 12)
    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Basic
    @Column(name = "week1")
    public Long getWeek1() {
        return week1;
    }

    public void setWeek1(Long week1) {
        this.week1 = week1;
    }

    @Basic
    @Column(name = "week2")
    public Long getWeek2() {
        return week2;
    }

    public void setWeek2(Long week2) {
        this.week2 = week2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessontimeEntity that = (LessontimeEntity) o;
        return timeId == that.timeId &&
                Objects.equals(lessonId, that.lessonId) &&
                Objects.equals(time, that.time) &&
                Objects.equals(place, that.place) &&
                Objects.equals(week, that.week) &&
                Objects.equals(week1, that.week1) &&
                Objects.equals(week2, that.week2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonId, timeId, time, place, week, week1, week2);
    }
}
