package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DisplayName("userService test")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("dahye", "김다혜", "p1", Grade.BASIC, DefaultUserUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("toby", "토비", "p2", Grade.BASIC, DefaultUserUpgradePolicy.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("white", "흰", "p3", Grade.SILVER, 60, DefaultUserUpgradePolicy.MIN_RECOMMEND_FOR_GOLD - 1),
                new User("black", "검", "p4", Grade.SILVER, 60, DefaultUserUpgradePolicy.MIN_RECOMMEND_FOR_GOLD),
                new User("yellow", "노랑", "p5", Grade.GOLD, 100, Integer.MAX_VALUE)
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
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        userService.upgrades();

        checkGrade(users.get(0), false);
        checkGrade(users.get(1), true);
        checkGrade(users.get(2), false);
        checkGrade(users.get(3), true);
        checkGrade(users.get(4), false);
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
}