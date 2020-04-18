package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/19.
 */
public interface SqlRegistry {
    void registerSql(String key, String sql);
    String findSql(String key) throws SqlRetrievalFailureException;
}
