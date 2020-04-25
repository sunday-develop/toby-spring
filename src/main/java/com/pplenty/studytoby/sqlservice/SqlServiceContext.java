package com.pplenty.studytoby.sqlservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Created by yusik on 2020/04/26.
 */
@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        OxmSqlService service = new OxmSqlService();
        service.setUnmarshaller(unmarshaller());
        service.setSqlMapFile("src/main/resources/di/user-sqlmap.xml");
        return service;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.pplenty.studytoby.sqlservice.jaxb");
        return marshaller;
    }
}
