package app.wottrich.securitymanagerlibrary.exception;

/**
 * @author lucas.wottrich
 * @version 1.0
 * @since 10/11/2018.
 */
public class EqualKeyException extends RuntimeException {

	public EqualKeyException(String message) {
		super (message);
	}

	public EqualKeyException(String message, Throwable cause) {
		super (message, cause);
	}
}
