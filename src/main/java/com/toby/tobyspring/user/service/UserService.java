package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;

import java.util.List;

public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeGrades() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            Boolean changed = null;
            if (Grade.BASIC.equals(user.getGrade()) && user.getLogin() >= 50) {
                user.setGrade(Grade.SILVER);
                changed = true;
            } else if (Grade.SILVER.equals(user.getGrade()) && user.getRecomend() >= 30) {
                user.setGrade(Grade.GOLD);
                changed = true;
            } else if (Grade.GOLD.equals(user.getGrade())) {
                changed = false;
            } else {
                changed = false;
            }

            if (changed) {
                userDao.update(user);
            }
        }
    }

    public void add(User user) {
        if (user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
