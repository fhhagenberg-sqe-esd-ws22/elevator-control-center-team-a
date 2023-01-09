package sqelevator.util;

import sqelevator.IElevator;
import sqelevator.exceptions.ParseInvalidDoorStatusException;

public enum DoorStatus {
    CLOSED(IElevator.ELEVATOR_DOORS_CLOSED),
    OPEN(IElevator.ELEVATOR_DOORS_OPEN),
    CLOSING(IElevator.ELEVATOR_DOORS_CLOSING),
    OPENING(IElevator.ELEVATOR_DOORS_OPENING);

    public final int val;

    DoorStatus(int val) {
        this.val = val;
    }

    public static DoorStatus valueOf(int val) {
        for(DoorStatus s : DoorStatus.values()) {
            if (s.val == val) {
                return s;
            }
        }
        throw new ParseInvalidDoorStatusException(val);
    }
}