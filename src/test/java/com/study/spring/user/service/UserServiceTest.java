package com.study.spring.user.service;

import com.study.spring.user.dao.MockUserDao;
import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.study.spring.user.service.DefaultUserLevelUpgradePolicy.MIN_LOG_COUNT_FOR_SILVER;
import static com.study.spring.user.service.DefaultUserLevelUpgradePolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-test.xml")
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private List<User> userList;

    @BeforeEach
    public void setUp() {
        userList = Arrays.asList(
                new User("user1", "username1", "password1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "vvshinevv@naver.com"),
                new User("user2", "username2", "password2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "vvshinevv@naver.com"),
                new User("user3", "username3", "password3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "vvshinevv@naver.com"),
                new User("user4", "username4", "password4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "vvshinevv@naver.com"),
                new User("user5", "username5", "password5", Level.GOLD, 100, Integer.MAX_VALUE, "vvshinevv@naver.com")
        );
    }

    @Test
    public void bean() {
        assertNotNull(this.userServiceImpl);
    }

    @DisplayName("레벨 업그레이드 하는 부분")
    @Test
    public void upgradeLevels() {

        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(userList);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        UserLevelUpgradePolicy userLevelUpgradePolicy = new DefaultUserLevelUpgradePolicy();
        userServiceImpl.setUserLevelUpgradePolicy(userLevelUpgradePolicy);

        userServiceImpl.upgradeLevels();

        List<User> updatedList = mockUserDao.getUpdatedList();
        assertEquals(updatedList.size(), 2);
        checkUserAndLevel(updatedList.get(0), "user2", Level.SILVER);
        checkUserAndLevel(updatedList.get(1), "user4", Level.GOLD);

        List<String> requestList = mockMailSender.getRequestList();
        assertEquals(requestList.size(), 2);
        assertEquals(requestList.get(0), userList.get(1).getEmail());
        assertEquals(requestList.get(1), userList.get(3).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(updated.getId(), expectedId);
        assertEquals(updated.getLevel(), expectedLevel);
    }

    @DisplayName("add() 메소드의 테스트")
    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = userList.get(4);
        User userWithoutLevel = userList.get(0);
        userWithLevel.setLevel(null);

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
        assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
    }

    @DisplayName("예외 발생 시 작업 취소 여부 테스트")
    @Test
    public void upgradeAllOrNothingWithException() {

        UserLevelUpgradePolicy userLevelUpgradePolicy = new TestUserLevelUpgradePolicy(userList.get(3).getId());

        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(this.userDao);
        userServiceImpl.setMailSender(new MockMailSender());
        userServiceImpl.setUserLevelUpgradePolicy(userLevelUpgradePolicy);

        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(this.transactionManager);
        userServiceTx.setUserService(userServiceImpl);

        userDao.deleteAll();
        for (User user : userList) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, userServiceTx::upgradeLevels);
        checkLevelUpgraded(userList.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel());
        } else {
            assertEquals(userUpdate.getLevel(), user.getLevel());
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
            System.out.println();

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


