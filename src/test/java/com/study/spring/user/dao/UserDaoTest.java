package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDaoTest {

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    public void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("spring/applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        User user1 = new User();
        user1.setId("whiteship");
        user1.setName("back");
        user1.setPassword("married");

        userDao.add(user1);
        assertEquals(userDao.getCount(), 1);

        User user2 = userDao.get(user1.getId());

        assertEquals(user2.getName(), user1.getName());
        assertEquals(user2.getPassword(), user1.getPassword());
    }
}
