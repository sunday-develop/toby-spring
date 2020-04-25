package com.study.spring.user.config;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.service.DefaultUserLevelUpgradePolicy;
import com.study.spring.user.service.UserLevelUpgradePolicy;
import com.study.spring.user.service.UserService;
import com.study.spring.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
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
@Import({SqlServiceContext.class})
@PropertySource("/db/database.properties")
public class AppContext {

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Value("${db.driverClass")
    Class<? extends Driver> driverClass;

    @Value("${db.url")
    String url;

    @Value("${db.username")
    String username;

    @Value("${db.password")
    String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        try {
            dataSource.setDriverClass((Class<? extends java.sql.Driver>) Class.forName(env.getProperty("db.driverClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

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
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.mycompany.com");
        return mailSender;
    }

}
