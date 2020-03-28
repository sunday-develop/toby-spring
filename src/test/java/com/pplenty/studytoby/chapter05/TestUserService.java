package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import com.pplenty.studytoby.UserLevelUpgradePolicy;
import com.pplenty.studytoby.UserService;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by yusik on 2020/03/28.
 */
public class TestUserService extends UserService {

    private String id;

    public TestUserService(PlatformTransactionManager transactionManager, UserDao userDao, UserLevelUpgradePolicy policy) {
        super(transactionManager, userDao, policy);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void upgradeLevel(User user) {
        if (user.getId().equals(id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
