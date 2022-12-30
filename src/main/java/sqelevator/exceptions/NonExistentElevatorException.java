package sqelevator.exceptions;

public class NonExistentElevatorException extends RuntimeException {

    public NonExistentElevatorException(String fmt, Object ... args) {
        super(String.format(fmt, args));
    }
}
