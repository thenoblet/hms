package gtp.hms.exception;

/**
 * Custom exception class for handling service layer exceptions in the HMS application.
 * This exception is thrown when operations in the service layer fail.
 */
public class ServiceException extends Exception {

    /**
     * Constructs a new ServiceException with {@code null} as its detail message.
     * The cause is not initialized.
     */
    public ServiceException() {
        super();
    }

    /**
     * Constructs a new ServiceException with the specified detail message.
     * The cause is not initialized.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a new ServiceException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). A {@code null} value is permitted,
     *                and indicates that the cause is nonexistent or unknown.
     */
    public ServiceException(String message, Exception cause) {
        super(message, cause);
    }
}