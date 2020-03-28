package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {

    public static final int MIN_LOG_COUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        for (User user : userDao.getAll()) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        final Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return user.getLogin() >= MIN_LOG_COUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

}
