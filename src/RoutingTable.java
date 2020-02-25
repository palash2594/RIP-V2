import java.util.List;

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

    public void prepareRoutingTableToSend() {
        String routingTableString = "";
        for (int i = 0; i  < routingTable.size(); i++) {
            TableEntry currentEntry = routingTable.get(i);

        }
    }
}
