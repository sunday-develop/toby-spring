package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.*;

import javax.sql.DataSource;

/**
 * Created by yusik on 2020/03/28.
 */
public class TestUserService extends UserService {

    private String id;

    public TestUserService(DataSource dataSource, UserDao userDao, UserLevelUpgradePolicy policy) {
        super(dataSource, userDao, policy);
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
