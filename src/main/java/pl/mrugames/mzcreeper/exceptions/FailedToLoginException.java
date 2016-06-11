package pl.mrugames.mzcreeper.exceptions;

public class FailedToLoginException extends RuntimeException {
    public FailedToLoginException(Throwable cause) {
        super(cause);
    }
}
