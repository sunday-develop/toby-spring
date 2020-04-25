package com.study.spring.user.config;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.service.DefaultUserLevelUpgradePolicy;
import com.study.spring.user.service.UserLevelUpgradePolicy;
import com.study.spring.user.service.UserService;
import com.study.spring.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.study.spring")
@Import(SqlServiceContext.class)
public class AppContext {

    @Autowired
    private UserDao userDao;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/test?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }


    @Bean
    public UserService userService() {
        UserServiceImpl testUserService = new UserServiceImpl();
        testUserService.setUserDao(userDao);
        testUserService.setUserLevelUpgradePolicy(defaultUserLevelUpgradePolicy());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public UserLevelUpgradePolicy defaultUserLevelUpgradePolicy() {
        return new DefaultUserLevelUpgradePolicy();
    }

    @Bean
    public MailSender mailSender() {
        return new JavaMailSenderImpl();
    }


}
