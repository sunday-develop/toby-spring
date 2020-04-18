package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlRetrievalFailureException;
import org.springframework.util.StringUtils;

import java.util.Map;

public class UserSqlService implements SqlService {

    private Map<String, String> sqlMap;

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {

        String sql = sqlMap.get(key);
        if (StringUtils.isEmpty(sql)) {
            throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }

        return sql;
    }
}
