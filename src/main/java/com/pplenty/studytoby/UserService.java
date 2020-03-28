package com.pplenty.studytoby;

import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserService {

    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;

    public UserService(UserDao userDao, UserLevelUpgradePolicy policy) {
        this.userDao = userDao;
        this.policy = policy;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            upgradeLevel(user);
        }
    }

    public void upgradeLevel(User user) {
        if (policy.canUpgradeLevel(user)) {
            policy.upgradeLevel(user);
            userDao.update(user);
        }
    }
}
