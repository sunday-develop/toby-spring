package com.study.spring.learningtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-bean.xml")
public class OxmTest {

    @Autowired
    private Unmarshaller unMarshaller;

    @Test
    void unMarshallSqlMap() throws XmlMappingException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        Source xmlSource = new StreamSource(classLoader.getResourceAsStream("learningtest/sqlmap.xml"));

        jaxb.Sqlmap sqlMap = (jaxb.Sqlmap) unMarshaller.unmarshal(xmlSource);

        List<jaxb.SqlType> sqlTypeList = sqlMap.getSql();
        assertEquals(sqlTypeList.size(), 3);

    }
}
