package com.pplenty.studytoby.chapter03;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/21.
 */
@DisplayName("템플릿")
@SpringBootTest
@ContextConfiguration("classpath:test-applicationContext.xml")
class TemplateTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @DisplayName("모든 사용자 조회")
    @Test
    void getAll() {

        // when
        List<User> users = userDao.getAll();

        // then
        assertThat(users).isEmpty();

    }
}