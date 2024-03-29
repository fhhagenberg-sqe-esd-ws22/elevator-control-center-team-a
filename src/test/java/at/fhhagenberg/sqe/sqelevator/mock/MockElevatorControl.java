package at.fhhagenberg.sqe.sqelevator.mock;

import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class MockElevatorControl implements IElevator {

    private final Map<Integer, Integer> elevatorTargetMap = new HashMap<>();
    private final Map<Integer, boolean[]> serviceFloorMap = new HashMap<>();
    private final Map<Integer, boolean[]> elevatorStopRequest = new HashMap<>();
    private final int floorCount;
    private final int elevatorCount;
    private final boolean[] upRequested;
    private final boolean[] downRequested;

    private final boolean[] defaultRequested;
    private final boolean[] defaultServiceFloor;

    private long clockTick = 0;

    public MockElevatorControl(int elevatorCount, int floorCount) {
        if (elevatorCount <= 0) throw new IllegalArgumentException("Elevatorcount must be greater than 0.");
        if (floorCount <= 0) throw new IllegalArgumentException("Floorcount must be greater than 0.");
        this.elevatorCount = elevatorCount;
        this.floorCount = floorCount;

        upRequested = new boolean[floorCount];
        downRequested = new boolean[floorCount];

        defaultServiceFloor = new boolean[floorCount];
        defaultRequested = new boolean[floorCount];
        for(int ii = 0; ii < floorCount; ii++) {
            defaultServiceFloor[ii] = true;
            defaultRequested[ii] = false;
        }

        reset();
    }

    public void reset() {
        for(int ii = 0; ii < defaultRequested.length; ii++) {
            upRequested[ii] = defaultRequested[ii];
            downRequested[ii] = defaultRequested[ii];
        }

        for(int ii = 0; ii < elevatorCount; ii++) {
            elevatorTargetMap.put(ii, 0);
            serviceFloorMap.put(ii, defaultServiceFloor.clone());
            elevatorStopRequest.put(ii, defaultRequested.clone());
        }
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        return IElevator.ELEVATOR_DIRECTION_UNCOMMITTED;
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        return 0;
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        return elevatorStopRequest.get(elevatorNumber)[floor];
    }

    public void setElevatorButton(int elevatorNumber, int floor) {
        boolean[] stops = elevatorStopRequest.get(elevatorNumber);
        stops[floor] = true;

        elevatorStopRequest.put(elevatorNumber, stops);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        return IElevator.ELEVATOR_DOORS_CLOSED;
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        return elevatorTargetMap.get(elevatorNumber);
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return elevatorCount;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        return getFloorHeight() * elevatorTargetMap.get(elevatorNumber);
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        return 0;
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        return elevatorNumber * 5;
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        return elevatorNumber * 2;
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException {
        return downRequested[floor];
    }

    public void setFloorButtonDown(int floor) {
        downRequested[floor] = true;
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException {
        return upRequested[floor];
    }

    public void setFloorButtonUp(int floor) {
        upRequested[floor] = true;
    }

    @Override
    public int getFloorHeight() throws RemoteException {
        return 7; // The perfect floor height.
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return floorCount;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        return serviceFloorMap.get(elevatorNumber)[floor];
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        return elevatorTargetMap.get(elevatorNumber);
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        // Intentionally left blank
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        boolean[] val = serviceFloorMap.get(elevatorNumber);
        val[floor] = service;
        serviceFloorMap.put(elevatorNumber, val);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        elevatorTargetMap.put(elevatorNumber, target);
    }

    @Override
    public long getClockTick() throws RemoteException {
        return clockTick++;
    }
}
