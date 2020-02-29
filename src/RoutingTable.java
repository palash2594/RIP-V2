import java.util.List;
import java.util.Map;

public class RoutingTable {
    private List<TableEntry> routingTable;

    public RoutingTable(List<TableEntry> routingTable) {
        this.routingTable = routingTable;
    }

    public List<TableEntry> getRoutingTable() {
        return routingTable;
    }

    public void setRoutingTable(List<TableEntry> routingTable) {
        this.routingTable = routingTable;
    }

    public void addEntry(TableEntry entry) {
        routingTable.add(entry);
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
