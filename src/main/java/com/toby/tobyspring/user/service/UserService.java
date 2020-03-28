package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;

import java.util.List;

public class UserService implements UserGradeUpgradePolicy {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgrades() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgrade(user)) {
                upgrade(user);
            }
        }
    }

    private boolean canUpgrade(User user) {
        Grade currentGrade = user.getGrade();
        switch (currentGrade) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecomend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Grade: " + currentGrade);
        }
    }

    private void upgrade(User user) {
        user.upgrade();
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
