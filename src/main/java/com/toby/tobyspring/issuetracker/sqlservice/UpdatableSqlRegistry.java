package com.toby.tobyspring.issuetracker.sqlservice;

import com.toby.tobyspring.issuetracker.exception.SqlUpdateFailureException;
import com.toby.tobyspring.user.sqlservice.SqlRegistry;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {
    public void updateSql(String key, String sql) throws SqlUpdateFailureException;
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
