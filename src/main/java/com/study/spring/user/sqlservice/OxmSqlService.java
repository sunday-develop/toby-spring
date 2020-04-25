package com.study.spring.user.sqlservice;

import com.study.spring.user.dao.UserDao;
import com.study.spring.user.exception.SqlRetrievalFailureException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService {

    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlMap(Resource sqlmap) {
        oxmSqlReader.setSqlmap(sqlmap);
    }

    @PostConstruct
    public void loadSql() {
        baseSqlService.setSqlReader(oxmSqlReader);
        baseSqlService.setSqlRegistry(sqlRegistry);
        baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return baseSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader {

        private Resource sqlmap = new ClassPathResource("sqlmap/sqlmap.xml", UserDao.class);
        private Unmarshaller unmarshaller;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(sqlmap.getInputStream());
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
