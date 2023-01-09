package sqelevator.updater;

import java.rmi.RemoteException;

public interface Updater {
    void update() throws RemoteException;
}
