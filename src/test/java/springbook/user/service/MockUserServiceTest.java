package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static springbook.user.service.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

public class MockUserServiceTest {

    private UserService userService;

    @Mock
    private UserDao userDao;
    @Mock
    private MailSender mailSender;

//    private UserDao userDao = mock(UserDao.class);
//    private MailSender mailSender = mock(MailSender.class);

    private final List<User> users = List.of(
            User.of("bumjin", "박범진", "p1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0, "email1@email.com"),
            User.of("joytouch", "강명성", "p2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0, "email2@email.com"),
            User.of("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "email3@email.com"),
            User.of("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "email4@email.com"),
            User.of("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "email5@email.com")
    );

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserServiceImpl(userDao, mailSender);
    }

    @Test
    void mockUpgradeLevels() throws Exception {
        given(userDao.getAll()).willReturn(users);

        userService.upgradeLevels();

        verify(userDao, times(2)).update(any(User.class));
        verify(userDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isSameAs(Level.SILVER);
        verify(userDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isSameAs(Level.GOLD);

        final ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(mailMessageArg.capture());

        final List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

}
