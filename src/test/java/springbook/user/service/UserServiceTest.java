package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.config.TestConfig;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static springbook.user.service.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private final List<User> users = List.of(
            User.of("bumjin", "박범진", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "email1@email.com"),
            User.of("joytouch", "강명성", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "email2@email.com"),
            User.of("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "email3@email.com"),
            User.of("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "email4@email.com"),
            User.of("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "email5@email.com")
    );

    @BeforeEach
    void setup() {
        userService.deleteAll();
    }

    @Test
    void bean() throws Exception {
        assertThat(userService).isNotNull();
    }

    @Test
    void upgradeLevels() throws Exception {
        final MockUserDao mockUserDao = new MockUserDao(users);

        final MockMailSender mockMailSender = new MockMailSender();
        final UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao, mockMailSender);

        userServiceImpl.upgradeLevels();

        final List<User> updated = mockUserDao.getUpdated();
        assertThat(updated).hasSize(2);
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

        final List<String> requests = mockMailSender.getRequests();
        assertThat(requests).hasSize(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isSameAs(expectedLevel);
    }

    @Test
    void mockUpgradeLevels() throws Exception {
        final UserDao mockUserDao = mock(UserDao.class);
        given(mockUserDao.getAll()).willReturn(users);

        final MockMailSender mockMailSender = mock(MockMailSender.class);
        final UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao, mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
//        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isSameAs(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isSameAs(Level.GOLD);

        final ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());

        final List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    @Test
    void upgradeAllOrNothing() throws Exception {
        users.forEach(userService::add);

        try {
            testUserService.upgradeLevels();
            fail("TestUSerServiceException expected");
        } catch (RuntimeException e) {

        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        final User userUpdate = userService.get(user.getId());
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

        final User userWithLevelRead = userService.get(userWithLevel.getId());
        final User userWithoutLevelRead = userService.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isSameAs(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isSameAs(Level.BASIC);
    }

    @Test
    void advisorAutoProxyCreator() throws Exception {
        assertThat(testUserService).isInstanceOf(Proxy.class);
    }

    @Test
    void readOnlyTransactionAttribute() throws Exception {
        users.forEach(userService::add);

        assertThatThrownBy(() -> testUserService.getAll())
                .isInstanceOf(TransientDataAccessResourceException.class);
    }

    @Test
    void transactionSync() throws Exception {
        final DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        final TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        try {
            userService.deleteAll();
            userService.add(users.get(0));
            userService.add(users.get(1));
        } finally {
            transactionManager.rollback(txStatus);
        }
    }

    /////////////////////////////////////////////
    public static class TestUserService extends UserServiceImpl {

        private final String id = "madnite1";

        public TestUserService(UserDao userDao, MailSender mailSender) {
            super(userDao, mailSender);
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }

    }

    private static class TestUserServiceException extends RuntimeException { }

    private static class MockMailSender implements MailSender {

        private final List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException { }

    }

    private static class MockUserDao implements UserDao {

        private final List<User> users;
        private final List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

    }

}