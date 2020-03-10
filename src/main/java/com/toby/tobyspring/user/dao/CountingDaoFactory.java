package com.toby.tobyspring.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class CountingDaoFactory {
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(connectionMaker());
        return userDao;
    }

    @Bean
    public DataSource connectionMaker() {
        CountingConnectionMaker connectionMaker = new CountingConnectionMaker();
        connectionMaker.setDataSource(dataSource());

        return connectionMaker.makeNewConnection();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(oracle.jdbc.driver.OracleDriver.class);
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        dataSource.setUsername("dahye");
        dataSource.setPassword("test");

        return dataSource;
    }
}
