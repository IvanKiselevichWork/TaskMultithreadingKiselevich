package by.kiselevich.taskmultithreading.exception;

public class IndexOutOfMaxtrixBoundsException extends Exception {

    public IndexOutOfMaxtrixBoundsException() {
    }

    public IndexOutOfMaxtrixBoundsException(String message) {
        super(message);
    }

    public IndexOutOfMaxtrixBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexOutOfMaxtrixBoundsException(Throwable cause) {
        super(cause);
    }

    public IndexOutOfMaxtrixBoundsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
