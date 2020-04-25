package com.study.spring.user.sqlservice;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.exception.SqlNotFoundException;
import com.study.spring.user.exception.SqlRetrievalFailureException;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlMapFile(String sqlMapFile) {
        oxmSqlReader.setSqlMapFile(sqlMapFile);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        oxmSqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    private class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private String sqlMapFile;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlMapFile(String sqlMapFile) {
            this.sqlMapFile = sqlMapFile;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(UserDao.class.getResourceAsStream(sqlMapFile));
                jaxb.Sqlmap sqlmap = (jaxb.Sqlmap) unmarshaller.unmarshal(source);

                for (jaxb.SqlType sqlType : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
