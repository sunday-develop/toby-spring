package springbook.user.sqlservice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

    private static final String REGISTER_SQL = "insert into sqlmap(key_, sql_) values(:key, :sql)";
    private static final String FIND_SQL = "select sql_ from sqlmap where key_ = :key";

    private final NamedParameterJdbcTemplate jdbc;
    private final TransactionTemplate transactionTemplate;

    public EmbeddedDbSqlRegistry(DataSource dataSource) {
        jdbc = new NamedParameterJdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void registerSql(String key, String sql) {
        final Map<String, String> params = Map.of("key", key, "sql", sql);
        jdbc.update(REGISTER_SQL, params);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        final Map<String, String> params = Map.of("key", key);

        try {
            return jdbc.queryForObject(FIND_SQL, params, String.class);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL 을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        final Map<String, String> params = Map.of("key", key, "sql", sql);
        final int affected = jdbc.update("update sqlmap set sql_ = :sql where key_ = :key", params);
        if (affected == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL 을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.executeWithoutResult(transactionStatus -> sqlmap.forEach(this::updateSql));
    }

}
