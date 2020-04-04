package chap5;

import chap5.application.UserService;
import chap5.config.TestApplicationContextConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestApplicationContextConfig.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @DisplayName("userService 빈이 정상적으로 주입되었다면, true 를 반환한다.")
    @Test
    void bean() {
        assertThat(this.userService).isNotNull();
    }
}
