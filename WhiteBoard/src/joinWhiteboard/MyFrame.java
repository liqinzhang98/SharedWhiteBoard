package joinWhiteboard;

import remote.iRemoteWhiteboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class MyFrame extends JFrame implements WindowListener, ListSelectionListener{
    private JTextField textField;
    private Draw drawArea;
    private JList list;
    private JTextArea textArea;
    private String userName;
    private String managerName;
    private String kickingUser;
    private String[] users = new String[1];
    private iRemoteWhiteboard whiteboardServer;
    private JButton btnKick = new JButton("Kick");
    public JMenuBar menubar;
    private JMenuItem itemOpen;

    private JMenuItem itemClose;
    private JMenuItem itemQuit;
    private JMenuItem itemNew;
    private FileDialog openDia;
    public MyFrame(String title) {
        super(title);
        // full view
        Container content = this.getContentPane();
        content.setLayout(new BorderLayout());
        // draw pane
        drawArea = new Draw();
        content.add(drawArea, BorderLayout.CENTER);
        //tool menu color
        JPanel tmc = new JPanel();
        tmc.setLayout(new BorderLayout());
        // tool control panel
        JPanel controls = new JPanel();

        JButton btnRectangle = new JButton("Rectangle");
        btnRectangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("rectangle");
            }
        });

        JButton btnTriangle = new JButton("Triangle");
        btnTriangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("triangle");
            }
        });

        JButton btnPencil = new JButton("Pencil");
        btnPencil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("pencil");
            }
        });

        JButton btnLine = new JButton("Line");
        btnLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("line");
            }
        });

        JButton btnCircle = new JButton("Circle");
        btnCircle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("circle");
            }
        });

        JButton btnText = new JButton("Text");
        btnText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.setToolType("text");
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.clear();
            }
        });

        controls.add(btnRectangle);
        controls.add(btnTriangle);
        controls.add(btnPencil);
        controls.add(btnLine);
        controls.add(btnCircle);
        controls.add(btnText);
        controls.add(btnClear);

        tmc.add(controls, BorderLayout.CENTER);
        //content.add(controls, BorderLayout.NORTH);

        // menu bar
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu mnuFile = new JMenu("File");
        itemNew = new JMenuItem("New");
        itemOpen = new JMenuItem("Open");
        itemClose = new JMenuItem("Close");
        itemQuit = new JMenuItem("Quit");

        menubar.add(mnuFile);
        mnuFile.add(itemNew);
        mnuFile.add(itemOpen);
        mnuFile.add(itemClose);
        mnuFile.add(itemQuit);

        openDia = new FileDialog(this, "open", FileDialog.LOAD);

        if (!Objects.equals(userName, managerName)) {
            menubar.setVisible(false);
        }
        myEvent();




        // initiate color bar
        JPanel colors = new JPanel();

        JButton btnBlue = new JButton("");
        btnBlue.setBounds(300, 40, 90, 20);
        btnBlue.setBackground(Color.BLUE);
        btnBlue.setOpaque(true);
        btnBlue.setBorderPainted(false);
        btnBlue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.blue();
            }
        });
        colors.add(btnBlue);

        JButton btnGreen = new JButton("");
        btnGreen.setBounds(300, 40, 90, 20);
        btnGreen.setBackground(Color.GREEN);
        btnGreen.setOpaque(true);
        btnGreen.setBorderPainted(false);
        btnGreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.green();
            }
        });
        colors.add(btnGreen);

        JButton btnPurple = new JButton("");
        btnPurple.setBounds(300, 40, 90, 20);
        btnPurple.setBackground(Color.MAGENTA);
        btnPurple.setOpaque(true);
        btnPurple.setBorderPainted(false);
        btnPurple.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.magenta();
            }
        });
        colors.add(btnPurple);

        JButton btnYellow = new JButton("");
        btnYellow.setBounds(300, 40, 90, 20);
        btnYellow.setBackground(Color.YELLOW);
        btnYellow.setOpaque(true);
        btnYellow.setBorderPainted(false);
        btnYellow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.yellow();
            }
        });
        colors.add(btnYellow);

        JButton btnOrange = new JButton("");
        btnOrange.setBounds(300, 40, 90, 20);
        btnOrange.setBackground(Color.ORANGE);
        btnOrange.setOpaque(true);
        btnOrange.setBorderPainted(false);
        btnOrange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.orange();
            }
        });
        colors.add(btnOrange);

        JButton btnRed = new JButton("");
        btnRed.setBounds(300, 40, 90, 20);
        btnRed.setBackground(Color.RED);
        btnRed.setOpaque(true);
        btnRed.setBorderPainted(false);
        btnRed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.red();
            }
        });
        colors.add(btnRed);

        JButton btnBlack = new JButton("");
        btnBlack.setBounds(300, 40, 90, 20);
        btnBlack.setBackground(Color.BLACK);
        btnBlack.setOpaque(true);
        btnBlack.setBorderPainted(false);
        btnBlack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.black();
            }
        });
        colors.add(btnBlack);

        JButton btnCYAN = new JButton("");
        btnCYAN.setBounds(300, 40, 90, 20);
        btnCYAN.setBackground(Color.CYAN);
        btnCYAN.setOpaque(true);
        btnCYAN.setBorderPainted(false);
        btnCYAN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.cyan();
            }
        });
        colors.add(btnCYAN);

        JButton btnGray = new JButton("");
        btnGray.setBounds(300, 40, 90, 20);
        btnGray.setBackground(Color.GRAY);
        btnGray.setOpaque(true);
        btnGray.setBorderPainted(false);
        btnGray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.gray();
            }
        });
        colors.add(btnGray);

        JButton btnPink = new JButton("");
        btnPink.setBounds(300, 40, 90, 20);
        btnPink.setBackground(Color.PINK);
        btnPink.setOpaque(true);
        btnPink.setBorderPainted(false);
        btnPink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.pink();
            }
        });
        colors.add(btnPink);

        JButton btnLGray = new JButton("");
        btnLGray.setBounds(300, 40, 90, 20);
        btnLGray.setBackground(Color.LIGHT_GRAY);
        btnLGray.setOpaque(true);
        btnLGray.setBorderPainted(false);
        btnLGray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.lGray();
            }
        });
        colors.add(btnLGray);

        JButton btnLBlue = new JButton("");
        btnLBlue.setBounds(300, 40, 90, 20);
        btnLBlue.setBackground(new Color(0, 102, 102));
        btnLBlue.setOpaque(true);
        btnLBlue.setBorderPainted(false);
        btnLBlue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.lBlue();
            }
        });
        colors.add(btnLBlue);

        JButton btnDGreen = new JButton("");
        btnDGreen.setBounds(300, 40, 90, 20);
        btnDGreen.setBackground(new Color(51, 102, 0));
        btnDGreen.setOpaque(true);
        btnDGreen.setBorderPainted(false);
        btnDGreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.dGreen();
            }
        });
        colors.add(btnDGreen);

        JButton btnLDPurple = new JButton("");
        btnLDPurple.setBounds(300, 40, 90, 20);
        btnLDPurple.setBackground(new Color(102, 0, 102));
        btnLDPurple.setOpaque(true);
        btnLDPurple.setBorderPainted(false);
        btnLDPurple.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.dPurple();
            }
        });
        colors.add(btnLDPurple);

        JButton btnWhite = new JButton("");
        btnWhite.setBounds(300, 40, 90, 20);
        btnWhite.setBackground(Color.white);
        btnWhite.setOpaque(true);
        btnWhite.setBorderPainted(false);
        btnWhite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.white();
            }
        });
        colors.add(btnWhite);

        JButton btnOlive = new JButton("");
        btnOlive.setBounds(300, 40, 90, 20);
        btnOlive.setBackground(new Color(102, 102, 0));
        btnOlive.setOpaque(true);
        btnOlive.setBorderPainted(false);
        btnOlive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawArea.olive();
            }
        });
        colors.add(btnOlive);

        tmc.add(colors, BorderLayout.SOUTH);
        //content.add(colors, BorderLayout.SOUTH);
        content.add(tmc, BorderLayout.NORTH);

        // initiate a new sub-content section for chatRoom function
        JPanel chatBox = new JPanel();
        chatBox.setLayout(new BorderLayout());
        // chatLabel
        JLabel chatLabel = new JLabel("ChatBox");
        chatLabel.setFont(new Font("Monaco", Font.PLAIN, 14));
        chatLabel.setBounds(600, 150, 60, 30);


        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setBounds(611, 187, 183, 207);
        JScrollPane chatRoom = new JScrollPane(textArea);
        chatRoom.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatRoom.setBounds(600, 180, 200, 200);


        //Type and send
        JPanel typeSend = new JPanel();
        textField = new JTextField();
        textField.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));
        textField.setBounds(630, 400, 130, 30);
        textField.setColumns(10);
        typeSend.add(textField);

        JButton btnSend = new JButton("Send");
        btnSend.setBounds(780, 400, 80, 30);
        btnSend.addMouseListener(new MouseAdapter() {
                                     @Override
                                     public void mouseClicked(MouseEvent e1) {
                                         String chatMsg = textField.getText();
                                         try {
                                             if (!Objects.equals(chatMsg, "")) {
                                                 whiteboardServer.broadcast(userName + ": " + chatMsg);
                                                 textField.setText(null);
                                             }
                                         } catch (RemoteException e2) {
                                             JOptionPane.showMessageDialog(null, "No manager", "Error!", JOptionPane.ERROR_MESSAGE);
                                             e2.printStackTrace();
                                             System.exit(0);
                                         }
                                     }
        });
        typeSend.add(btnSend);
        // combine components to the chat section
        chatBox.add(chatLabel, BorderLayout.NORTH);
        chatBox.add(chatRoom, BorderLayout.CENTER);
        chatBox.add(typeSend, BorderLayout.SOUTH);
        // add chat section to the main panel
        content.add(chatBox, BorderLayout.EAST);

        // initiate a new sub-content section for UserList
        JPanel userList = new JPanel();
        userList.setLayout(new BorderLayout());
        // UserList label
        JLabel userListLabel = new JLabel("User List");
        userListLabel .setFont(new Font("Monaco", Font.PLAIN, 14));
        userListLabel .setBounds(640, 200, 60, 30);

        this.users[0] = "";
        list = new JList(this.users);
        list.setBounds(640, 180, 200, 200);
        list.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, Color.LIGHT_GRAY));

        //kick
        //JButton btnKick = new JButton("Kick");
        btnKick.setBounds(770, 400, 80, 30);
        btnKick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!Objects.equals(kickingUser, userName) && Objects.equals(userName, managerName)) {
                        whiteboardServer.removeUser(kickingUser);
                        whiteboardServer.updateUserList();
                    }
                } catch (RemoteException e1) {
                    System.out.println("error in kicking");
                }
            }
        });

        // combine components to the chat section
        userList.add(userListLabel, BorderLayout.NORTH);
        userList.add(list, BorderLayout.CENTER);
        userList.add(btnKick, BorderLayout.SOUTH);
        // add chat section to the main panel
        content.add(userList, BorderLayout.WEST);

        // window listener
        addWindowListener(this);

        // user list selection-listener
        list.addListSelectionListener(this);
    }

    public Draw getDrawPanel() {
        return drawArea;
    }

    public JList getUserList() {
        return list;
    }
    //get textarea
    public JTextArea getTextArea() {
        return textArea;
    }

    public JButton getKickingBtn(){
        return btnKick;
    }

    public iRemoteWhiteboard getWhiteboardServer(){
        return this.whiteboardServer;
    }

    public void setWhiteboardServer(iRemoteWhiteboard whiteboard){
        this.whiteboardServer = whiteboard;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            this.whiteboardServer.removeUser(userName);
            this.whiteboardServer.updateUserList();
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        this.kickingUser = (String) this.list.getSelectedValue();
    }

    private void myEvent(){
        itemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDia.setVisible(true);
                String fileDirectory = openDia.getDirectory();
                String FileName = openDia.getFile();
                if (fileDirectory == null || FileName == null) {
                    return;
                }
                File file = new File(fileDirectory, FileName);
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    drawArea.load(bufferedImage);
                    drawArea.updateCanvas();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        itemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawArea.clear();
            }
        });

        itemClose.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                drawArea.clear();
            }
        });
        itemQuit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    whiteboardServer.removeUser(userName);
                    whiteboardServer.updateUserList();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });
    }
}
