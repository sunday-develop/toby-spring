package com.pplenty.studytoby.chapter04;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by yusik on 2020/03/22.
 */
@DisplayName("예외")
@SpringBootTest
@ContextConfiguration("classpath:test-applicationContext.xml")
public class ExceptionTest {

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

    @DisplayName("스프링 예외 전환")
    @Test
    void duplicateUserException() {

        try {

            userDao.add(new User("1", "name", "123"));
            userDao.add(new User("1", "name", "123"));

        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            SQLException sqlEx = (SQLException) ex.getRootCause();
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            assertThat(translator.translate(null, null, sqlEx))
                    .isInstanceOf(DuplicateKeyException.class);
        }


    }

}
