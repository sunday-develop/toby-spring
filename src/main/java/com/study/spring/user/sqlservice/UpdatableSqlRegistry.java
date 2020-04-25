package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlUpdateFailureException;

import java.util.Map;

public interface UpdatableSqlRegistry extends SqlRegistry {

    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateql(Map<String, String> sqlmap) throws SqlUpdateFailureException;

}
