package springbook.learningtest.oxm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@Configuration
public class OxmTestConfig {

    @Bean
    public Unmarshaller unmarshaller() {
        final Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return jaxb2Marshaller;
    }

}
