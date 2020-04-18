package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/18.
 */
public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
