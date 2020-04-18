package com.study.spring.user.exception;

public class SqlRetrievalFailureException extends RuntimeException {

    public SqlRetrievalFailureException(Throwable cause) {
        super(cause);
    }

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
