package com.pplenty.studytoby.sqlservice;

import java.util.Map;

/**
 * Created by yusik on 2020/04/25.
 */
public class MyUpdatableSqlRegistry implements UpdatableSqlRegistry {
    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {

    }

    @Override
    public void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException {

    }

    @Override
    public void registerSql(String key, String sql) {

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }
}
