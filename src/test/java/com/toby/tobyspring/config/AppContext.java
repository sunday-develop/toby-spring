package com.toby.tobyspring.config;

import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.service.DefaultUserUpgradePolicy;
import com.toby.tobyspring.user.service.DummyMailSender;
import com.toby.tobyspring.user.service.UserService;
import com.toby.tobyspring.user.service.UserServiceTest;
import oracle.jdbc.driver.OracleDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.toby.tobyspring")
@Import(SqlServiceContext.class)
public class AppContext {

    @Autowired
    UserDao userDao;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(OracleDriver.class);
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        dataSource.setUsername("dahye");
        dataSource.setPassword("test");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Configuration
    @Profile("production")
    public class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
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

}
