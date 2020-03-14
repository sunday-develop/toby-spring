package com.pplenty.studytoby.chapter01.step01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.Driver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by yusik on 2020/03/09.
 */
@DisplayName("오브젝트와 의존관계")
public class UserDaoTest {

    @BeforeEach
    void setUp() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");

        PreparedStatement ps = con.prepareStatement("truncate toby.users");
        ps.executeUpdate();

    }

    @DisplayName("UserDao 의 변화")
    @Test
    void add() throws SQLException {

        // given
        UserDao userDao = new UserDao();
        userDao.setDataSource(new SimpleDriverDataSource(
                new Driver(),
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123"
        ));

        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        // when
        userDao.add(user);
        User result = userDao.get("koh");

        // then
        assertThat(result.getId()).isEqualTo(user.getId());
        System.out.println(result);

    }

    @DisplayName("팩토리 사용")
    @Test
    void factory() throws SQLException {

        // given
        UserDao userDao = new DaoFactory().userDao();

        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        // when
        userDao.add(user);
        User result = userDao.get("koh");

        // then
        assertThat(result.getId()).isEqualTo(user.getId());
        System.out.println(result);

    }

    @DisplayName("application context 를 이용하여 get bean")
    @Test
    void applicationContext() throws SQLException {

        // given
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        // when
        userDao.add(user);
        User result = userDao.get("koh");

        // then
        assertThat(result.getId()).isEqualTo(user.getId());
        System.out.println(result);

    }

    @DisplayName("Bean 동일성 비교 by factory")
    @Test
    void equalsBeanByFactory() {

        // given
        DaoFactory factory = new DaoFactory();

        // when
        UserDao userDao = factory.userDao();
        UserDao userDao2 = factory.userDao();

        // then
        assertThat(userDao == userDao2).isFalse();
        assertThat(userDao).isNotEqualTo(userDao2);
        System.out.println(userDao);
        System.out.println(userDao2);

    }

    @DisplayName("Bean 동일성 비교 by application context")
    @Test
    void equalsBean() {

        // given
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        // when
        UserDao userDao = context.getBean("userDao", UserDao.class);
        UserDao userDao2 = context.getBean("userDao", UserDao.class);

        // then
        assertThat(userDao == userDao2).isTrue();
        assertThat(userDao).isEqualTo(userDao2);
        System.out.println(userDao);
        System.out.println(userDao2);

    }
}
