
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

/**
 * Created by Guy Carmy on 25/06/2017.
 * ID: 301726154
 *
 * this is an assisting thread that always waits for packets from the clients to add or remove them from the clients list
 */
public class MessagesServerThread extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private HashMap<String,Client> clientList;

    //constructor
    MessagesServerThread(DatagramSocket socket,HashMap<String,Client> cL){
        //the socket and list created in MessagesServer
        this.socket=socket;
        this.clientList=cL;
        //a buffer for the packet
        byte buf[]=new byte[256];
        //a packet for receiving messages
        packet=new DatagramPacket(buf,buf.length);

    }

    @Override
    public void run() {
        //server is receiving as long as its running
        while(true){
            try {
                //waiting for a packet
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("Failed receiving clients");
                System.exit(1);
            }
            //get the message from the packet
            byte buf[];
            buf= packet.getData();
            String str=new String(buf,0,packet.getLength());
            //handles with join
            if (str.equals("Join")) {
                //creating a client
                Client c = new Client(packet.getAddress(), packet.getPort());
                //adding to the list. the key is the Clients toString
                clientList.put(c.toString(),c);
            }
            //handles with removing a client
            else if (str.equals("Leave")) {
                //remove from list
                clientList.remove(packet.getAddress().getHostName());
            }

        }

    }
}
