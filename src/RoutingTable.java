/**
 * This class contains the routing table implementation.
 *
 * @author: Palash Jain
 *
 * @version: 1.0
 */

import java.util.Map;

public class RoutingTable {
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
}
