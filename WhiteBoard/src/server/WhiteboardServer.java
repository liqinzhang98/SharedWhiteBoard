package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

import remote.iRemoteClient;
import remote.iRemoteWhiteboard;

public class WhiteboardServer extends UnicastRemoteObject implements iRemoteWhiteboard {
    protected WhiteboardServer() throws RemoteException {
    }
    private byte[] imageData;
    private ArrayList<User> users = new ArrayList<User>();
    private String managerName = "";

    @Override
    public void updateAll(byte[] iData) throws RemoteException {
        this.imageData = iData;
        // for each user, update their canvas
        for(User u : users){
            try {
                u.getUserInformation().renewImage(iData);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String getManagerName() {
        return managerName;
    }
    @Override
    public synchronized void registerListener(String[] details) throws RemoteException {
        try {
            // look the registry
            iRemoteClient nextClient = (iRemoteClient) Naming.lookup("rmi://" + details[1] + "/" + details[2]);
            // add the user
            users.add(new User(details[0], nextClient, details[2]));
            // define the manager
            if (users.size() == 1) {
                managerName = details[0];
            }
            // let the manager check of let the new user in
            if(users.size() > 1) {
                boolean yesOrNo = managerDecide(details[0]);
                if(!yesOrNo) {
                    users.remove(users.size() - 1); // remove before send the message to the client
                    nextClient.rejectUser("Manager refused to let you in\n");
                }
            }
            // pass the canvas image to the new user
            if(imageData != null) {
                nextClient.renewImage(imageData);
            }
            // broadcast the news
            nextClient.messageFromServer("[Server] : " + details[0] + ", you are joined the room.\n");
            broadcast("[Server] : " + details[0] + " has joined the room.\n");
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean managerDecide(String str) throws RemoteException {
        // let the manager at the index 0 of the array to decide
        return users.get(0).getUserInformation().decide(str);
    }

    @Override
    public synchronized void removeUser(String username) throws RemoteException {
        int userLeaving = 0;
        for(int i = 0;i < users.size();i++) {
            if(users.get(i).getName().equals(username)) {
                userLeaving = i;
            }
        }
        // manager leaving
        if(userLeaving == 0) {
            users.remove(userLeaving);
            if (users.size() > 0) {
                // tell everyone the manager leaves the party, close the room
                for (User u: users) {
                    removingThreads(u, "Manager left the party, room is closing...");
                    // clear everything after leaving
                    users = new ArrayList<User>();
                    imageData = null;
                }
            }
            // others leaving
        } else{
            iRemoteClient exile = users.get(userLeaving).getUserInformation();
            removingThreads(users.get(userLeaving), "you are out");
            users.remove(userLeaving);
            broadcast("[Server] :" + username + " left the room\n");
        }
    }

    public void removingThreads(User u, String msg){
        Thread removingThread = new Thread(()->{
            try {
                u.getUserInformation().rejectUser(msg);
            } catch (RemoteException ignored) {
            }
        });
        removingThread.start();
    }

    @Override
    public synchronized void broadcast(String msg) throws RemoteException {
        for(User u : users){
            try {
                u.getUserInformation().messageFromServer(msg + "\n");
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateUserList() throws RemoteException {
        String[] userArray = new String[users.size()];
        // convert to array
        for (int i = 0; i < users.size();i++) {
            userArray[i] = users.get(i).getName();
        }
        // passing the new array and update the userList
        for(User u : users) {
            u.getUserInformation().updateUserList(userArray);
        }
    }

    @Override
    public void checkAvailability(String[] details) throws RemoteException, MalformedURLException, NotBoundException {
        if(users.size() > 0) {
            try {
                iRemoteClient nextClient = (iRemoteClient)Naming.lookup("rmi://" + details[1] + "/" + details[2]);
                    // avoid second manager join in
                if (Objects.equals(details[2], "create")) {
                    nextClient.rejectUser("Reached the maximum number of manager.\n");
                }
            } catch (MalformedURLException | NotBoundException e) {
                e.printStackTrace();
            }
        }

        if (users.size()==0 && Objects.equals(details[2], "join")){
            iRemoteClient nextClient = (iRemoteClient)Naming.lookup("rmi://" + details[1] + "/" + details[2]);
                // discard the request from non-manager user to join before a manager user
            nextClient.rejectUser("Room have not been created yet.\n");
        }

        //check identical names
        if (!checkIdentical(details[0]) && users.size()>0 ) {
            iRemoteClient nextClient = (iRemoteClient) Naming.lookup("rmi://" + details[1] + "/" + details[2]);
            nextClient.rejectUser("Get a new Name\n");
        }

    }

    @Override
    public boolean checkIdentical(String newUserName) throws RemoteException {
        for(User u:users){
            if (Objects.equals(newUserName, u.getName())){
                return false;
            }
        }
        return true;
    }
}
