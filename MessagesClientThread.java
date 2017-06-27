import java.io.IOException;
import java.net.*;
import java.util.Date;

/**
 * Created by Guy Carmy on 25/06/2017.
 * ID: 301726154
 *
 * this class handles with all the connection (UDP) of the client
 * it is a thread so it runs simultaneously and does not interfere with the GUI
 */
public class MessagesClientThread extends Thread {
    //UDP components
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress currentServer;
    private int port = 6666;
    //a boolean for the loop in run to know if it should wait for messages from server or not
    private boolean connected = false;
    //will hold the panel that it was called from so it can return data
    private MessagesClientPanel panel;

    //constructor
    public MessagesClientThread(MessagesClientPanel panel){
        //set the panel
        this.panel=panel;
        //set the socket
        try {
            socket=new DatagramSocket();
        } catch (IOException e) {
            System.out.println("failed creating socket");
            System.exit(1);
        }
    }

    //send a "leave" message to the current server its connected to
    public void leaveServer(){
        try {
            //creating a packet with a buffer that says "Leave"
            String str="Leave";
            byte buf[];
            buf=str.getBytes();
            DatagramPacket p=new DatagramPacket(buf,buf.length,currentServer,port);
            //sending the leave message
            socket.send(p);
            //setting the connected boolean to false
            connected=false;
        } catch (IOException e1) {
            System.out.println("failed disconnecting from server");
            System.exit(1);
        }
    }

    //send a "Join" message to the given server
    public void setServer(String serverName){
        try {
            //creating a packet with a buffer that says "Join" and with server name
            String str="Join";
            byte buf[];
            buf=str.getBytes();
            //set current server for the leave method
            currentServer=InetAddress.getByName(serverName);
            DatagramPacket p=new DatagramPacket(buf,buf.length,currentServer,port);
            //sending the message
            socket.send(p);
            //setting the connected boolean to true
            connected=true;
        } catch (UnknownHostException e1) {
            System.out.println("failed making address");
            System.exit(1);
        } catch (IOException e1) {
            System.out.println("failed connecting to server");
            System.exit(1);
        }
    }

    //the thread is waiting to receive messages as long as the boolean connected is true
    @Override
    public void run() {
        while(connected) {
            //a buffer and packet to hold the message
            byte buf[] = new byte[512];
            packet = new DatagramPacket(buf, buf.length);
            //wait for a packet
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("failed receiving message");
                System.exit(1);
            }
            //create a string with the message from the packet
            buf=packet.getData();
            String message=(new String(buf)).substring(0,packet.getLength());
            //create a string with the date and time
            String date=new Date().toString();
            //telling the panel to append the message
            panel.appendMsg(date);
            panel.appendMsg(" >> ");
            panel.appendMsg(message);
            panel.appendMsg("\n");
        }
    }
}
