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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.pplenty.studytoby.UserLevelUpgradeEventPolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pplenty.studytoby.UserLevelUpgradeEventPolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("서비스 추상화")
@SpringBootTest
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MockMailSender mailSender;

    private List<User> users;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        // given fixture
        users = Arrays.asList(
                new User("pplenty", "yusik", "1234", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
                new User("kohyusik", "고유식", "4321",
                        "jason.parsing+sendme@gmail.com", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
                new User("kohyusik1", "고유", "test", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("kohyusik2", "권세희", "test", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("kohyusik3", "유식", "test", Level.GOLD, 100, Integer.MAX_VALUE)
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
    void upgradeLevels() throws SQLException {

        // given
        for (User user : users) {
            userDao.add(user);
        }

        // when
        userService.upgradeLevels();

        // then
        System.out.println(users);
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

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

    @DisplayName("예외 발생 시 작업 취소 여부")
    @Test
    void upgradeAllOrNothing() {
        // given
        for (User user : users) {
            userDao.add(user);
        }
        testUserService.setId(users.get(3).getId());

        // when
        try {
            testUserService.upgradeLevels();
        } catch (TestUserServiceException ignore) {
        }

        // then
        checkLevelUpgraded(users.get(1), false);
    }

    @DisplayName("mock 메일 발송 확인")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void upgradeLevelSendEmail() {

        // given
        for (User user : users) {
            userDao.add(user);
        }

        // when
        userService.upgradeLevels();

        // then
        System.out.println(users);
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mailSender.getRequests();
        System.out.println(requests);
        assertThat(requests.size()).isEqualTo(1);

    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    public static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(Objects.requireNonNull(simpleMessage.getTo())[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            for (SimpleMailMessage simpleMessage : simpleMessages) {
                requests.add(Objects.requireNonNull(simpleMessage.getTo())[0]);
            }
        }
    }
}
