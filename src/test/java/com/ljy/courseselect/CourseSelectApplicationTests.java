package com.ljy.courseselect;

import com.ljy.courseselect.domain.CourseEntity;
import com.ljy.courseselect.service.impl.CourseListImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ServletComponentScan
public class CourseSelectApplicationTests {
    @Autowired
    CourseListImpl courseList;

    @Test
    public void contextLoads() {
        System.out.println("Test_______helloWorld!");
    }

    @Test
    public void testCourseDao(){

    }

}
