package com.study.spring.user.service;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.study.spring.user.service.DefaultUserLevelUpgradePolicy.MIN_LOG_COUNT_FOR_SILVER;
import static com.study.spring.user.service.DefaultUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring/applicationTestContext-bean.xml", "classpath:spring/applicationTestContext-transaction.xml"})
public class UserServiceTest {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        userList = Arrays.asList(
                new User("user1", "username1", "password1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "vvshinevv@naver.com"),
                new User("user2", "username2", "password2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "vvshinevv@naver.com"),
                new User("user3", "username3", "password3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "vvshinevv@naver.com"),
                new User("user4", "username4", "password4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "vvshinevv@naver.com"),
                new User("user5", "username5", "password5", Level.GOLD, 100, Integer.MAX_VALUE, "vvshinevv@naver.com")
        );
    }

    @DisplayName("레벨 업그레이드 하는 부분")
    @Test
    void upgradeLevels() {

        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(userList);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new DefaultUserLevelUpgradePolicy();
        userServiceImpl.setUserLevelUpgradePolicy(userLevelUpgradePolicy);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(userList.get(1));
        assertEquals(userList.get(1).getLevel(), Level.SILVER);
        verify(mockUserDao).update(userList.get(3));
        assertEquals(userList.get(3).getLevel(), Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArgumentCaptor.capture());

        List<SimpleMailMessage> mailMessageList = mailMessageArgumentCaptor.getAllValues();
        assertEquals(Objects.requireNonNull(mailMessageList.get(0).getTo())[0], userList.get(1).getEmail());
        assertEquals(Objects.requireNonNull(mailMessageList.get(1).getTo())[0], userList.get(3).getEmail());
    }

    @DisplayName("add() 메소드의 테스트")
    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = userList.get(4);
        User userWithoutLevel = userList.get(0);
        userWithLevel.setLevel(null);

        testUserService.add(userWithLevel);
        testUserService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
    }

    @DisplayName("예외 발생 시 작업 취소 여부 테스트")
    @Test
    void upgradeAllOrNothingWithException() {
        userDao.deleteAll();
        for (User user : userList) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, testUserService::upgradeLevels);
        checkLevelUpgraded(userList.get(1), false);
    }

    @Test
    void readOnlyTransactionAttribute() {
        assertThrows(TransientDataAccessResourceException.class, testUserService::getAll);
    }

    @Test
    @Transactional
    @Rollback
    void transactionSync() {

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        try {

            testUserService.deleteAll();
            testUserService.add(userList.get(0));
            testUserService.add(userList.get(1));

        } finally {
            transactionManager.rollback(txStatus);
        }
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(userUpdate.getLevel(), user.getLevel());
        }
    }

    static class TestUserService extends UserServiceImpl {

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }

            return null;
        }
    }

    static class TestUserLevelUpgradePolicy extends DefaultUserLevelUpgradePolicy {

        private String id;

        public TestUserLevelUpgradePolicy(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            user.upgradeLevel();
        }
    }

    private static class TestUserServiceException extends RuntimeException {
    }

    static class MockMailSender implements MailSender {
        private List<String> requestList = new ArrayList<>();

        public List<String> getRequestList() {
            return requestList;
        }

        @Override
        public void send(SimpleMailMessage simpleMailMessage) throws MailException {
            requestList.add(Objects.requireNonNull(simpleMailMessage.getTo())[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

        }

    }
}


