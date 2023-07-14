package remote;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface iRemoteWhiteboard extends Remote {
    public void updateAll(byte[] b) throws RemoteException;
    String getManagerName() throws RemoteException;
    public void registerListener(String[] details)throws RemoteException;
    boolean managerDecide(String str) throws RemoteException;
    public void removeUser(String username) throws RemoteException;
    public void broadcast(String msg) throws RemoteException;
    void updateUserList() throws RemoteException;
    void checkAvailability(String[] details) throws RemoteException, MalformedURLException, NotBoundException;
    boolean checkIdentical(String newUserName) throws RemoteException;
}
