package joinWhiteboard;

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

public class JoinWhiteBoard extends UnicastRemoteObject implements Serializable, iRemoteClient{
    private String ipAddress;
    private String userName;
    private String portNumber;
    private String serviceName;
    private String serviceType;
    private iRemoteWhiteboard reachWhiteboardServer;
    private String[] userData = new String[3];
    protected whiteBoardDemo gui;


    public JoinWhiteBoard(String ipAddress, String portNumber, String userName) throws RemoteException {
        this.ipAddress = ipAddress + ":"+ portNumber;
        this.userName = userName;
        this.portNumber = portNumber;
        this.serviceName = "whiteboard";
        this.serviceType = "join";
    }

    public void connectServer() {
        try{
            Naming.rebind("rmi://" + this.ipAddress + "/" + this.serviceType, this);
            this.reachWhiteboardServer = (iRemoteWhiteboard) Naming.lookup("rmi://"
                    + ipAddress + "/" + serviceName);
            this.userData[0] = userName;
            this.userData[1] = ipAddress;
            this.userData[2] = serviceType;
            reachWhiteboardServer.checkAvailability(userData);
            this.gui = new whiteBoardDemo(this.userName, this.serviceType);
            reachWhiteboardServer.registerListener(userData);
            gui.getMainFrame().getKickingBtn().setVisible(false);//update the visibility after being registered
            gui.getMainFrame().menubar.setVisible(false);
            gui.getMainFrame().setManagerName(reachWhiteboardServer.getManagerName());
            gui.getMainFrame().setWhiteboardServer(reachWhiteboardServer);
            gui.getMainFrame().setUsername(userName);
            gui.getMainFrame().getDrawPanel().setWhiteboard(reachWhiteboardServer);
            reachWhiteboardServer.updateUserList();

        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Server not found\ndrawing cannot be shared",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try{
            JoinWhiteBoard othercreate = new JoinWhiteBoard("localhost", "6000", "coc");
            //JoinWhiteBoard othercreate = new JoinWhiteBoard(args[0], args[1], args[2]);
            othercreate.connectServer();
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
    public boolean decide(String str) throws RemoteException {
        // Other users not going to let the new user in or not
        return false;
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
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
