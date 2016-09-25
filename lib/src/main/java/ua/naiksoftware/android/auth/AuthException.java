package ua.naiksoftware.android.auth;

/**
 * Created by naik on 25.09.16.
 */

public class AuthException extends Exception {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(Throwable cause) {
        super(cause);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
