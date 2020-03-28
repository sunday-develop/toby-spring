package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.Level;
import com.pplenty.studytoby.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Created by yusik on 2020/03/28.
 */
@DisplayName("서비스 추상화: User 테스트")
public class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @DisplayName("사용자 레벨 업그레이드")
    @Test
    void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @DisplayName("예외 처리, 사용자 레벨 업그레이드")
    @Test
    void upgradeLevelException() {

        assertThatIllegalStateException()
                .isThrownBy(() -> {
                    Level[] levels = Level.values();
                    for (Level level : levels) {
                        if (level.nextLevel() != null) {
                            continue;
                        }
                        user.setLevel(level);
                        user.upgradeLevel();
                        assertThat(user.getLevel()).isEqualTo(level.nextLevel());
                    }
                });


    }
}
