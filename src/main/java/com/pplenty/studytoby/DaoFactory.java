package com.pplenty.studytoby;

import com.pplenty.studytoby.chapter03.JdbcContext;
import org.mariadb.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * Created by yusik on 2020/03/13.
 */
@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(new JdbcContext(), dataSource());
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost:63306/toby");
        dataSource.setUsername("jason");
        dataSource.setPassword("qwe123");
        return dataSource;
    }

}
