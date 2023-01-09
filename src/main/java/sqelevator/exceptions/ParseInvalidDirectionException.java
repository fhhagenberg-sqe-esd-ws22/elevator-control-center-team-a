package sqelevator.exceptions;

public class ParseInvalidDirectionException extends RuntimeException {
    public ParseInvalidDirectionException(int val) {
        super(String.format("Encountered direction value '%d'", val));
    }
}
