import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReceivePacket extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    public void run() {
        try {
            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName(DataStore.getMulticastIP());
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateRoutingTable(String routingTableString) {
        // TODO: 2/25/20 parse routing table string
    }
}
