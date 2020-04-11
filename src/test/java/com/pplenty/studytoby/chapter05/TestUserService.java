package com.pplenty.studytoby.chapter05;

import com.pplenty.studytoby.User;
import com.pplenty.studytoby.UserDao;
import com.pplenty.studytoby.UserLevelUpgradePolicy;
import com.pplenty.studytoby.UserServiceImpl;
import org.springframework.mail.MailSender;

import java.util.List;

/**
 * Created by yusik on 2020/03/28.
 */
public class TestUserService extends UserServiceImpl {

    private String id;

    public TestUserService(UserDao userDao,
                           UserLevelUpgradePolicy policy,
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

    @Override
    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user);
        }
        return null;
    }
}
