package com.pplenty.studytoby;

import com.pplenty.studytoby.sqlservice.SqlServiceContext;
import org.mariadb.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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
@Import({SqlServiceContext.class, AppContext.ProductionAppContext.class})
@ComponentScan(basePackages = "com.pplenty.studytoby")
@EnableTransactionManagement
@Configuration
public class AppContext {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost:63306/toby");
        dataSource.setUsername("jason");
        dataSource.setPassword("qwe123");
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

