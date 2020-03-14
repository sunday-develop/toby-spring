package com.pplenty.studytoby.chapter01.step01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by yusik on 2020/03/14.
 */
class UserDaoConnetionCountingTest {

    @BeforeEach
    void setUp() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");

        PreparedStatement ps = con.prepareStatement("truncate toby.users");
        ps.executeUpdate();
    }

    @DisplayName("DB Connection counting 빈 주입")
    @Test
    void connectionCountingDI() throws SQLException {
        // given
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);
        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);

        User user = new User();
        user.setId("koh");
        user.setName("yusik");
        user.setPassword("1234");

        // when
        userDao.add(user);
        User result = userDao.get("koh");

        // then
        assertThat(ccm.getCounter() > 0).isTrue();
        System.out.println("Connection counter : "+ccm.getCounter());
    }
}