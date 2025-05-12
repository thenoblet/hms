package gtp.hms.exception;

import java.sql.SQLException;

/**
 * Custom exception class for data access layer operations.
 * Wraps SQLException with more specific application-level error messages.
 *
 * <p>Used to handle database-related errors throughout the application's
 * data access layer while providing meaningful error context.
 */
public class DaoException extends SQLException {

    /**
     * Creates a new DaoException with the specified error message.
     *
     * @param message the detail message describing the error
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Creates a new DaoException with the specified error message and cause.
     *
     * @param message the detail message describing the error
     * @param cause the underlying exception that caused this error
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}