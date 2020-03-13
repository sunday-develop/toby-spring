package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-test.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao userDao;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        userDao = context.getBean("userDao", UserDao.class);
        user1 = new User("user1", "username1", "username11");
        user2 = new User("user2", "username2", "username22");

        System.out.println(this.context);
        System.out.println(this);
    }

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    public void addAndGet() throws SQLException {

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

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown"));
    }
}
