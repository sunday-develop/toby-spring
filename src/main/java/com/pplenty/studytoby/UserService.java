package com.pplenty.studytoby;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserService {

    private final DataSource dataSource;
    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;

    public UserService(DataSource dataSource, UserDao userDao, UserLevelUpgradePolicy policy) {
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.policy = policy;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();
        Connection con = DataSourceUtils.getConnection(dataSource);
        con.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                upgradeLevel(user);
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void upgradeLevel(User user) {
        if (policy.canUpgradeLevel(user)) {
            policy.upgradeLevel(user);
            userDao.update(user);
        }
    }
}
