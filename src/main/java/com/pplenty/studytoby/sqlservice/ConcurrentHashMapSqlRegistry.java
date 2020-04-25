package com.pplenty.studytoby.sqlservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yusik on 2020/04/26.
 */
public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        return sql;
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (sqlMap.get(key) == null) {
            throw new SqlUpdateFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlMap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }
}
