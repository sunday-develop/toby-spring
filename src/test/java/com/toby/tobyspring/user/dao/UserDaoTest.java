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
        User user1 = new User("dahyekim", "김다혜", "dahye");
        User user2 = new User("toby", "토비", "toby");

        userDao.deleteAll();
        Assertions.assertEquals(0, userDao.getCount());

        userDao.add(user1);
        userDao.add(user2);
        Assertions.assertEquals(2, userDao.getCount());

        User userget1 = userDao.get(user1.getId());
        Assertions.assertEquals(userget1.getName(), user1.getName());
        Assertions.assertEquals(userget1.getPassword(), user1.getPassword());

        User userget2 = userDao.get(user2.getId());
        Assertions.assertEquals(userget2.getName(), user2.getName());
        Assertions.assertEquals(userget2.getPassword(), user2.getPassword());
    }

    @Test
    @DisplayName("getCount() 테스트")
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);
        User user1 = new User("dahyekim", "김다혜", "dahye");
        User user2 = new User("toby", "토비", "toby");
        User user3 = new User("whiteship", "백기선", "white");

        userDao.deleteAll();
        Assertions.assertEquals(0, userDao.getCount());

        userDao.add(user1);
        Assertions.assertEquals(1, userDao.getCount());

        userDao.add(user2);
        Assertions.assertEquals(2, userDao.getCount());

        userDao.add(user3);
        Assertions.assertEquals(3, userDao.getCount());
    }
}