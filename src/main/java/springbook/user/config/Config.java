package springbook.user.config;

import com.mysql.cj.jdbc.Driver;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
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

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Import(SqlServiceConfig.class)
@Configuration
@PropertySource("./database.properties")
@ComponentScan(basePackages = "springbook.user")
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

        return new TransactionInterceptor(transactionManager(null), attributeSource);
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
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
    public DataSource dataSource(@Value("${db.driverClass}") Class<Driver> driverClass,
                                 @Value("${db.url}") String url,
                                 @Value("${db.username}") String username,
                                 @Value("${db.password}") String password) {

        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public MailSender mailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");
        return mailSender;
    }

    @Bean
    public SqlMapConfig sqlMapConfig() {
        return new UserSqlMapConfig();
    }

}
