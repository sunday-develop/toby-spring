package com.pplenty.studytoby.chapter02;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by yusik on 2020/03/14.
 */
@DisplayName("테스트")
public class UserDaoTest {

    private static UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeAll
    static void beforeAll() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("/di/userDaoFactory.xml");
        userDao = context.getBean("userDao", UserDao.class);
    }

    @BeforeEach
    void setUp() throws SQLException {

        // given fixture
        user1 = new User("pplenty", "yusik", "1234");
        user2 = new User("kohyusik", "usik", "4321");
        user3 = new User("usikzzang", "YUSU", "qwer");

        userDao.deleteAll();
    }

    @DisplayName("사용자 생성 및 조회")
    @Test
    void addAndGet() throws SQLException {

        // given
        // when
        userDao.add(user1);
        userDao.add(user2);

        // then
        assertThat(userDao.getCount()).isEqualTo(2);

        User result1 = userDao.get(user1.getId());
        assertThat(result1.getId()).isEqualTo(user1.getId());
        assertThat(result1.getPassword()).isEqualTo(user1.getPassword());

        User result2 = userDao.get(user2.getId());
        assertThat(result2.getId()).isEqualTo(user2.getId());
        assertThat(result2.getPassword()).isEqualTo(user2.getPassword());
    }

    @DisplayName("존재하지 않는 사용자")
    @Test
    void getUserFailure() throws SQLException {

        // then
        assertThat(userDao.getCount()).isEqualTo(0);
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown_id"));
    }

    @DisplayName("사용자 생성 및 조회(junit5)")
    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    void addAndGetFromCsv(String id, String name, String password) throws SQLException {

        // given
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);

        // when
        userDao.add(user);
        User result = userDao.get(id);
        int count = userDao.getCount();

        // then
        assertThat(count).isEqualTo(1);
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
    }

    @DisplayName("여러 사용자 추가 & 갯수 조회 테스트")
    @Test
    void count() throws SQLException {

        // given
        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");
        User user2 = new User("ko", "yu", "sik");

        // when
        // then
        assertThat(userDao.getCount()).isEqualTo(0);
        userDao.add(user);
        assertThat(userDao.getCount()).isEqualTo(1);
        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);
    }
}