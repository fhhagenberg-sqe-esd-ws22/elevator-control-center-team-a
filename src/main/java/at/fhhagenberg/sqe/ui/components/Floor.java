package at.fhhagenberg.sqe.ui.components;

import sqelevator.IElevator;

import java.rmi.RemoteException;

class Floor {
    private int floorId;
    private final IElevator elevatorControl;
    private final ElevatorListView elevatorListView;

    public boolean isUnderService() throws RemoteException {
        return elevatorControl.getServicesFloors((int) elevatorListView.getSelectedElevator().elevatorId, floorId);
    }
    public void setUnderService() throws RemoteException { elevatorControl.setServicesFloors((int) elevatorListView.getSelectedElevator().elevatorId, floorId, true);}
    public void unsetUnderService() throws RemoteException { elevatorControl.setServicesFloors((int) elevatorListView.getSelectedElevator().elevatorId, floorId, false);}

    public Floor(ElevatorListView listview, IElevator control, int id)
    {
        elevatorControl = control;
        elevatorListView = listview;
        floorId = id;
    }
    @Override
    public String toString()
    {
        return ("Floor" + floorId);
    }
}