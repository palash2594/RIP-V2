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
                extractRoutingTable(packet.getData(), packet.getLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void extractRoutingTable(byte[] receivedPacket, int length) {
        int podID = DataStore.getPodID();
        if ((receivedPacket[3] & 0xff) == podID) {
            // packet is it's own.
            return;
        }
        RIPPacket ripPacket = new RIPPacket();
        // Parsing packet to get the routing table.
        RoutingTable receivedRoutingTable = ripPacket.readPacket(receivedPacket, length);
        String receivedIP = ripPacket.getIP(receivedPacket);
        System.out.println("*****inside update routing.*****");

        updateRoutingTable(receivedRoutingTable, receivedIP);

    }

    public void updateRoutingTable(RoutingTable receivedRoutingTable, String receivedIP) {
        Map<String, TableEntry> myRoutingTable = DataStore.getRoutingTable().getRoutingTable();
        boolean triggerFlag = false;
        try {
            for (Map.Entry<String, TableEntry> entry : receivedRoutingTable.getRoutingTable().entrySet()) {
                TableEntry currentTableEntry = entry.getValue();

                // new entry.
                if (!myRoutingTable.containsKey(entry.getKey())) {
                    currentTableEntry.setCost(currentTableEntry.getCost() + 1);
                    myRoutingTable.put(entry.getKey(), currentTableEntry);
                } else { // existing entry.
                    // calculating new cost.
                    int newCost = currentTableEntry.getCost() + 1;
//                    myRoutingTable.get(entry.getKey()).setTime(System.currentTimeMillis());
                    // checking if the new cost is lower than the previous cost.
                    if (newCost < myRoutingTable.get(entry.getKey()).getCost()) {
                        myRoutingTable.get(entry.getKey()).setCost(newCost);
                        myRoutingTable.get(entry.getKey()).setNextHop(receivedIP);
                        triggerFlag = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        displayRoutingTable();

        if (triggerFlag) {
            // TODO: 2/29/20 send triggered update.
        }
    }

    public void displayRoutingTable() {
        Map<String, TableEntry> routingTable = DataStore.getRoutingTable().getRoutingTable();
        System.out.println("Routing table for node : " + DataStore.getPodID());
        System.out.println("|-------------------------------------------------------|");
        System.out.println("| Address\t| Next Hop\t| Cost\t| Time\t\t|");
        System.out.println("|-------------------------------------------------------|");
        for (Map.Entry<String, TableEntry> entry : routingTable.entrySet()) {
            TableEntry currentEntry = entry.getValue();
            System.out.print("| " + currentEntry.getAddress() + "  \t| ");
            System.out.print(currentEntry.getNextHop() + "\t| ");
            System.out.print(currentEntry.getCost() + "\t| ");
            System.out.println(currentEntry.getTime() + "\t|");
        }
        System.out.println("|-------------------------------------------------------|");
    }

}
