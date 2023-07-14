package server;

import remote.iRemoteClient;

public class User {
    String clientName;
    iRemoteClient remoteClient;
    String serviceType;
    public User(String name, iRemoteClient remoteClient, String serviceType){
        this.clientName = name;
        this.remoteClient = remoteClient;
        this.serviceType = serviceType;
    }
    public String getName(){
        return this.clientName;
    }

    public iRemoteClient getUserInformation(){
        return this.remoteClient;
    }
}
