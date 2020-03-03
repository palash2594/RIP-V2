import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendPacket extends Thread {

    private DatagramSocket socket = null;
    private InetAddress group;
    private int count = 1; // if count is one that means request command will be used.

    public void sendPacket() throws IOException {
        System.out.println("Inside send packet.");
        socket = new DatagramSocket();
        group = InetAddress.getByName(DataStore.MULTICAST_IP);
//        String ripPacketString = RoutingTable.prepareRoutingTableToSend(count);
        RIPPacket ripPacket = new RIPPacket();
        byte[] packetToSend = ripPacket.preparePacket(count);
        count++;

//        buffer = packetToSend;
//        System.out.println(Arrays.toString(buffer));
        DatagramPacket packet = new DatagramPacket(packetToSend, packetToSend.length, group, 4446);
        socket.send(packet);
        socket.close();
    }

    public void run() {
        try {
            while (true) {
                sendPacket();
//                System.out.println("inside run function.");
                sleep(5000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
