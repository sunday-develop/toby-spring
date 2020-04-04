package chap5.config;

import chap5.application.UserService;
import chap5.domain.UserDao;
import chap5.infra.UserDaoJdbc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationContextConfig {

    @Bean
    public DataSource dataSource(@Value("${toby-spring.study.datasource.jdbcUrl}") String url,
                                 @Value("${toby-spring.study.datasource.username}") String username,
                                 @Value("${toby-spring.study.datasource.password}") String password) {

        return new SingleConnectionDataSource(url, username, password, true);
    }

    @Bean
    public UserService userService() {
        return new UserService(userDao());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource(null, null, null));
    }

}
