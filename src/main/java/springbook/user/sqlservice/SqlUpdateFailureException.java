package springbook.user.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {

    public SqlUpdateFailureException() {
    }

    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlUpdateFailureException(Throwable cause) {
        super(cause);
    }

    public SqlUpdateFailureException(String message,
                                     Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
