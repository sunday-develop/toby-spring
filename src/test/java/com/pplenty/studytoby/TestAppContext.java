package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter05.TestUserService;
import com.pplenty.studytoby.chapter05.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

/**
 * Created by yusik on 2020/04/26.
 */
@Profile("test")
@Configuration
public class TestAppContext {

    @Autowired
    UserDao userDao;

    @Autowired
    UserLevelUpgradePolicy policy;

    @Bean
    public UserService testUserService() {
        return new TestUserService(userDao, policy, mailSender());
    }

    @Bean
    public MailSender mailSender() {
        return new UserServiceTest.MockMailSender();
    }

}

