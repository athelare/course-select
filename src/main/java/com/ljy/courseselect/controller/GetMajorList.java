package com.ljy.courseselect.controller;

import com.ljy.courseselect.domain.GradeMajorEntity;
import com.ljy.courseselect.service.impl.MajorListImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/majors")
public class GetMajorList {
    @Autowired
    MajorListImpl majorList;

    @RequestMapping("/{gradeId}")
    @ResponseBody
    public List<GradeMajorEntity> getMajors(@PathVariable String gradeId){
        return majorList.findMajorListByGradeId(gradeId);
    }
}
