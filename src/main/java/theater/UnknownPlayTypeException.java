package theater;

/** Thrown when a play type is not supported. */
public class UnknownPlayTypeException extends RuntimeException {
    public UnknownPlayTypeException(String type) {
        super("unknown type: " + type);
    }
}
