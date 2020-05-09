package com.toby.tobyspring.config;

import com.toby.tobyspring.issuetracker.sqlservice.EmbeddedDbSqlRegistry;
import com.toby.tobyspring.user.dao.UserDao;
import com.toby.tobyspring.user.dao.UserDaoJdbc;
import com.toby.tobyspring.user.service.*;
import com.toby.tobyspring.user.sqlservice.OxmSqlService;
import com.toby.tobyspring.user.sqlservice.SqlRegistry;
import com.toby.tobyspring.user.sqlservice.SqlService;
import oracle.jdbc.driver.OracleDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {

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

    @Bean
    public UserDao userDao() {
        UserDaoJdbc dao = new UserDaoJdbc();
        dao.setDataSource(dataSource());
        dao.setSqlService(sqlService());
        return dao;
    }

    @Bean
    public UserService userService() {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserDao(userDao());
        service.setUserUpgradePolicy(defaultUserUpgradePolicy());
        service.setMailSender(mailSender());
        return service;
    }

    @Bean
    public DefaultUserUpgradePolicy defaultUserUpgradePolicy() {
        return new DefaultUserUpgradePolicy();
    }

    @Bean
    public UserService testUserService() {
        UserServiceTest.TestUserServiceImpl testUserService = new UserServiceTest.TestUserServiceImpl();
        testUserService.setUserDao(userDao());
        testUserService.setUserUpgradePolicy(defaultUserUpgradePolicy());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(HSQL)
                .addScript("classpath:sql/schema.sql")
                .build();
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.toby.tobyspring.user.sqlservice.jaxb");
        return marshaller;
    }

}
