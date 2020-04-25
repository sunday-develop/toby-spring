package springbook.user.sqlservice;

import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

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

}