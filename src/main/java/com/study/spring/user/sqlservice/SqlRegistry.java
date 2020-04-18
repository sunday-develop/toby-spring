package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlNotFoundException;

public interface SqlRegistry {

    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;
}
