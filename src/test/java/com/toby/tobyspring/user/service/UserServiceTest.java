package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DisplayName("userService test")
class UserServiceTest {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("adahye", "김다혜", "p1", Grade.BASIC, DefaultUserUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER - 1, 0, "dahye@nav.com"),
                new User("btoby", "토비", "p2", Grade.BASIC, DefaultUserUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER, 0, "toby@nav.com"),
                new User("cwhite", "흰", "p3", Grade.SILVER, 60, DefaultUserUpgradePolicy.MIN_RECOMMEND_FOR_GOLD - 1, "white@nav.com"),
                new User("dblack", "검", "p4", Grade.SILVER, 60, DefaultUserUpgradePolicy.MIN_RECOMMEND_FOR_GOLD, "black@nav.com"),
                new User("eyellow", "노랑", "p5", Grade.GOLD, 100, Integer.MAX_VALUE, "yellow@nav.com")
        );
    }

    @Test
    @DisplayName("bean이 null이 아닌가 ? null이 아니라면, 빈 생성 정상적으로 된 것")
    public void bean() {
        assertNotNull(this.userService);
    }

    @Test
    @DisplayName("사용자 레벨 업그레이드 테스트")
    public void upgrades() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserUpgradePolicy(new DefaultUserUpgradePolicy());

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userService.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userService.setMailSender(mockMailSender);

        userService.upgrades();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(this.users.get(1));
        assertEquals(users.get(1).getGrade(), Grade.SILVER);
        verify(mockUserDao).update(this.users.get(3));
        assertEquals(users.get(3).getGrade(), Grade.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArgumentCaptor.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
        assertEquals(users.get(1).getEmail(), mailMessages.get(0).getTo()[0]);
        assertEquals(users.get(3).getEmail(), mailMessages.get(1).getTo()[0]);
    }

    private void checkUserAndGrade(User updated, String expectedId, Grade expectedGrade) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedGrade, updated.getGrade());
    }

    private void checkGrade(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(user.getGrade().nextGrade(), userUpdate.getGrade());
        } else {
            assertEquals(user.getGrade(), userUpdate.getGrade());
        }
    }

    @Test
    @DisplayName("add() 메소드 테스트")
    public void add() {
        userDao.deleteAll();

        User userWithGrade = users.get(4);
        User userWithoutGrade = users.get(0);
        userWithoutGrade.setGrade(null);

        userService.add(userWithGrade);
        userService.add(userWithoutGrade);

        User userWithGradeRead = userDao.get(userWithGrade.getId());
        User userWithoutGradeRead = userDao.get(userWithoutGrade.getId());

        assertEquals(userWithGrade.getGrade(), userWithGradeRead.getGrade());
        assertEquals(Grade.BASIC, userWithoutGradeRead.getGrade());
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        public void upgrade(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgrade(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @DisplayName("예외 발생 시 작업 취소 여부 테스트")
    @Test
    public void upgradeAllOrNothing() {
        TestUserService userService = new TestUserService(users.get(3).getId());
        userService.setUserDao(this.userDao);
        userService.setUserUpgradePolicy(new DefaultUserUpgradePolicy());
        userService.setMailSender(mailSender);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(userService);
        txHandler.setPlatformTransactionManager(transactionManager);
        txHandler.setPattern("upgrades");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[]{UserService.class}, txHandler);

        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        try {
            txUserService.upgrades();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkGrade(users.get(1), false);
    }
}