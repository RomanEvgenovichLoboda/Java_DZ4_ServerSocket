import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyListener implements Runnable{
    MyGUI myGUI;
    Socket socket = null;
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;
    File file = null;
    String msg ="";
    public MyListener(Socket s,MyGUI gui){
        this.myGUI = gui;
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            JOptionPane.showMessageDialog(this.myGUI, "Client connected " + this.socket.getInetAddress().getHostName(), "Connect:", JOptionPane.PLAIN_MESSAGE);
            myGUI.usersModel.addElement(this.socket.getInetAddress().getHostName());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            do {
                try {
                    msg = (String)inputStream.readObject();
                    myGUI.filesModel.addElement(msg);
//                    JOptionPane.showMessageDialog(this.myGUI, "client> " + msg, "Message:", JOptionPane.PLAIN_MESSAGE);
                    if(msg.equals("exit"))
                    {
                        break;
                    }
                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date d = new Date();
                    sendMessage("Message received " + df.format(d));
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(this.myGUI, e.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
                    BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(inputStream.readAllBytes()));
                    JFrame jFrame = new JFrame("Image");
                    jFrame.setSize(bImage.getWidth(),bImage.getHeight());
                    jFrame.setAlwaysOnTop(true);
                    JPanel panel = new JPanel(){
                        @Override
                        public void paint(Graphics g) {
                            super.paint(g);
                            g.drawImage(bImage,0,0,bImage.getWidth(),bImage.getHeight(),null);
                        }
                    };
                    panel.setSize(bImage.getWidth(),bImage.getHeight());
                    jFrame.add(panel);
                    jFrame.setVisible(true);
                }
            } while (!msg.equals("exit"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this.myGUI, e.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
        }
    }
    void sendMessage(String m)
    {
        try {
            outputStream.writeObject(m);
            outputStream.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this.myGUI, e.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
