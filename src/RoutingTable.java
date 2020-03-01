import java.util.Map;

public class RoutingTable {
//    private List<TableEntry> routingTable;
    private Map<String, TableEntry> routingTable;

    public RoutingTable(Map<String, TableEntry> routingTable) {
        this.routingTable = routingTable;
    }

    public Map<String, TableEntry> getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(Map<String, TableEntry> routingTable) {
        this.routingTable = routingTable;
    }

    public void addEntry(String podAddress, TableEntry entry) {
        routingTable.put(podAddress, entry);
    }

    public String prepareRoutingTableToSend(int count) {
        String routingTableString = "";
        // checking if this is not the first packet to be sent.
        if (count >= 1) {
            for (int i = 0; i < routingTable.size(); i++) {
                TableEntry currentEntry = routingTable.get(i);
                routingTableString += currentEntry.getAddress() + "--";
                routingTableString += currentEntry.getNextHop() + "--";
                routingTableString += currentEntry.getCost() + "--";
                routingTableString += currentEntry.getTime() + "---";
            }
        }
        return DataStore.getPodID() + "-" + DataStore.getPodAddress() + "-" + DataStore.getPodIP() + "-" + routingTableString;
    }
}
