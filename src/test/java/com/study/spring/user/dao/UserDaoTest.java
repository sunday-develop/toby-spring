package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    public void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("spring/applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user1 = new User("user1", "username1", "username11");
        User user2 = new User("user2", "username2", "username22");

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        userDao.add(user2);
        assertEquals(userDao.getCount(), 2);

        User userTarget1 = userDao.get(user1.getId());
        assertEquals(userTarget1.getName(), user1.getName());
        assertEquals(userTarget1.getPassword(), user1.getPassword());

        User userTarget2 = userDao.get(user2.getId());
        assertEquals(userTarget2.getName(), user2.getName());
        assertEquals(userTarget2.getPassword(), user2.getPassword());

    }

    @DisplayName("get() 메소드의 예외 상황에 대한 테스트")
    @Test
    public void getUserFailure() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("spring/applicationContext.xml");

        final UserDao userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown"));
    }
}
