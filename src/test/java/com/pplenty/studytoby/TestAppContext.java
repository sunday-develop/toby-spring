package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter05.TestUserService;
import com.pplenty.studytoby.chapter05.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by yusik on 2020/04/26.
 */
@ComponentScan(basePackages = "com.pplenty.studytoby")
@EnableTransactionManagement
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

