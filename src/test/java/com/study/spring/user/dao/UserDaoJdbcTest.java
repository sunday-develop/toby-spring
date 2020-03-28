package com.study.spring.user.dao;

import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import com.study.spring.user.exception.DuplicationUserIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-test.xml")
public class UserDaoJdbcTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        user1 = new User("user1", "username1", "username11", Level.BASIC, 1, 0, "user1@naver.com");
        user2 = new User("user2", "username2", "username22", Level.SILVER, 55, 10, "user2@naver.com");
        user3 = new User("user3", "username3", "username33", Level.GOLD, 100, 40, "user3@naver.com");

        System.out.println(this.context);
        System.out.println(this);
    }

    @DisplayName("getAll()에 대한 테스트")
    @Test
    public void getAll() {
        userDao.deleteAll();

        List<User> userList0 = userDao.getAll();
        assertEquals(userList0.size(), 0);

        userDao.add(user1);
        List<User> userList1 = userDao.getAll();
        assertEquals(userList1.size(), 1);
        checkSameUser(user1, userList1.get(0));

        userDao.add(user2);
        List<User> userList2 = userDao.getAll();
        assertEquals(userList2.size(), 2);
        checkSameUser(user1, userList2.get(0));
        checkSameUser(user2, userList2.get(1));

        userDao.add(user3);
        List<User> userList3 = userDao.getAll();
        assertEquals(userList3.size(), 3);
        checkSameUser(user1, userList3.get(0));
        checkSameUser(user2, userList3.get(1));
        checkSameUser(user3, userList3.get(2));

    }

    @DisplayName("add() 메소드와 get() 메소드에 대한 테스트")
    @Test
    public void addAndGet() {

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        userDao.add(user2);
        assertEquals(userDao.getCount(), 2);

        User userTarget1 = userDao.get(user1.getId());
        checkSameUser(userTarget1, user1);

        User userTarget2 = userDao.get(user2.getId());
        checkSameUser(userTarget2, user2);
    }

    @DisplayName("get() 메소드의 예외 상황에 대한 테스트")
    @Test
    public void getUserFailure() {

        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown"));
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }

    public void useUserDaoAddMethod() throws DuplicateKeyException {
        try {
            userDao.add(new User());
        } catch (DuplicateKeyException e) {
            throw new DuplicationUserIdException(e); // 예외를 전환할 때는 원인이 되는 예외를 중첩하는 것이 좋다.
        }
    }

    @DisplayName("DataAcessException에 대한 테스트")
    @Test
    public void duplicateKey() {

        userDao.deleteAll();

        userDao.add(user1);
        assertThrows(DataAccessException.class, () -> userDao.add(user1));
    }

    @DisplayName("SQLException 전환 기능의 학습 테스트")
    @Test
    public void sqlExceptionTranslate() {
        userDao.deleteAll();

        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateKeyException e) {

            SQLException sqlException = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlException)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    @DisplayName("보완된 update() 테스트")
    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("updated");
        user1.setPassword("updated");
        user1.setLevel(Level.GOLD);
        user1.setLogin(10000);
        user1.setRecommend(9999);

        userDao.update(user1);

        User user1Update = userDao.get(user1.getId());
        checkSameUser(user1, user1Update);
        User user2Same = userDao.get(user2.getId());
        checkSameUser(user2, user2Same);
    }
}
