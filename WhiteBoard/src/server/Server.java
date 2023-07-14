package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import remote.iRemoteWhiteboard;

public class Server {
    public static void main(String[] args) {
        try {
            iRemoteWhiteboard whiteboard = new WhiteboardServer();
            //int port = Integer.parseInt(args[0]);
            int port = 6000;
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("whiteboard", whiteboard);
            System.out.println("[Server]: start running...\n[server]: Port " + port + " is initiated.");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
