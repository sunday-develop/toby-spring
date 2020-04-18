package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/18.
 */
public class SqlNotFoundException extends RuntimeException {

    public SqlNotFoundException(String message) {
        super(message);
    }

    public SqlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
