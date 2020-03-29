package com.study.spring.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @DisplayName("업그레이드 레벨")
    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();

        for (Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }

            user.setLevel(level);
            user.upgradeLevel();
            assertEquals(user.getLevel(), level.nextLevel());
        }
    }

    @DisplayName("업그레이드 레벨 Exception")
    @Test
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
            user.setLevel(level);
            assertThrows(IllegalStateException.class, () -> user.upgradeLevel());
        }
    }

}
