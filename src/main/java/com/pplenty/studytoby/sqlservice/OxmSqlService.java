package com.pplenty.studytoby.sqlservice;

import com.pplenty.studytoby.sqlservice.jaxb.SqlType;
import com.pplenty.studytoby.sqlservice.jaxb.Sqlmap;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by yusik on 2020/04/25.
 */
public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    @PostConstruct
    public void loadSql() {
        oxmSqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlMapFile(String sqlMapFile) {
        this.oxmSqlReader.setSqlMapFile(sqlMapFile);
    }


    private static class OxmSqlReader implements SqlReader {

        private static final String DEFAULT_SQLMAP_FILE = "src/main/resources/di/user-sqlmap.xml";

        private String sqlMapFile = DEFAULT_SQLMAP_FILE;
        private Unmarshaller unmarshaller;

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                String contextPath = Sqlmap.class.getPackage().getName();
                JAXBContext context;
                context = JAXBContext.newInstance(contextPath);

                javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new FileInputStream(new File(this.sqlMapFile)));

                List<SqlType> sqlList = sqlmap.getSql();
                for (SqlType sqlType : sqlList) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (JAXBException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlMapFile(String sqlMapFile) {
            this.sqlMapFile = sqlMapFile;
        }
    }
}
