package com.study.spring.user.service;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.domain.Level;
import com.study.spring.user.domain.User;

import java.util.List;

public class UserService {

    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void upgradeLevels() {
        List<User> userList = userDao.getAll();
        for (User user : userList) {
            if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                userLevelUpgradePolicy.upgradeLevel(user);
                userDao.update(user);
            }
        }
    }

    public void add(User user) {

        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }
}
