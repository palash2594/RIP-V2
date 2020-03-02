import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PodManager {

    private RoutingTable routingTable;
    private DatagramSocket socket = null;
    private InetAddress group;
    private byte[] buffer;
    private static ThreadPoolExecutor executor;

    public void initialization(int podID) throws UnknownHostException {
        DataStore.setPodID(podID);
        String podAddress = "10.0." + podID + ".0";
        DataStore.setPodAddress(podAddress);

        String podIP = InetAddress.getLocalHost().getHostAddress().trim();
        DataStore.setPodIP(podIP);

        int noOfProcessors = Runtime.getRuntime().availableProcessors();
        // Setting the Threadpool executor.
        DataStore.setExecutor((ThreadPoolExecutor) Executors.newFixedThreadPool(noOfProcessors));
        executor = DataStore.getExecutor();

        // initializing the routing table.
        RoutingTable routingTable = new RoutingTable(new HashMap<String, TableEntry>());
        DataStore.setRoutingTable(routingTable);

        TableEntry tableEntry = new TableEntry(podAddress, podIP, 0, new Date().getTime());
        DataStore.getRoutingTable().addEntry(podAddress, tableEntry);

        DataStore.setAddressToIPMapping(new HashMap<>());

        DataStore.setNonRechableDirectly(new HashMap<String, Boolean>());

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PodManager podManager = new PodManager();

        podManager.initialization(Integer.parseInt(args[0]));

        // starting send packet thread.
        podManager.sendPacket();

        // starting receive packet thread.
        podManager.receivePacket();

        //starting timeout checking thread.
        podManager.checkTimeouts();

    }

    public void sendPacket() throws InterruptedException {
        SendPacket sendPacket = new SendPacket();
        executor.submit(sendPacket);
    }

    public void receivePacket() {
        ReceivePacket receivePacket = new ReceivePacket();
        executor.submit(receivePacket);
    }

    public void checkTimeouts() {
        CheckTimeouts checkTimeouts = new CheckTimeouts();
        executor.execute(checkTimeouts);
    }

}
