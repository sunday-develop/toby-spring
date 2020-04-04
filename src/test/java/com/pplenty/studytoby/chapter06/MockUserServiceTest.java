package com.pplenty.studytoby.chapter06;

import com.pplenty.studytoby.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static com.pplenty.studytoby.UserLevelUpgradeEventPolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static com.pplenty.studytoby.UserLevelUpgradeEventPolicy.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("Mockito")
@SpringBootTest
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
public class MockUserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private UserLevelUpgradePolicy policy;

    @Mock
    private MailSender mailSender;

    private List<User> users;

    @BeforeEach
    void setUp() {
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

    @DisplayName("사용자 레벨 업그레이드 검증")
    @Test
    void mockUpgradeLevels() {

        // given
        when(userDao.getAll()).thenReturn(users);
        when(policy.canUpgradeLevel(users.get(1))).thenReturn(true);
        when(policy.canUpgradeLevel(users.get(3))).thenReturn(true);

        // when
        userService.upgradeLevels();

        // then
        System.out.println(userDao.getAll());

        verify(userDao, times(2)).update(any(User.class));
        verify(userDao).update(users.get(1));
        verify(userDao).update(users.get(3));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailMessageArg.capture());

        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());

    }
}
