package com.toby.tobyspring.issuetracker.sqlservice;

import org.junit.jupiter.api.DisplayName;

@DisplayName("ConcurrentHashMap을 이용한 SQL 레지스트리 테스트")
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
