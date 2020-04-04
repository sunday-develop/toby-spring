package chap5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return new SingleConnectionDataSource("jdbc:mysql://127.0.0.1:3306/springbook", "root", "test", true);
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource());
    }
}
