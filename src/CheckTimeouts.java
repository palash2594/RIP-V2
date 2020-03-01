import java.util.Map;

public class CheckTimeouts extends Thread {

    private final int TEN_SECONDS = 11;
    private final int INFINITY = 16;

    public void run() {
        try {
            Map<String, TableEntry> myRoutingTable = DataStore.getRoutingTable().getRoutingTable();
            String myAddress = DataStore.getPodAddress();
            ReceivePacket receivePacket = new ReceivePacket();
            while (true) {
                for (Map.Entry<String, TableEntry> entry : myRoutingTable.entrySet()) {
                    if (!entry.getValue().getAddress().equals(myAddress)) {
                        TableEntry currentEntry = entry.getValue();
                        if (((System.currentTimeMillis() - currentEntry.getTime()) / 1000) > TEN_SECONDS) {
                            currentEntry.setCost(INFINITY);
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
}
