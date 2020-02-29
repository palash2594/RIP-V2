import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RIPPacket {

    private final String REQUEST = "1";
    private final String RESPONSE = "2";
    private final String VERSION = "2";
    private final String MUST_BE_ZERO = "0";

    private String command;
    private String version;
    private RoutingTable routingTable;
    private String mustBeZero;

    // for request update.
    public RIPPacket() {
        this.command = REQUEST;
        this.version = VERSION;
        this.mustBeZero = MUST_BE_ZERO;
    }

    // for response update.
    public RIPPacket(RoutingTable routingTable) {
        this.command = RESPONSE;
        this.version = VERSION;
        this.routingTable = routingTable;
        this.mustBeZero = MUST_BE_ZERO;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    public byte[] preparePacket(int command) {

        List<TableEntry> routingTable = DataStore.getRoutingTable().getRoutingTable();
        int myID = DataStore.getPodID();
        String[] myIP = DataStore.getPodIP().split(".");
        // size of routing table.
        int size = routingTable.size();

        byte[] packetInBytes = new byte[8 + size * 9];
        int counter = 0;

        // command type, 1 => request, 2 => response.
        packetInBytes[counter++] = (byte) command;

        // version
        packetInBytes[counter++] = (byte) Integer.parseInt(VERSION);

        // must be zero field.
        packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);

        packetInBytes[counter++] = (byte) myID;

        for (String ip : myIP) {
            packetInBytes[counter++] = (byte) Integer.parseInt(ip);
        }

        System.out.println("routing table size: " + routingTable.size());
        // routing table.
        for (int i = 0; i < routingTable.size(); i++) {
            TableEntry currentEntry = routingTable.get(i);
            String[] address = currentEntry.getAddress().split(".");
            String[] nextHop = currentEntry.getNextHop().split(".");
            int cost = currentEntry.getCost();
            for (String add : address) {
                packetInBytes[counter++] = (byte) Integer.parseInt(add);
            }
            for (String hop : nextHop) {
                packetInBytes[counter++] = (byte) Integer.parseInt(hop);
            }
            packetInBytes[counter++] = (byte) cost;
            // no need to send the time.
        }
        return packetInBytes;
    }

    public RoutingTable readPacket(byte[] packet, int length) {
        List<TableEntry> listEntry = new ArrayList<>();
        RoutingTable receivedRoutingTable = new RoutingTable(listEntry);

        int counter = 0;
        int command = packet[counter++] & 0xff;
        int version = packet[counter++] & 0xff;
        int mustBeZero = packet[counter++] & 0xff;
        int podID = packet[counter++] & 0xff;
        String podIP = "";

        for (int i = 0; i < 4; i++) {
            podIP += (packet[counter++] & 0xff) + ".";
        }
        podIP = podIP.substring(0, podIP.length() - 1); // removing the last dot (.).
        if (command == 1) {
            // request
        } else {
            // response
            while (counter < length) {
                String address = "";
                for (int i = 0; i < 4; i++) {
                    address += (packet[counter++] & 0xff) + ".";
                }
                address += address.substring(0, address.length() - 1);

                String nextHop = "";
                for (int i = 0; i < 4; i++) {
                    nextHop += (packet[counter++] & 0xff) + ".";
                }
//                System.out.println(counter);
                nextHop += nextHop.substring(0, nextHop.length() - 1);

                int cost = packet[counter++] & 0xff;
                Date date = new Date();
                TableEntry currentEntry = new TableEntry(address, nextHop, cost, date.getTime());
                receivedRoutingTable.addEntry(currentEntry);
            }
        }
        System.out.println("*****inside read packet*****");
        return routingTable;
    }
}