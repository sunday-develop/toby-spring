package com.toby.tobyspring.user.dao;

import com.toby.tobyspring.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

@DisplayName("userDao test")
public class UserDaoTest {

    @Test
    @DisplayName("userDao 프로세스 검증")
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("dahyek776");
        user.setName("김다혜223");
        user.setPassword("dahye232");

        userDao.add(user);

        User user2 = userDao.get("dahyek776");
        Assertions.assertEquals(user2.getName(), user.getName());
        Assertions.assertEquals(user2.getPassword(), user.getPassword());
    }
}