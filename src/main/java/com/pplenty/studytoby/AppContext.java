package com.pplenty.studytoby;

import com.pplenty.studytoby.sqlservice.SqlServiceContext;
import org.mariadb.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by yusik on 2020/04/26.
 */
@PropertySource("classpath:database.properties")
@Import({SqlServiceContext.class, AppContext.ProductionAppContext.class})
@ComponentScan(basePackages = "com.pplenty.studytoby")
@EnableTransactionManagement
@Configuration
public class AppContext {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        try {
            dataSource.setDriverClass((Class<? extends java.sql.Driver>) Class.forName(env.getProperty("db.driverClass")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Bean
    public UserLevelUpgradePolicy policy() {
        return new UserLevelUpgradeEventPolicy();
    }

    @Configuration
    @Profile("prod")
    public static class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("alt1.gmail-smtp-in.l.google.com");
            return mailSender;
        }
    }

}

