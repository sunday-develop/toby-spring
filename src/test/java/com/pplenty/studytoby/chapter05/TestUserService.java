package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import com.pplenty.studytoby.UserLevelUpgradePolicy;
import com.pplenty.studytoby.UserServiceImpl;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by yusik on 2020/03/28.
 */
public class TestUserService extends UserServiceImpl {

    private String id;

    public TestUserService(UserDao userDao,
                           UserLevelUpgradePolicy policy,
                           PlatformTransactionManager transactionManager,
                           MailSender mailSender) {
        super(userDao, policy, mailSender);
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
