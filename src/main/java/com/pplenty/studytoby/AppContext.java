package com.pplenty.studytoby;

import com.pplenty.studytoby.sqlservice.SqlServiceContext;
import org.mariadb.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@PropertySource("classpath:database.properties")
@Import({SqlServiceContext.class, AppContext.ProductionAppContext.class})
@ComponentScan(basePackages = "com.pplenty.studytoby")
@EnableTransactionManagement
@Configuration
public class AppContext {

    @Value("${db.driverClass}")
    Class<? extends Driver> driverClass;

    @Value("${db.url}")
    String url;

    @Value("${db.username}")
    String username;

    @Value("${db.password}")
    String password;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Bean
    public DataSource dataSource() {
        // PropertyPlaceholderAutoConfiguration
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
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

