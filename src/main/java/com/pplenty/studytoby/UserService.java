package com.pplenty.studytoby;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class UserService {

    private final PlatformTransactionManager transactionManager;
    private final UserDao userDao;
    private final UserLevelUpgradePolicy policy;

    public UserService(PlatformTransactionManager transactionManager, UserDao userDao, UserLevelUpgradePolicy policy) {
        this.transactionManager = transactionManager;
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

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        try {
            
            List<User> users = userDao.getAll();
            for (User user : users) {
                upgradeLevel(user);
            }

            // 커밋
            transactionManager.commit(status);
        } catch (Exception e) {
            // 롤백
            transactionManager.rollback(status);
            throw e;
        }
    }

    public void upgradeLevel(User user) {
        if (policy.canUpgradeLevel(user)) {
            policy.upgradeLevel(user);
            userDao.update(user);
        }
    }
}
