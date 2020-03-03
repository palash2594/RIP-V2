import java.util.*;

public class RIPPacket {

    private final String REQUEST = "1";
    private final String RESPONSE = "2";
    private final String VERSION = "2";
    private final String MUST_BE_ZERO = "0";
    private final int SUBNET = 255;

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
        Map<String, TableEntry> routingTable = DataStore.getRoutingTable().getRoutingTable();
        int myID = DataStore.getPodID();
        String[] myIP = DataStore.getPodIP().split("\\.");
        // size of routing table.
        int size = routingTable.size();

        byte[] packetInBytes = new byte[4 + size * 20];
        int counter = 0;

        // command type, 1 => request, 2 => response.
        packetInBytes[counter++] = (byte) command;

        // version
        packetInBytes[counter++] = (byte) Integer.parseInt(VERSION);

        // must be zero field.
        packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
        packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO); // .

        // ***********
        // loop for RTEs.
        for (Map.Entry<String, TableEntry> entry : routingTable.entrySet()) {
            // address family identifier (2 bytes).
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);

            // route tag (2 bytes).
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);

            TableEntry currentEntry = entry.getValue();
            // ip address (4 bytes).
            String[] address = currentEntry.getAddress().split("\\.");
            for (String add : address) {
                packetInBytes[counter++] = (byte) Integer.parseInt(add);
            }
            // subnet mask.
            for (int i = 0; i < 3; i++) {
                packetInBytes[counter++] = (byte) SUBNET;
            }
            packetInBytes[counter++] = (byte) 0;

            // next hop.
            String[] nextHop = currentEntry.getNextHop().split("\\.");
            for (String hop : nextHop) {
                packetInBytes[counter++] = (byte) Integer.parseInt(hop);
            }

            // metric.
            int cost = currentEntry.getCost();
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
            packetInBytes[counter++] = (byte) Integer.parseInt(MUST_BE_ZERO);
            packetInBytes[counter++] = (byte) cost;
            // no need to send the time.
        }

        // ***********
//        packetInBytes[counter++] = (byte) myID;
//
//        for (String ip : myIP) {
//            packetInBytes[counter++] = (byte) Integer.parseInt(ip);
//        }
//
//        System.out.println("routing table size: " + routingTable.size());
//        // routing table.
//        for (Map.Entry<String, TableEntry> entry : routingTable.entrySet()) {
//            TableEntry currentEntry = entry.getValue();
//            String[] address = currentEntry.getAddress().split("\\.");
//            String[] nextHop = currentEntry.getNextHop().split("\\.");
//            int cost = currentEntry.getCost();
//            for (String add : address) {
//                packetInBytes[counter++] = (byte) Integer.parseInt(add);
//            }
//            for (String hop : nextHop) {
//                packetInBytes[counter++] = (byte) Integer.parseInt(hop);
//            }
//            packetInBytes[counter++] = (byte) cost;
//            // no need to send the time.
//        }
        return packetInBytes;
    }

    public RoutingTable readPacket(byte[] packet, int length) {
        Map<String, TableEntry> listEntry = new HashMap<>();
        RoutingTable receivedRoutingTable = new RoutingTable(listEntry);
        RoutingTable myRoutingTable = DataStore.getRoutingTable();

        int counter = 0;
        int command = packet[counter++] & 0xff;
        int version = packet[counter++] & 0xff;
        counter += 2; // must be zero.
        // **********
        if (command == 1) {
            // request
        } else {
            // response
            while (counter < length) {
                // address family identifier
                counter += 2;

                // route tag.
                counter += 2;

                // ip address.
                String address = "";
                for (int i = 0; i < 4; i++) {
                    address += (packet[counter++] & 0xff) + ".";
                }
                address = address.substring(0, address.length() - 1);
                System.out.println("received address: " + address);

                // subnet mask.
                counter += 4;

                // next hop.
                String nextHop = "";
                for (int i = 0; i < 4; i++) {
                    nextHop += (packet[counter++] & 0xff) + ".";
                }
                nextHop = nextHop.substring(0, nextHop.length() - 1);

                // metric
                counter += 3;
                int cost = packet[counter++] & 0xff;
                TableEntry currentEntry = new TableEntry(address, nextHop, cost, System.currentTimeMillis());
                receivedRoutingTable.addEntry(address, currentEntry);
            }
        }
        // **********

//        int podID = packet[counter++] & 0xff;
//        String podIP = "";
//
//        for (int i = 0; i < 4; i++) {
//            podIP += (packet[counter++] & 0xff) + ".";
//        }
//        podIP = podIP.substring(0, podIP.length() - 1); // removing the last dot (.).
//        if (command == 1) {
//            // request
//        } else {
            // response
//            String podAddress = "10.0." + podID + ".0";

            // removing it from the map because we get a packet from that node.
//            DataStore.getNonRechableDirectly().remove("10.0." + podID + ".0");

            // updating time in routing table to th current time.
//            if (myRoutingTable.getRoutingTable().containsKey(podAddress)) {
//                DataStore.getRoutingTable().getRoutingTable().get("10.0." + podID + ".0").setTime(System.currentTimeMillis());
//            }
//            while (counter < length) {
//                String address = "";
//                for (int i = 0; i < 4; i++) {
//                    address += (packet[counter++] & 0xff) + ".";
//                }
//                address = address.substring(0, address.length() - 1);
//                System.out.println("received address: " + address);

//                String nextHop = "";
//                for (int i = 0; i < 4; i++) {
//                    nextHop += (packet[counter++] & 0xff) + ".";
//                }
//                nextHop = nextHop.substring(0, nextHop.length() - 1);

//                int cost = packet[counter++] & 0xff;
//                TableEntry currentEntry = new TableEntry(address, nextHop, cost, System.currentTimeMillis());
//                receivedRoutingTable.addEntry(address, currentEntry);
//            }
//        }
        System.out.println("*****inside read packet*****");
        return receivedRoutingTable;
    }

    public String getIP(byte[] packet) {

        int counter = 4;
        String podIP = "";

        for (int i = 0; i < 4; i++) {
            podIP += (packet[counter++] & 0xff) + ".";
        }
        podIP = podIP.substring(0, podIP.length() - 1); // removing the last dot (.).

        return podIP;
    }
}