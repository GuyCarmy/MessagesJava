import java.net.InetAddress;

/**
 * Created by Guy Carmy on 25/06/2017.
 * ID: 301726154
 *
 * a very basic class to represent a clients address and port.
 */
public class Client {
    private InetAddress address;
    private int port;

    public Client(InetAddress a,int p){
        address=a;
        port=p;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return address+" "+port;
    }
}
