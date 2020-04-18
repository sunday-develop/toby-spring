package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/18.
 */
public class SqlRetrievalFailureException extends RuntimeException {

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(Throwable cause) {
        super(cause);
    }
}
