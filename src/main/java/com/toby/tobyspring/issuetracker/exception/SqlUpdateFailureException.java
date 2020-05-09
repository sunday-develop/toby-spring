package com.toby.tobyspring.issuetracker.exception;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(String msg) {
        super(msg);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }
}
