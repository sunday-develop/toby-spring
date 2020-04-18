package springbook.user.sqlservice;

import lombok.Setter;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry {

    private final Map<String, String> sqlMap = new HashMap<>();
    private final String sqlmapFile;

    @Setter
    private SqlReader sqlReader;

    @Setter
    private SqlRegistry sqlRegistry;

    public XmlSqlService setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
        return this;
    }

    public XmlSqlService(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @PostConstruct
    public void loadSql() {
        final String contextPath = Sqlmap.class.getPackageName();
        try {
            final JAXBContext context = JAXBContext.newInstance(contextPath);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final InputStream is = getClass().getClassLoader().getResourceAsStream(sqlmapFile);
            final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        final String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        final String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

}
