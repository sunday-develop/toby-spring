package com.study.spring.config;

import com.study.spring.factorybean.MessageFactoryBean;
import com.study.spring.user.dao.UserDao;
import com.study.spring.user.service.UserLevelUpgradePolicy;
import com.study.spring.user.service.UserService;
import com.study.spring.user.service.UserServiceImpl;
import com.study.spring.user.service.UserServiceTest;
import com.study.spring.user.sqlservice.OxmSqlService;
import com.study.spring.user.sqlservice.SqlRegistry;
import com.study.spring.user.sqlservice.SqlService;
import com.study.spring.user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.study.spring")
public class TestApplicationContext {

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
    public UserLevelUpgradePolicy testUserLevelUpgradePolicy() {
        return new UserServiceTest.TestUserLevelUpgradePolicy("user4");
    }

    @Bean
    public UserService userService() {
        UserServiceImpl testUserService = new UserServiceImpl();
        testUserService.setUserDao(userDao);
        testUserService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public UserService testUserService() {
        UserServiceTest.TestUserService testUserService = new UserServiceTest.TestUserService();

        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender());

        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new UserServiceTest.MockMailSender();
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.study.spring.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }

    @Bean
    public FactoryBean message() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseFactory().getDatabase();
    }
}
