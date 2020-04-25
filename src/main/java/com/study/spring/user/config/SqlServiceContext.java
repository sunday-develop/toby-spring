package com.study.spring.user.config;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.sqlservice.OxmSqlService;
import com.study.spring.user.sqlservice.SqlRegistry;
import com.study.spring.user.sqlservice.SqlService;
import com.study.spring.user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@Configuration
public class SqlServiceContext {

    @Autowired
    private SqlMapConfig sqlMapconfig;

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setSqlMap(sqlMapconfig.getSqlMapResource());
        return sqlService;
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
        marshaller.setContextPath("com.study.spring.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseFactory().getDatabase();
    }
}
