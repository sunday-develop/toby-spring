package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter01.step01.DaoFactory;
import com.pplenty.studytoby.chapter01.step01.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = DaoFactory.class)
class StudyTobyApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        System.out.println(userDao);
    }

}
