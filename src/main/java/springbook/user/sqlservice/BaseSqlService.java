package springbook.user.sqlservice;

import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

public class BaseSqlService implements SqlService, InitializingBean {

    private final Map<String, String> sqlMap = new HashMap<>();
    private final SqlReader sqlReader;
    private final SqlRegistry sqlRegistry;

    public BaseSqlService(SqlReader sqlReader, SqlRegistry sqlRegistry) {
        this.sqlReader = sqlReader;
        this.sqlRegistry = sqlRegistry;
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
        sqlReader.read(sqlRegistry);
    }

}
