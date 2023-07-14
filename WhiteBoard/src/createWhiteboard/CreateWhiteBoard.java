package createWhiteboard;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import remote.iRemoteClient;
import remote.iRemoteWhiteboard;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CreateWhiteBoard extends UnicastRemoteObject implements Serializable, iRemoteClient{

    private String ipAddress;
    private String managerName;
    private String portNumber;
    private String serviceName;
    private String serviceType;
    private iRemoteWhiteboard reachWhiteboardServer;
    private String[] userData = new String[3];

    protected whiteBoardDemo gui;

    public CreateWhiteBoard(String ipAddress, String portNumber, String managerName) throws RemoteException {
        this.ipAddress = ipAddress + ":"+ portNumber;
        this.managerName = managerName;
        this.portNumber = portNumber;
        this.serviceName = "whiteboard";
        this.serviceType = "create";
    }

    public void connectServer() {
        try{
            Naming.rebind("rmi://" + ipAddress + "/" + serviceType, this);
            this.reachWhiteboardServer = (iRemoteWhiteboard) Naming.lookup("rmi://"
                    + ipAddress + "/"
                    + serviceName);
            this.userData[0] = managerName;
            this.userData[1] = ipAddress;
            this.userData[2] = serviceType;
            reachWhiteboardServer.checkAvailability(userData);
            this.gui = new whiteBoardDemo(this.managerName, this.serviceType);
            reachWhiteboardServer.registerListener(userData);
            gui.getMainFrame().setManagerName(reachWhiteboardServer.getManagerName());
            gui.getMainFrame().setWhiteboardServer(reachWhiteboardServer);
            gui.getMainFrame().setUsername(managerName);
            gui.getMainFrame().getDrawPanel().setWhiteboard(reachWhiteboardServer);
            reachWhiteboardServer.updateUserList();

        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Server not found\n",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try{
            CreateWhiteBoard managercreate = new CreateWhiteBoard("localhost", "6000", "bob");
            //CreateWhiteBoard managercreate = new CreateWhiteBoard(args[0], args[1], args[2]);
            managercreate.connectServer();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void messageFromServer(String message) throws RemoteException {
        gui.getMainFrame().getTextArea().append(message);
    }

    @Override
    public void updateUserList(String[] currentUsers) throws RemoteException {
        gui.getMainFrame().getUserList().setListData(currentUsers);
    }

    @Override
    public boolean decide(String name) throws RemoteException {
        int decision = JOptionPane.showConfirmDialog(null,name +
                " would like to enter the party\n" + "approve?","someone wants to share your whiteboard",
                JOptionPane.YES_NO_OPTION);
        return decision == 0;
    }

    @Override
    public void renewImage(byte[] b) throws RemoteException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            BufferedImage image = ImageIO.read(in);
            gui.getMainFrame().getDrawPanel().load(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectUser(String str) throws RemoteException {
        JOptionPane.showMessageDialog(null, str, "error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
