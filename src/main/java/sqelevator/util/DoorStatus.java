package sqelevator.util;

import sqelevator.IElevator;

public enum DoorStatus {
    Closed(IElevator.ELEVATOR_DOORS_CLOSED),
    Open(IElevator.ELEVATOR_DOORS_OPEN),
    Closing(IElevator.ELEVATOR_DOORS_CLOSING),
    Opening(IElevator.ELEVATOR_DOORS_OPENING);

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
        throw new RuntimeException(String.format("Unknown doorstatus %d", val));
    }
}