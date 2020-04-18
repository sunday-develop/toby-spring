package springbook.user.config;

import com.mysql.cj.jdbc.Driver;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.sqlservice.SimpleSqlService;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Configuration
@EnableTransactionManagement
public class Config {

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public Advisor transactionAdvisor() {
        final AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setAdvice(transactionAdvice());
        pointcutAdvisor.setExpression("bean(*Service)");
        return pointcutAdvisor;
    }

    @Bean
    public Advice transactionAdvice() {
        final DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
        readOnlyAttribute.setReadOnly(true);

        final NameMatchTransactionAttributeSource attributeSource = new NameMatchTransactionAttributeSource();
        attributeSource.addTransactionalMethod("get*", readOnlyAttribute);
        attributeSource.addTransactionalMethod("*", new DefaultTransactionAttribute());

        return new TransactionInterceptor(transactionManager(), attributeSource);
    }

    @Bean
    public TransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl(userDao(), mailSender());
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource(), sqlService());
    }

    @Bean
    public SqlService sqlService() {
        return new SimpleSqlService(sqlMap());
    }

    @Bean
    public Map<String, String> sqlMap() {
        try {
            final Properties sqlProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sql.properties"));
            return sqlProperties.entrySet().stream()
                    .collect(toUnmodifiableMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
        dataSource.setUsername("spring");
        dataSource.setPassword("book");
        return dataSource;
    }

    @Bean
    public MailSender mailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");
        return mailSender;
    }

}
