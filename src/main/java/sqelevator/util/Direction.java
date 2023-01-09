package sqelevator.util;

import sqelevator.IElevator;
import sqelevator.exceptions.ParseInvalidDirectionException;

public enum Direction {
    Up(IElevator.ELEVATOR_DIRECTION_UP),
    Down(IElevator.ELEVATOR_DIRECTION_DOWN),
    Uncommitted(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);

    public final int val;

    Direction(int val) {
        this.val = val;
    }

    public static Direction valueOf(int val) {
        for(Direction d : Direction.values()) {
            if (d.val == val) {
                return d;
            }
        }
        throw new ParseInvalidDirectionException(val);
    }
}
