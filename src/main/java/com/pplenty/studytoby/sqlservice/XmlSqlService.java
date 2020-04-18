package com.pplenty.studytoby.sqlservice;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yusik on 2020/04/18.
 */
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private final Map<String, String> sqlMap = new HashMap<>();

    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
    private String sqlMapFile;

    public void setSqlMapFile(String sqlMapFile) {
        this.sqlMapFile = sqlMapFile;
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlRegistry.findSql(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlRetrievalFailureException {
        return sqlMap.get(key);
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        try {
            String contextPath = Sqlmap.class.getPackage().getName();
            JAXBContext context;
            context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileInputStream(new File(this.sqlMapFile)));

            List<SqlType> sqlList = sqlmap.getSql();
            for (SqlType sqlType : sqlList) {
                sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
