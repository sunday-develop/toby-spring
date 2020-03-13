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

        userDao.deleteAll();
        Assertions.assertEquals(0, userDao.getCount());

        User user = new User();
        user.setId("dahyekim");
        user.setName("김다혜");
        user.setPassword("pass");

        userDao.add(user);
        Assertions.assertEquals(1, userDao.getCount());

        User user2 = userDao.get("dahyekim");
        Assertions.assertEquals(user2.getName(), user.getName());
        Assertions.assertEquals(user2.getPassword(), user.getPassword());
    }
}