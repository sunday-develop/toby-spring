package springbook.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.config.TestConfig;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    private final List<User> users = List.of(
            User.of("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
            User.of("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
            User.of("erwins", "신승한", "p3", Level.SILVER, 60, 29),
            User.of("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
            User.of("green", "오민규", "p5", Level.GOLD, 100, 100)
    );

    @Test
    void bean() throws Exception {
        assertThat(userService).isNotNull();
    }

}