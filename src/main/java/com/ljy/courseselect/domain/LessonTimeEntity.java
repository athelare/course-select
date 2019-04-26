package com.ljy.courseselect.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "lesson_time", schema = "course_select")
public class LessonTimeEntity {
    private int timeId;
    private Long firstHalf;
    private Long secondHalf;
    private String place;
    private String time;
    private String week;
    private LessonEntity lessonByLessonId;

    @Id
    @Column(name = "time_id", nullable = false)
    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    @Basic
    @Column(name = "first_half", nullable = true)
    public Long getFirstHalf() {
        return firstHalf;
    }

    public void setFirstHalf(Long firstHalf) {
        this.firstHalf = firstHalf;
    }

    @Basic
    @Column(name = "second_half", nullable = true)
    public Long getSecondHalf() {
        return secondHalf;
    }

    public void setSecondHalf(Long secondHalf) {
        this.secondHalf = secondHalf;
    }

    @Basic
    @Column(name = "place", nullable = true, length = 50)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Basic
    @Column(name = "time", nullable = true, length = 50)
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Basic
    @Column(name = "week", nullable = true, length = 50)
    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonTimeEntity that = (LessonTimeEntity) o;
        return timeId == that.timeId &&
                Objects.equals(firstHalf, that.firstHalf) &&
                Objects.equals(secondHalf, that.secondHalf) &&
                Objects.equals(place, that.place) &&
                Objects.equals(time, that.time) &&
                Objects.equals(week, that.week);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId, firstHalf, secondHalf, place, time, week);
    }

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id")
    public LessonEntity getLessonByLessonId() {
        return lessonByLessonId;
    }

    public void setLessonByLessonId(LessonEntity lessonByLessonId) {
        this.lessonByLessonId = lessonByLessonId;
    }
}
