package chap5.application;

import chap5.domain.Level;
import chap5.domain.User;
import chap5.domain.UserDao;

import java.util.List;

import static chap5.domain.Level.*;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level userLevel = user.getLevel();

        switch (userLevel) {
            case BASIC:
                return user.getLogin() >= 50;
            case SILVER:
                return user.getRecommend() >= 30;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + userLevel);
        }
    }

    private void upgradeLevel(User user) {
        if (user.getLevel() == BASIC) user.setLevel(SILVER);
        else if (user.getLevel() == SILVER) user.setLevel(GOLD);
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(BASIC);
        userDao.add(user);
    }
}
