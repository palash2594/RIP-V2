import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ThreadPoolExecutor;

public class SendPacket extends Thread {

    private RoutingTable routingTable;
    private DatagramSocket socket = null;
    private InetAddress group;
    private byte[] buffer;
    private int count = 0; // if count if zero that means request command will be used.

    public void sendPacket() throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(DataStore.getMulticastIP());
        String ripPacketString = RoutingTable.prepareRoutingTableToSend(count);
        count ++;

        buffer = ripPacketString.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public void run() {
        try {
            while(true) {
                sendPacket();
                sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
