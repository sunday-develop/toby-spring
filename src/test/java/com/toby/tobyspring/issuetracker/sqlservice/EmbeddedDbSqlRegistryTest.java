package com.toby.tobyspring.issuetracker.sqlservice;

import com.toby.tobyspring.issuetracker.exception.SqlUpdateFailureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("EmbeddedDatabase를 이용한 SQL 레지스트리 테스트")
public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    EmbeddedDatabase db;

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:/sql/schema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @Test
    @DisplayName("다중 SQL 수정에 대한 트랜잭션 테스트")
    public void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "MODIFY1");
        sqlmap.put("KEY1234123", "modify99");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail("updateSql must be fail by SqlUpdateFailureException");
        } catch (SqlUpdateFailureException e) {
        }

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
