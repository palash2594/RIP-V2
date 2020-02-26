public class DataStore {

    private static int podID;
    private static String podIP;
    private static String podAddress;
    private static RoutingTable routingTable;
    public final static String MULTICAST_IP = "224.0.0.9";

    public static int getPodID() {
        return podID;
    }

    public static void setPodID(int podID) {
        DataStore.podID = podID;
    }

    public static String getPodIP() {
        return DataStore.podIP;
    }

    public static void setPodIP(String podIP) {
        DataStore.podIP = podIP;
    }

    public static String getPodAddress() {
        return podAddress;
    }

    public static void setPodAddress(String podAddress) {
        DataStore.podAddress = podAddress;
    }

    public static RoutingTable getRoutingTable() {
        return routingTable;
    }

    public static void setRoutingTable(RoutingTable routingTable) {
        DataStore.routingTable = routingTable;
    }
}
