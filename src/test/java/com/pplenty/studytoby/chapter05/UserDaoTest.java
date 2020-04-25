package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.Level;
import com.pplenty.studytoby.TestAppContext;
import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("서비스 추상화")
@SpringBootTest
@ContextConfiguration(classes = TestAppContext.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        // given fixture
        user1 = new User("pplenty", "yusik", "1234", Level.BASIC, 1, 0);
        user2 = new User("kohyusik", "usik", "4321", Level.SILVER, 55, 10);
    }

    @DisplayName("추가필드 적용")
    @Test
    void addAndGet() {

        // given
        userDao.add(user1);
        userDao.add(user2);

        // when
        User userGet1 = userDao.get(user1.getId());
        User userGet2 = userDao.get(user2.getId());

        System.out.println(userGet1);
        System.out.println(user1);

        // then
        checkSameUser(userGet1, user1);
        checkSameUser(userGet2, user2);

    }

    @DisplayName("사용자 수정")
    @Test
    void update() {

        // given
        userDao.add(user1);
        userDao.add(user2);

        // when
        user1.setName("고유식");
        user1.setPassword("studyhard");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        // then
        User userGet1 = userDao.get(user1.getId());
        checkSameUser(userGet1, user1);
        User userGet2 = userDao.get(user2.getId());
        checkSameUser(userGet2, user2);

    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }
}
