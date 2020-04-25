package springbook.user.sqlservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    private EmbeddedDatabase database;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/embeddeddb/sqlRegistrySchema.sql")
                .build();

        return new EmbeddedDbSqlRegistry(database);
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void transactionalUpdate() throws Exception {
        checkFind("SQL1", "SQL2", "SQL3");

        final Map<String, String> sqlmap = Map.of(
                "KEY1", "Modified1",
                "KEY99999@#$", "Modified9999"
        );

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) { }

        checkFind("SQL1", "SQL2", "SQL3");
    }

}