package com.toby.tobyspring.issuetracker.exception;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }
}
