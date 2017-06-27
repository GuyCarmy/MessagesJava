import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Created by Guy Carmy on 25/06/2017.
 * ID: 301726154
 *
 * this is the server side for the messages program
 * it contains a clients list to send the messages to
 * it is also a panel for the gui
 *
 */

// todo make sure it works with more then 1 client on same pc. maybe address is not the same and only host name?
public class MessagesServer extends Panel {
    //panel components
    private JTextArea textArea;
    private JScrollPane jsp;
    private JButton sendBtn;
    //udp components
    private DatagramSocket socket;
    private DatagramPacket packet;
    private HashMap<String,Client> clientsList;
    private int port = 6666;
    private MessagesServerThread serverThread;

    //constructor
    public MessagesServer(){
        //panel
        textArea=new JTextArea(10,30);
        textArea.setEditable(true);
        jsp=new JScrollPane(textArea);
        sendBtn=new JButton("Click to Send");
        sendBtn.addActionListener(new SendListener());
        this.setLayout(new BorderLayout());
        this.add(jsp,BorderLayout.CENTER);
        this.add(sendBtn,BorderLayout.SOUTH);

        //socket on said port
        try {
            socket=new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("failed creating socket");
            System.exit(1);
        }

        //an hashmap for the clients list
        clientsList=new HashMap<String,Client>();
        //server thread runs simultaneously and wait for join / leave messages from the clients
        //the thread also manages the clients list
        serverThread=new MessagesServerThread(socket,clientsList);
        serverThread.start();
    }


    //the send button listener.
    //creates a buffer with the text written on the jtextarea and send it to all the clients in a loop
    public class SendListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            //creating a buffer from the text
            byte buf[];
            buf=(textArea.getText()).getBytes();
            //iterate through the clients list
            Iterator it=clientsList.values().iterator();
            while(it.hasNext()){
                Client c=(Client)it.next();
                try {
                    //creating a packet with the buffer and the client address and port for each client
                    packet = new DatagramPacket(buf, buf.length,c.getAddress(),c.getPort());
                    //sending the packet
                    socket.send(packet);
                } catch (UnknownHostException e1) {
                    System.out.println("failed getting host address");
                    System.exit(1);
                } catch (IOException e1) {
                    System.out.println("failed sending packet");
                    System.exit(1);
                }
            }
            //clearing the text area for the next message
            textArea.setText("");

        }
    }

    //main for the server. creating a frame and adding the panel.
    public static void main(String[]args){
        MessagesServer server= new MessagesServer();
        JFrame frame=new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add(server);
        frame.setVisible(true);

    }
}
