package com.toby.tobyspring.user.sqlservice;

public interface SqlRegistry {
    void registerSql(String key, String sql);
    String findSql(String key) throws SqlNotFoundException;
}
