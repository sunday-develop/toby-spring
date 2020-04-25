package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlUpdateFailureException;
import com.study.spring.user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    private EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {

        db = new EmbeddedDatabaseFactory().getDatabase();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @Test
    void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY999", "Modified999");

        try {
            getSqlRegistry().updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {
            checkFindResult("SQL1", "SQL2", "SQL3");
        }
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }
}
