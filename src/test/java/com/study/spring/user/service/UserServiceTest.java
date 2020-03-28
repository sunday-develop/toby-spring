package com.study.spring.user.service;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
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
@ContextConfiguration(locations = "classpath:spring/applicationContext-test.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> userList;

    @BeforeEach
    public void setUp() {
        userList = Arrays.asList(
                new User("user1", "username1", "password1", Level.BASIC, 49, 0),
                new User("user2", "username2", "password2", Level.BASIC, 50, 0),
                new User("user3", "username3", "password3", Level.SILVER, 60, 29),
                new User("user4", "username4", "password4", Level.SILVER, 60, 30),
                new User("user5", "username5", "password5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void bean() {
        assertNotNull(this.userService);
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();

        for (User user : this.userList) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(userList.get(0), Level.BASIC);
        checkLevel(userList.get(1), Level.SILVER);
        checkLevel(userList.get(2), Level.SILVER);
        checkLevel(userList.get(3), Level.GOLD);
        checkLevel(userList.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertEquals(userUpdate.getLevel(), expectedLevel);
    }
}

