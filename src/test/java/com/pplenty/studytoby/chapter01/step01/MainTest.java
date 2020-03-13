package com.pplenty.studytoby.chapter01.step01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by yusik on 2020/03/09.
 */
@DisplayName("1.1 초난감 DAO")
public class MainTest {

    @BeforeEach
    void setUp() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");

        PreparedStatement ps = con.prepareStatement("truncate toby.users");

        ps.executeUpdate();

    }

    @DisplayName("UserDao")
    @Test
    void add() throws SQLException {

        // given
        UserDao userDao = new UserDao();
//        UserDao userDao = new NUserDao();
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

}
