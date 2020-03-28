package com.toby.tobyspring.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("user의 upgrade() 테스트")
    public void upgrade() {
        Grade[] grades = Grade.values();
        for (Grade grade : grades) {
            if (grade.nextGrade() == null) continue;
            user.setGrade(grade);
            user.upgrade();
            assertEquals(grade.nextGrade(), user.getGrade());
        }
    }

    @Test
    @DisplayName("user의 upgrade() exception 테스트")
    public void cannotUpgrade() {
        Grade[] grades = Grade.values();
        for (Grade grade : grades) {
            if (grade.nextGrade() != null) continue;
            user.setGrade(grade);
            assertThrows(IllegalArgumentException.class, () -> {
                user.upgrade();
            });
        }
    }
}