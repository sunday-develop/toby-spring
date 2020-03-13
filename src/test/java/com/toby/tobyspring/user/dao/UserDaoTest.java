package com.toby.tobyspring.user.dao;

import com.toby.tobyspring.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @ref https://junit.org/junit5/docs/current/user-guide/
 * @ref https://howtodoinjava.com/junit5/before-each-annotation-example/
 */
@DisplayName("userDao test")
public class UserDaoTest {

    UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setup() {
        userDao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:oracle:thin:@localhost:1521:orcl", "dahye", "test", true);
        userDao.setDataSource(dataSource);

        user1 = new User("dahyekim", "김다혜", "dahye");
        user2 = new User("toby", "토비", "toby");
        user3 = new User("whiteship", "백기선", "white");
    }

    @Test
    @DisplayName("userDao 프로세스 검증")
    public void addAndGet() throws SQLException {
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
        userDao.deleteAll();
        Assertions.assertEquals(0, userDao.getCount());

        userDao.add(user1);
        Assertions.assertEquals(1, userDao.getCount());

        userDao.add(user2);
        Assertions.assertEquals(2, userDao.getCount());

        userDao.add(user3);
        Assertions.assertEquals(3, userDao.getCount());
    }

    @Test
    @DisplayName("get() 메소드의 예외상황에 대한 테스트")
    public void getUserFailure() throws SQLException {
        userDao.deleteAll();
        Assertions.assertEquals(0, userDao.getCount());

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unkown_id");
        });
    }
}