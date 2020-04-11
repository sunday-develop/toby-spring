package springbook.config;

import com.mysql.cj.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import springbook.user.config.Config;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceTest;

import javax.sql.DataSource;

@Import(Config.class)
@Configuration
public class TestConfig {

    @Bean
    public UserService testUserService(UserDao userDao) {
        return new UserServiceTest.TestUserService(userDao, mailSender());
    }

    @Bean
    @Primary
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
        dataSource.setUsername("spring");
        dataSource.setPassword("book");
        return dataSource;
    }

}
