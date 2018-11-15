package app.wottrich.securitymanagerlibrary.exception;

/**
 * @author lucas.wottrich
 * @since 15/11/2018.
 */
public class CancelButtonException extends RuntimeException {

    public CancelButtonException() {
        super("You need to set cancel button to user to use!");
    }

}
