package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter05.TestUserService;
import com.pplenty.studytoby.chapter05.UserServiceTest;
import com.pplenty.studytoby.sqlservice.OxmSqlService;
import com.pplenty.studytoby.sqlservice.SqlService;
import org.mariadb.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by yusik on 2020/04/26.
 */
@Configuration
@ImportResource("classpath:test-applicationContext.xml")
public class TestApplicationContext {

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
    public UserDao userDao() {
        UserDaoJdbc dao = new UserDaoJdbc();
        dao.setDataSource(dataSource());
        dao.setSqlService(sqlService());
        return dao;
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userDao(), policy(), mailSender());
    }

    @Bean
    public UserService testUserService() {
        return new TestUserService(userDao(), policy(), mailSender());
    }

    @Bean
    public MailSender mailSender() {
        return new UserServiceTest.MockMailSender();
    }

    @Bean
    public SqlService sqlService() {
        OxmSqlService service = new OxmSqlService();
        service.setUnmarshaller(unmarshaller());
        service.setSqlMapFile("test-applicationContext.xml");
        return service;
    }

    @Bean
    public UserLevelUpgradePolicy policy() {
        return new UserLevelUpgradeEventPolicy();
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.pplenty.studytoby.sqlservice.jaxb");
        return marshaller;
    }

}

