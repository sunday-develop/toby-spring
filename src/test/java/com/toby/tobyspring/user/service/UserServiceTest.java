package com.toby.tobyspring.user.service;

import com.toby.tobyspring.config.AppContext;
import com.toby.tobyspring.config.TestAppContext;
import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = AppContext.class)
@DisplayName("userService test")
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    @Autowired
    ApplicationContext context;

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

    public static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "dblack";

        @Override
        public void upgrade(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgrade(user);
        }

        @Override
        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }

            return super.getAll();
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    @DisplayName("예외 발생 시 작업 취소 여부 테스트")
    @Test
    public void upgradeAllOrNothing() {
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        try {
            testUserService.upgrades();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkGrade(users.get(1), false);
    }

    @DisplayName("읽기 전용 속성 테스트 - oracle에서는 정상동작 하지 않음")
    @Disabled
    @Test
    public void readOnlyTransactionAttribute() {
        assertThrows(TransientDataAccessResourceException.class, () -> {
            testUserService.getAll();
        });
    }

    @DisplayName("트랜잭션 메니저를 이용한 트랜잭션 제어")
    @Test
    public void transactionSync() {
        userService.deleteAll();
        assertEquals(0, userDao.getCount());

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        userService.add(users.get(0));
        userService.add(users.get(1));
        assertEquals(2, userDao.getCount());

        transactionManager.rollback(txStatus);
        assertEquals(0, userDao.getCount());
    }

    @DisplayName("테스트 코드에서의 @Transactional 어노테이션")
    @Test
    @Transactional
    public void transactionAnnotationInTestCode() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }
}