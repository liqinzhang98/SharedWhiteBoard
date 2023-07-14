package createWhiteboard;

import remote.iRemoteWhiteboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Draw extends JComponent implements ActionListener,
        MouseListener, MouseMotionListener{
    private Image image;
    private Graphics2D g2;
    private int currentX, currentY, oldX, oldY;
    private iRemoteWhiteboard whiteboard;
    private String toolType = "pencil";
    public Draw() {
        setDoubleBuffered(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    public void drawComponent(){
        if (Objects.equals(this.toolType, "pencil")) {
            g2.drawLine(oldX, oldY, currentX, currentY);
            oldX = currentX;
            oldY = currentY;

        } else if (Objects.equals(this.toolType, "rectangle")) {
            g2.drawRect(Math.min(oldX, currentX), Math.min(oldY, currentY),
                    Math.abs(oldX - currentX), Math.abs(oldY - currentY));
            g2.fillRect(Math.min(oldX, currentX), Math.min(oldY, currentY),
                    Math.abs(oldX - currentX), Math.abs(oldY - currentY));

        } else if (Objects.equals(this.toolType, "triangle")) {
            int x3 = getThirdPoint(oldX, oldY, currentX, currentY,
                    calculateDistance(oldX, oldY, currentX, currentY))[0];
            int y3 = getThirdPoint(oldX, oldY, currentX, currentY,
                    calculateDistance(oldX, oldY, currentX, currentY))[1];
            g2.drawPolygon(new int[]{oldX, currentX, x3}, new int[]{oldY, currentY, y3},3);
            g2.fillPolygon(new int[]{oldX, currentX, x3}, new int[]{oldY, currentY, y3},3);


        } else if (Objects.equals(this.toolType, "line")) {
            g2.drawLine(oldX, oldY, currentX, currentY);

        } else if (Objects.equals(this.toolType, "circle")) {
            g2.drawOval(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.max(Math.abs(oldX - currentX),
                    Math.abs(oldY - currentY)),Math.max(Math.abs(oldX - currentX),Math.abs(oldY - currentY)));
            g2.fillOval(Math.min(oldX, currentX), Math.min(oldY, currentY), Math.max(Math.abs(oldX - currentX),
                    Math.abs(oldY - currentY)),Math.max(Math.abs(oldX - currentX),Math.abs(oldY - currentY)));

        } else if (Objects.equals(this.toolType, "text")) {
            String textInput;
            textInput = JOptionPane.showInputDialog(
                    "write something");
            if(textInput != null) {
                g2.drawString(textInput, oldX, oldY);

            }
        }
        repaint();
        this.updateCanvas();
    }
    // share the image to all the canvas
    public void updateCanvas() {
        try {
            BufferedImage image = new BufferedImage(this.getSize().width,this.getSize().height,
                    BufferedImage.TYPE_INT_BGR);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Graphics2D newImage = image.createGraphics();
            this.paint(newImage);
            newImage.dispose();
            ImageIO.write(image,"png", out);
            byte[] imgArr = out.toByteArray();
            whiteboard.updateAll(imgArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        // initiate the white board with plain image
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            initiate();
        }
        g.drawImage(image, 0, 0, null);
    }
    // calculate the third point of a triangle
    public int calculateDistance(int x1, int y1, int x2, int y2){
        return (int)Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2));
    }
    // calculate the third point of a triangle
    public int[] getThirdPoint(int x1, int y1, int x2, int y2, int distance){
        int[] result = new int[2];
        // Vector of A > B
        double abx = x2 - x1;
        double aby = y2 - y1;
        double n = Math.toRadians(120.0);
        double rx = (abx * Math.cos(n)) - (aby * Math.sin(n));
        double ry = (abx * Math.sin(n)) + (aby * Math.cos(n));
        double cx = x2 + rx;
        double cy = y2 + ry;
        result[0] = (int)cx;
        result[1] = (int)cy;
        return result;
    }
    public void clear() {
        // clear everything
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
        updateCanvas();
    }

    public void initiate(){
        // initiate a white board
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    public void load(BufferedImage image) {
        g2.drawImage(image, 0, 0, null);
        repaint();
    }
    // all colours
    public void red() {
        g2.setPaint(Color.red);
    }

    public void black() {
        g2.setPaint(Color.black);
    }

    public void magenta() {
        g2.setPaint(Color.magenta);
    }

    public void green() {
        g2.setPaint(Color.green);
    }

    public void blue() {
        g2.setPaint(Color.blue);
    }

    public void yellow() {g2.setPaint(Color.YELLOW);}

    public void orange() {g2.setPaint(Color.ORANGE);}

    public void cyan() {g2.setPaint(Color.CYAN);}

    public void gray() {g2.setPaint(Color.GRAY);}

    public void pink() {g2.setPaint(Color.PINK);}

    public void lGray() {g2.setPaint(Color.LIGHT_GRAY);}

    public void lBlue() {g2.setPaint(new Color(0, 102, 102));}

    public void dGreen() {g2.setPaint(new Color(51, 102, 0));}

    public void dPurple() {g2.setPaint(new Color(102, 0, 102));}

    public void white() {g2.setPaint(Color.WHITE);}

    public void olive() {g2.setPaint(new Color(102, 102, 0));}

    public void setWhiteboard(iRemoteWhiteboard whiteboard) {
        this.whiteboard = whiteboard;
    }

    public void setToolType(String type) {
        this.toolType = type;
        System.out.println("new tool chose: " + type);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!Objects.equals(this.toolType, "text")){
            oldX = e.getX();
            oldY = e.getY();
            drawComponent();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // save coord x,y when mouse is pressed
        oldX = e.getX();
        oldY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!Objects.equals(this.toolType, "pencil")) {
            currentX = e.getX();
            currentY = e.getY();
            drawComponent();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (Objects.equals(this.toolType, "pencil")){
            currentX = e.getX();
            currentY = e.getY();
            if (g2 != null) {
                drawComponent();
            }
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
