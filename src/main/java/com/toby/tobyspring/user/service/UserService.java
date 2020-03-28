package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService {
    UserDao userDao;
    UserUpgradePolicy userUpgradePolicy;
    private PlatformTransactionManager transactionManager;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserUpgradePolicy(UserUpgradePolicy userUpgradePolicy) {
        this.userUpgradePolicy = userUpgradePolicy;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgrades() {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (userUpgradePolicy.canUpgrade(user)) {
                    upgrade(user);
                }
            }
            this.transactionManager.commit(transactionStatus);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    protected void upgrade(User user) {
        user.upgrade();
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getGrade() == null) user.setGrade(Grade.BASIC);
        userDao.add(user);
    }
}
