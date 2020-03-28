package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.config.TestConfig;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static springbook.user.service.UserService.MIN_LOG_COUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final List<User> users = List.of(
            User.of("bumjin", "박범진", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0),
            User.of("joytouch", "강명성", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0),
            User.of("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
            User.of("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
            User.of("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
    );

    @BeforeEach
    void setup() {
        userDao.deleteAll();
    }

    @Test
    void bean() throws Exception {
        assertThat(userService).isNotNull();
    }

    @Test
    void upgradeLevels() throws Exception {
        users.forEach(userDao::add);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        final UserService testUserService = new TestUserService(userDao, transactionManager, users.get(3).getId());

        users.forEach(userDao::add);

        try {
            testUserService.upgradeLevels();
            fail("TestUSerServiceException expected");
        } catch (TestUserServiceException e) {

        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        final User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isSameAs(user.getLevel().nextLevel().get());
        } else {
            assertThat(userUpdate.getLevel()).isSameAs(user.getLevel());
        }
    }

    @Test
    void add() throws Exception {
        final User userWithLevel = users.get(4);
        final User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        final User userWithLevelRead = userDao.get(userWithLevel.getId());
        final User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isSameAs(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isSameAs(Level.BASIC);
    }

    static class TestUserService extends UserService {

        private String id;

        public TestUserService(UserDao userDao, PlatformTransactionManager transactionManager, String id) {

            super(userDao, transactionManager);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

    }

    private static class TestUserServiceException extends RuntimeException { }

}