package sqelevator.exceptions;

public class NonExistentFloorException extends RuntimeException {

    public NonExistentFloorException(String fmt, Object ... args) {
        super(String.format(fmt, args));
    }
}
