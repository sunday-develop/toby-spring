package com.toby.tobyspring.user.dao;

import com.toby.tobyspring.user.domain.User;
import com.toby.tobyspring.user.exception.DuplicateUserIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @ref https://junit.org/junit5/docs/current/user-guide/
 * @ref https://howtodoinjava.com/junit5/before-each-annotation-example/
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DisplayName("userDao test")
public class UserDaoTest {

    @Autowired
    UserDaoJdbc userDao;

    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setup() {
        userDao = new UserDaoJdbc();
        DataSource dataSource = new SingleConnectionDataSource("jdbc:oracle:thin:@localhost:1521:orcl", "dahye", "test", true);
        userDao.setDataSource(dataSource);

        user1 = new User("adahyekim", "김다혜", "dahye");
        user2 = new User("btoby", "토비", "toby");
        user3 = new User("cwhiteship", "백기선", "white");
    }

    @Test
    @DisplayName("userDao 프로세스 검증")
    public void addAndGet() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        userDao.add(user2);
        assertEquals(2, userDao.getCount());

        User userget1 = userDao.get(user1.getId());
        assertEquals(userget1.getName(), user1.getName());
        assertEquals(userget1.getPassword(), user1.getPassword());

        User userget2 = userDao.get(user2.getId());
        assertEquals(userget2.getName(), user2.getName());
        assertEquals(userget2.getPassword(), user2.getPassword());
    }

    @Test
    @DisplayName("getCount() 테스트")
    public void count() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        assertEquals(1, userDao.getCount());

        userDao.add(user2);
        assertEquals(2, userDao.getCount());

        userDao.add(user3);
        assertEquals(3, userDao.getCount());
    }

    @Test
    @DisplayName("get() 메소드의 예외상황에 대한 테스트")
    public void getUserFailure() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unkown_id");
        });
    }

    @Test
    @DisplayName("getAll() 메소드 테스트")
    public void getAll() {
        userDao.deleteAll();

        List<User> user0 = userDao.getAll();
        assertEquals(0, user0.size());

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertEquals(1, users1.size());
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertEquals(2, users2.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));


        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertEquals(3, users3.size());
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));

    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

    @Test
    @DisplayName("중복된 아이디를 등록하는 경우")
    public void duplicateUser() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        userDao.add(user1);
        Assertions.assertThrows(DuplicateUserIdException.class, () -> {
            userDao.add(user1);
        });
    }

    @Test
    @DisplayName("SQLException 전환 기능의 학습 테스트")
    public void learningTranslateException() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertEquals(DuplicateKeyException.class, set.translate(null, null, sqlEx).getClass());
        }

    }
}