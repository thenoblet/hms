package gtp.hms.exception;

import java.sql.SQLException;

public class DaoException extends SQLException {

    public DaoException(String message) {
        super(message);
    }


    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
