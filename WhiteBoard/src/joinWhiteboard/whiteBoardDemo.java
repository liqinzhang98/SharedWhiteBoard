package joinWhiteboard;

import javax.swing.*;
import java.rmi.RemoteException;

public class whiteBoardDemo{
    MyFrame frame;
    String title;
    String userName;
    String serviceType;
    public whiteBoardDemo(String userName, String serviceType)  throws RemoteException {
        this.title = "[" + serviceType + "] " + userName + "'s Whiteboard";
        this.userName = userName;
        this.serviceType = serviceType;
        frame = new MyFrame(this.title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300,600);
        frame.setVisible(true);
    }
    public MyFrame getMainFrame(){
        return frame;
    }
}
