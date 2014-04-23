package ca.spacek.gkdd.blacklist;

/**
 * Created by temp on 21/04/14.
 */
public class ReflectionException extends Exception {
    public ReflectionException(Throwable throwable) {
        super(throwable);
    }

    public ReflectionException(String detailMessage) {
        super(detailMessage);
    }

    public ReflectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
