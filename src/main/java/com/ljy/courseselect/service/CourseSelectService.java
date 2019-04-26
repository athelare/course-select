package com.ljy.courseselect.service;

import com.ljy.courseselect.pojo.SelectResult;

import java.util.List;

public interface CourseSelectService {
    SelectResult GeneratePlans(List<String> courseIds, List<String> lessonIds);
}
