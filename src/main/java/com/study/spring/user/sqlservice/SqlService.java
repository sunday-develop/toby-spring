package com.study.spring.user.sqlservice;

import com.study.spring.user.exception.SqlRetrievalFailureException;

public interface SqlService {

    String getSql(String key) throws SqlRetrievalFailureException;
}
