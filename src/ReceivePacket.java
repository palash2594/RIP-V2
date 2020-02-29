import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Map;

public class ReceivePacket extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    public void run() {
        try {
            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName(DataStore.MULTICAST_IP);
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println("Packet length: " + packet.getLength());
                System.out.println("Received packet from: " + packet.getData()[3]);
                updateRoutingTable(packet.getData(), packet.getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Receive end.");
    }

    public synchronized void updateRoutingTable(byte[] receivedPacket, int length) {
        // TODO: 2/25/20 parse routing table string
        int podID = DataStore.getPodID();
        if ((receivedPacket[3] & 0xff) == podID) {
            // packet is it's own.
            return;
        }
        RIPPacket ripPacket = new RIPPacket();
        RoutingTable receivedRoutingTable = ripPacket.readPacket(receivedPacket, length);
        System.out.println("*****inside update routing.*****");

        Map<String, TableEntry> myRoutingTable = DataStore.getRoutingTable().getRoutingTable();

        try {
            for (Map.Entry<String, TableEntry> entry : receivedRoutingTable.getRoutingTable().entrySet()) {
                if (!myRoutingTable.containsKey(entry.getKey())) {
                    TableEntry currentTableEntry = entry.getValue();
                    System.out.println("My id " + DataStore.getPodID() + " Received addr: " + currentTableEntry.getAddress());
                    currentTableEntry.setCost(currentTableEntry.getCost() + 1);
                    myRoutingTable.put(entry.getKey(), currentTableEntry);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        DataStore.setRoutingTable(new RoutingTable(myRoutingTable));

    }
}
