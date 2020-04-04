package chap5.config;

import chap5.domain.UserDao;
import chap5.infra.UserDaoJdbc;
import chap5.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
public class TestApplicationContextConfig {

    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource("jdbc:mysql://127.0.0.1:3306/springbook", "root", "test", true);
    }

    @Bean
    public UserService userService() {
        return new UserService(userDao());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource());
    }

}
