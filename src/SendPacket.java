import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Arrays;

public class SendPacket extends Thread {

    private RoutingTable routingTable;
    private DatagramSocket socket = null;
    private InetAddress group;
    private byte[] buffer;
    private int count = 0; // if count if zero that means request command will be used.

    public void sendPacket() throws IOException {
        System.out.println("Inside send packet.");
        socket = new DatagramSocket();
        group = InetAddress.getByName(DataStore.MULTICAST_IP);
        String ripPacketString = RoutingTable.prepareRoutingTableToSend(count);
        System.out.println(ripPacketString);
        count ++;

        buffer = ripPacketString.getBytes();
        System.out.println(Arrays.toString(buffer));

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public void run() {
        try {
            while(true) {
                sendPacket();
                System.out.println("inside run function.");
                sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
