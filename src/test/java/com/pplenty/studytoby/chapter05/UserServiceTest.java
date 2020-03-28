package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.Level;
import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import com.pplenty.studytoby.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("서비스 추상화")
@SpringBootTest
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        // given fixture
        users = Arrays.asList(
                new User("pplenty", "yusik", "1234", Level.BASIC, 49, 0),
                new User("kohyusik", "usik", "4321", Level.BASIC, 50, 0),
                new User("kohyusik1", "고유식", "test", Level.SILVER, 60, 29),
                new User("kohyusik2", "권세희", "test", Level.SILVER, 60, 30),
                new User("kohyusik3", "유식", "test", Level.GOLD, 100, 100)
        );
    }

    @DisplayName("서비스 빈 주입")
    @Test
    void bean() {
        System.out.println(userService);
        assertThat(userService).isNotNull();
    }

    @DisplayName("사용자 레벨 업그레이드 검증")
    @Test
    void addAndGet() {

        // given
        for (User user : users) {
            userDao.add(user);
        }

        // when
        userService.upgradeLevels();

        // then
        System.out.println(users);
        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);

    }

    @DisplayName("사용자 추가: 경우에 따른 레벨 초기화")
    @Test
    void addLevelInitialize() {

        // given
        User userWithLevel = users.get(4); // GOLD level
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        // when
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        // then
        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);

    }


    private void checkLevel(User user, Level level) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(level);
    }
}
