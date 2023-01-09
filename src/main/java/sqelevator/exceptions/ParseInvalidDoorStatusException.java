package sqelevator.exceptions;

public class ParseInvalidDoorStatusException extends RuntimeException {
    public ParseInvalidDoorStatusException(int val) {
        super(String.format("Encountered door status value '%d'", val));
    }
}
