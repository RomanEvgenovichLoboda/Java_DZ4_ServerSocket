import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyGUI extends JFrame {
    ServerSocket listener = null;
    Socket client = null;
    int maxCount = 10; // max number of clients
    int count = 0;     // current client number
    int port = 8888;
    private JList<String> usersList = new JList<>();
    public DefaultListModel<String> usersModel = new DefaultListModel<String>();
    public DefaultListModel<String> filesModel = new DefaultListModel<String>();
    private JList<String> imagesList = new JList<>();
    private Container container;
    public MyGUI(){
        super("Server");
        this.setBounds(100,100,250,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        usersList.setToolTipText("Users");
        usersList.setModel(usersModel);
        imagesList.setModel(filesModel);
        imagesList.setToolTipText("Images");

        container = this.getContentPane();
        container.setLayout(new GridLayout(1,2,2,2));
        container.add(usersList);
        container.add(imagesList);
    }
    public void createConnection(){
        try {
            listener = new ServerSocket(port,maxCount);
            while (count <= maxCount){
                count++;
                client = listener.accept();
                MyListener ml = new MyListener(client,this);
                Thread t = new Thread(ml);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error:", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
