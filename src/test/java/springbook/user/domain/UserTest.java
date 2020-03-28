package springbook.user.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private final User user = User.of("id", "name", "pass", Level.BASIC, 0, 0, "email@email.com");

    @ParameterizedTest
    @MethodSource("upgradeLevelParam")
    void upgradeLevel(Level level) throws Exception {
        user.setLevel(level);
        user.upgradeLevel();
        assertThat(user.getLevel()).isSameAs(level.nextLevel().get());
    }

    private static Stream<Level> upgradeLevelParam() {
        return Stream.of(Level.values()).filter(level -> level.nextLevel().isPresent());
    }

    @ParameterizedTest
    @MethodSource("cannotUpgradeLevelParam")
    void cannotUpgradeLevel(Level level) throws Exception {
        user.setLevel(level);
        assertThatThrownBy(user::upgradeLevel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(level + "은 업그레이드가 불가능 합니다.");
    }

    private static Stream<Level> cannotUpgradeLevelParam() {
        return Stream.of(Level.values()).filter(level -> level.nextLevel().isEmpty());
    }

}