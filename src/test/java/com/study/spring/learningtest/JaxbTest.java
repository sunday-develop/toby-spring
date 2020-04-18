package com.study.spring.learningtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JaxbTest {

    private File sqlMapFile;

    @BeforeEach
    void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        sqlMapFile = new File(Objects.requireNonNull(classLoader.getResource("learningtest/sqlmap.xml")).getFile());
    }

    @Test
    void readSqlMap() throws JAXBException, IOException {

        String contextPath = jaxb.Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        jaxb.Sqlmap sqlmap = (jaxb.Sqlmap) unmarshaller.unmarshal(sqlMapFile);

        List<jaxb.SqlType> sqlTypeList = sqlmap.getSql();

        assertEquals(sqlTypeList.size(), 3);
        assertEquals(sqlTypeList.get(0).getKey(), "add");
        assertEquals(sqlTypeList.get(0).getValue(), "insert");
        assertEquals(sqlTypeList.get(1).getKey(), "get");
        assertEquals(sqlTypeList.get(1).getValue(), "select");
        assertEquals(sqlTypeList.get(2).getKey(), "delete");
        assertEquals(sqlTypeList.get(2).getValue(), "delete");
    }
}
