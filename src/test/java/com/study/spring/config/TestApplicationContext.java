package com.study.spring.config;

import com.study.spring.factorybean.MessageFactoryBean;
import com.study.spring.user.service.UserService;
import com.study.spring.user.service.UserServiceTest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class TestApplicationContext {

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserService();
    }

    @Bean
    public MailSender mailSender() {
        return new UserServiceTest.MockMailSender();
    }

    @Bean
    public UserServiceTest.TestUserLevelUpgradePolicy testUserLevelUpgradePolicy() {
        return new UserServiceTest.TestUserLevelUpgradePolicy("user4");
    }

    @Bean
    public FactoryBean message() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }
}
