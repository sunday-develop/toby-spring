package com.toby.tobyspring.config;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.service.DefaultUserUpgradePolicy;
import com.toby.tobyspring.user.service.DummyMailSender;
import com.toby.tobyspring.user.service.UserService;
import com.toby.tobyspring.user.service.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
public class TestAppContext {
    @Autowired
    UserDao userDao;

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserServiceImpl();
    }

    @Bean
    public DefaultUserUpgradePolicy defaultUserUpgradePolicy() {
        return new DefaultUserUpgradePolicy();
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
