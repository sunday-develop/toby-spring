package chap5;

import chap5.application.UserService;
import chap5.config.ApplicationContextConfig;
import chap5.domain.Level;
import chap5.domain.User;
import chap5.domain.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContextConfig.class)
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Fixture.getUsersForUpgradeLevelsTest();
    }

    @DisplayName("userService 빈이 정상적으로 주입되었다면, true 를 반환한다.")
    @Test
    void bean() {
        assertThat(this.userService).isNotNull();
    }

    @DisplayName("사용자가 레벨 업그레이드 정책에 부합한다면, 사용자 레벨을 업그레이드 한다.")
    @Test
    void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    // 헬퍼 메소드
    private void checkLevel(User user, Level expectedLevel) {
        User savedUser = userDao.get(user.getId());
        assertThat(savedUser.getLevel()).isEqualTo(expectedLevel);
    }

    @DisplayName("사용자의 레벨이 이미 설정되어 있다면 그대로 유지하고, 레벨이 없다면 디폴트인 BASIC 으로 설정한다.")
    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User savedUserWithLevel = userDao.get(userWithLevel.getId());
        User savedUserWithoutLevel = userDao.get(userWithoutLevel.getId());

        assertThat(savedUserWithLevel.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(savedUserWithoutLevel.getLevel()).isEqualTo(Level.BASIC);
    }

}
