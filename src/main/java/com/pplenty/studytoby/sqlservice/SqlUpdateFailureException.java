package com.pplenty.studytoby.sqlservice;

/**
 * Created by yusik on 2020/04/25.
 */
public class SqlUpdateFailureException extends Exception {

    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }
}
