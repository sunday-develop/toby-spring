package com.pplenty.studytoby.chapter02;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/14.
 */
public class UserDaoTest {

    UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("/di/userDaoFactory.xml");
        userDao = context.getBean("userDao", UserDao.class);
        userDao.deleteAll();
    }

    @Test
    void addAndGet() throws SQLException {

        // given
        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        // when
        userDao.add(user);
        User result = userDao.get("koh");
        int count = userDao.getCount();

        // then
        assertThat(count).isEqualTo(1);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }
}