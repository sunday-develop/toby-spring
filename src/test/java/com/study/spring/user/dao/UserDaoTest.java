package com.study.spring.user.dao;

import com.study.spring.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("spring/applicationContext.xml");
        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user1 = new User();
        user1.setId("whiteship");
        user1.setName("back");
        user1.setPassword("married");

        userDao.add(user1);

        System.out.println(user1.getId() + " 등록 성공");

        User user2 = userDao.get(user1.getId());

        assertThat(user2.getName()).isEqualTo(user1.getName());
        assertThat(user2.getPassword()).isEqualTo(user1.getPassword());
    }
}
