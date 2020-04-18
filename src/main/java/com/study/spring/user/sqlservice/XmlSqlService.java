package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlNotFoundException;
import com.study.spring.user.exception.SqlRetrievalFailureException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private Map<String, String> sqlMap = new HashMap<>();

    private SqlReader sqlReader;

    private SqlRegistry sqlRegistry;

    private String sqlMapFilePath;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlMapFilePath(String sqlMapFilePath) {
        this.sqlMapFilePath = sqlMapFilePath;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {

        String sql = sqlMap.get(key);

        if (StringUtils.isEmpty(sql)) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
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
                sqlMap.put(sqlType.getKey(), sqlType.getValue());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }
}
