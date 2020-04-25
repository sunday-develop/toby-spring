package springbook.user.sqlservice;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Unmarshaller;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class OxmSqlService implements SqlService, InitializingBean {

    private final OxmSqlReader oxmSqlReader;

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public OxmSqlService(Unmarshaller unmarshaller, String sqlmapFile) {
        oxmSqlReader = new OxmSqlReader(unmarshaller, sqlmapFile);
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
    public void afterPropertiesSet() throws Exception {
        oxmSqlReader.read(sqlRegistry);
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    private static class OxmSqlReader implements SqlReader {

        private static final String DFAULT_SQLMAP_FILE = "sqlmap.xml";

        private Unmarshaller unmarshaller;
        private String sqlmapFile = DFAULT_SQLMAP_FILE;

        private OxmSqlReader(Unmarshaller unmarshaller, String sqlmapFile) {
            this.unmarshaller = unmarshaller;
            this.sqlmapFile = sqlmapFile;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sqlmapFile);
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(new StreamSource(inputStream));
                for (SqlType sqlType : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(sqlmapFile + "을 가져올 수 없습니다.", e);
            }
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

    }


}
