public class DataStore {

    private static int podID;
    private static String podIP;
    private static String podAddress;
    private static String multicastIP;
    private static RoutingTable routingTable;

    public static int getPodID() {
        return podID;
    }

    public static void setPodID(int podID) {
        DataStore.podID = podID;
    }

    public static String getPodIP(String podIP) {
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

    public static String getMulticastIP() {
        return multicastIP;
    }

    public static void setMulticastIP(String multicastIP) {
        DataStore.multicastIP = multicastIP;
    }

    public static RoutingTable getRoutingTable() {
        return routingTable;
    }

    public static void setRoutingTable(RoutingTable routingTable) {
        DataStore.routingTable = routingTable;
    }
}
