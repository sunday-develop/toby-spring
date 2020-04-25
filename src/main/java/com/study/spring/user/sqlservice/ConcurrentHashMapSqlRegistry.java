package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlNotFoundException;
import com.study.spring.user.exception.SqlUpdateFailureException;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private Map<String, String> sqlMap = new ConcurrentHashMap<>();

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if (StringUtils.isEmpty(sqlMap.get(key))) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }

        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {

        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (StringUtils.isEmpty(sql)) {
            throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
