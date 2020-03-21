package com.toby.tobyspring.user.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
