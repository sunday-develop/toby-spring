package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlNotFoundException;
import com.study.spring.user.exception.SqlRetrievalFailureException;

import javax.annotation.PostConstruct;

public class BaseSqlService implements SqlService {

    protected SqlReader sqlReader;

    protected SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @PostConstruct
    public void loadSql() {
        sqlReader.read(sqlRegistry);
    }
}
