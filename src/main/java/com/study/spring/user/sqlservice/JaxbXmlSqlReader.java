package com.study.spring.user.sqlservice;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Objects;

public class JaxbXmlSqlReader implements SqlReader {


    private static final String DEFAULT_SQLMAP_FILE_PATH = "sqlmap/sqlmap.xml";

    private String sqlMapFilePath = DEFAULT_SQLMAP_FILE_PATH;

    public void setSqlMapFilePath(String sqlMapFilePath) {
        this.sqlMapFilePath = sqlMapFilePath;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = jaxb.Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);

            ClassLoader classLoader = getClass().getClassLoader();
            File sqlMapFile = new File(Objects.requireNonNull(classLoader.getResource(sqlMapFilePath)).getFile());

            Unmarshaller unmarshaller = context.createUnmarshaller();
            jaxb.Sqlmap jaxbSqlMap = (jaxb.Sqlmap) unmarshaller.unmarshal(sqlMapFile);

            for (jaxb.SqlType sqlType : jaxbSqlMap.getSql()) {
                sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
