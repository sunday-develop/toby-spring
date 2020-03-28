package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("서비스 추상화")
@SpringBootTest
public class ServiceTest {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @DisplayName("유저 중복 예외")
    @Test
    void duplicateUser() {
        assertThrows(DuplicateKeyException.class, () -> {
            userDao.add(new User("1", "name", "123"));
            userDao.add(new User("1", "name", "123"));
        });

    }
}
