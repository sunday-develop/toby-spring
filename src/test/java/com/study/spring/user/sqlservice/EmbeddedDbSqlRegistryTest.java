package com.study.spring.user.sqlservice;

import com.study.spring.user.sqlservice.updatable.EmbeddedDbSqlRegistry;
import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    private EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {

        db = new EmbeddedDatabaseFactory().getDatabase();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }
}
