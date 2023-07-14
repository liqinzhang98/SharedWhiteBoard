package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface iRemoteClient extends Remote {
    public void messageFromServer(String message) throws RemoteException;
    public void updateUserList(String[] currentUsers) throws RemoteException;
    boolean decide(String str) throws RemoteException;
    public void renewImage(byte[] b) throws RemoteException;
    public void rejectUser(String str)throws RemoteException;
}
