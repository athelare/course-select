package com.ljy.courseselect;

import com.ljy.courseselect.repository.LessonTimeDao;
import com.ljy.courseselect.service.impl.CourseListImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ServletComponentScan
public class CourseSelectApplicationTests {
    @Autowired
    CourseListImpl courseList;

    @Autowired
    LessonTimeDao lessonTimeDao;
    @Test
    public void contextLoads() {
        System.out.println("Test__helloWorld!");
    }

    @Test
    public void testTime(){
        System.out.println(lessonTimeDao.findLessonTimeEntityByTimeId(100).toString());
    }

}
