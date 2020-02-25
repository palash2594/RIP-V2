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

    public void getPacketBytes(String command) {
        // size of routing table.
        int size = routingTable.getRoutingTable().size();

        byte[] packetInBytes = new byte[size];
        int counter = 0;
        // request command.
        // TODO: 2/25/20 augment this part
        if (command == "0") {
            packetInBytes[counter++] = (byte)Integer.parseInt(command);

            // version
            packetInBytes[counter++] = (byte)Integer.parseInt(VERSION);

            // must be zero field.
            packetInBytes[counter++] = (byte)Integer.parseInt(MUST_BE_ZERO);
        } else {
            packetInBytes[counter++] = (byte)Integer.parseInt(command);

            // version
            packetInBytes[counter++] = (byte)Integer.parseInt(VERSION);

            // must be zero field.
            packetInBytes[counter++] = (byte)Integer.parseInt(MUST_BE_ZERO);

            // routing table.
            for (int i = 0; i < routingTable.getRoutingTable().size(); i++) {

            }
        }

    }

}
