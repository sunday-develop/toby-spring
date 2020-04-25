package springbook.user.sqlservice;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.Unmarshaller;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class OxmSqlService implements SqlService, InitializingBean {

    private final BaseSqlService baseSqlService;
    private final OxmSqlReader oxmSqlReader;

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public OxmSqlService(Unmarshaller unmarshaller) {
        oxmSqlReader = new OxmSqlReader(unmarshaller);
        baseSqlService = new BaseSqlService(oxmSqlReader, sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return baseSqlService.getSql(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        baseSqlService.afterPropertiesSet();
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        baseSqlService.setSqlRegistry(sqlRegistry);
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

        private OxmSqlReader(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
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
