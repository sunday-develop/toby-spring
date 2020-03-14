package com.pplenty.studytoby.chapter01.step01;

import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/14.
 */
public class UserDaoTestFromXml {

    @BeforeEach
    void setUp() throws SQLException {

        Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:63306/toby", "jason", "qwe123");

        PreparedStatement ps = con.prepareStatement("truncate toby.users");
        ps.executeUpdate();

    }

    @DisplayName("빈 조회 테스트 - GenericXmlApplicationContext")
    @Test
    void genericXml() {

        // given
        ApplicationContext context = new GenericXmlApplicationContext("/di/applicationContext.xml");

        // when
        UserDao userDao = context.getBean("userDao", UserDao.class);
        UserDao userDao2 = context.getBean("userDao", UserDao.class);

        // then
        assertThat(userDao == userDao2).isTrue();
        assertThat(userDao).isEqualTo(userDao2);
        System.out.println(userDao);
        System.out.println(userDao2);

    }

    @DisplayName("빈 조회 테스트 - ClassPathXmlApplicationContext")
    @Test
    void classPathXml() {

        // given
//        ApplicationContext context = new ClassPathXmlApplicationContext("userDao.xml", UserDao.class);
        ApplicationContext context = new ClassPathXmlApplicationContext("/di/applicationContext.xml");

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
