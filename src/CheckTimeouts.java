import java.util.HashMap;
import java.util.Map;

public class CheckTimeouts extends Thread {

    private final int TEN_SECONDS = 10;
    private final int INFINITY = 16;

    public void run() {
        Map<String, Boolean> isSet = new HashMap<>();
        try {
            Map<String, TableEntry> myRoutingTable = DataStore.getRoutingTable().getRoutingTable();
            String myAddress = DataStore.getPodAddress();
            ReceivePacket receivePacket = new ReceivePacket();
            while (true) {
                for (Map.Entry<String, TableEntry> entry : myRoutingTable.entrySet()) {

                    if (!entry.getValue().getAddress().equals(myAddress)) {
                        TableEntry currentEntry = entry.getValue();

                        if (((System.currentTimeMillis() - currentEntry.getTime()) / 1000) > TEN_SECONDS && !isSet.containsKey(currentEntry.getAddress())) {
                            isSet.put(currentEntry.getAddress(), true);
                            currentEntry.setCost(INFINITY);
                            removeAllUsageOfUnreachableIP(currentEntry.getAddress());
                            receivePacket.displayRoutingTable();
                        }
                    }
                }
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeAllUsageOfUnreachableIP(String unreachableAddress) {
        String unreachableIP = DataStore.getAddressToIPMapping().get(unreachableAddress);
        Map<String, TableEntry> myRoutingTable = DataStore.getRoutingTable().getRoutingTable();
        String defaultIP = "0.0.0.0";

        for (Map.Entry<String, TableEntry> entry : myRoutingTable.entrySet()) {
            TableEntry currentEntry = entry.getValue();
            if (currentEntry.getNextHop().equals(unreachableIP)) {
                currentEntry.setCost(INFINITY);
                currentEntry.setNextHop(defaultIP);
            }
        }
    }
}
