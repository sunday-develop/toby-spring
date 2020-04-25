package com.pplenty.studytoby.sqlservice;

import java.util.Map;

/**
 * Created by yusik on 2020/04/25.
 */
public interface UpdatableSqlRegistry extends SqlRegistry {

    void updateSql(String key, String sql) throws SqlUpdateFailureException;
    void updateSql(Map<String, String> sqlMap) throws SqlUpdateFailureException;
}
