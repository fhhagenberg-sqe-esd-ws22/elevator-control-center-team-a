package sqelevator.util;

import sqelevator.IElevator;

public enum DoorStatus {
    Closed(IElevator.ELEVATOR_DOORS_CLOSED),
    Open(IElevator.ELEVATOR_DOORS_OPEN),
    Closing(IElevator.ELEVATOR_DOORS_CLOSING),
    Opening(IElevator.ELEVATOR_DOORS_OPENING);

    public final int val;

    private DoorStatus(int val) {
        this.val = val;
    }
}