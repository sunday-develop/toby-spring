package com.pplenty.studytoby.sqlservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yusik on 2020/04/19.
 */
public class HashMapSqlRegistry implements SqlRegistry {

    private final Map<String, String> sqlMap = new HashMap<>();

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
}
