package springbook.user.sqlservice;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService, InitializingBean {

    private final BaseSqlService baseSqlService;
    private final OxmSqlReader oxmSqlReader;

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public OxmSqlService(Unmarshaller unmarshaller) {
        oxmSqlReader = new OxmSqlReader(unmarshaller);
        baseSqlService = new BaseSqlService(oxmSqlReader, sqlRegistry);
    }

    public OxmSqlService(Unmarshaller unmarshaller, SqlRegistry sqlRegistry) {
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

    public void setSqlmap(Resource sqlmap) {
        oxmSqlReader.setSqlmap(sqlmap);
    }

    private static class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private Resource sqlmap = new ClassPathResource("sqlmap.xml", getClass().getClassLoader());

        private OxmSqlReader(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                final Source source = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(source);
                for (SqlType sqlType : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sqlType.getKey(), sqlType.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
            }
        }

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

    }


}
