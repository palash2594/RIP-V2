import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PodManager {

    private static ThreadPoolExecutor executor;
    private RoutingTable routingTable;
    private DatagramSocket socket = null;
    private InetAddress group;
    private byte[] buffer;


    public static void main(String[] args) throws IOException, InterruptedException {
        int podID = Integer.parseInt(args[0]);
        DataStore.setPodID(podID);

        String podIP = InetAddress.getLocalHost().getHostAddress().trim();
        DataStore.setPodIP(podIP);

        String podAddress = "10.0." + podID + ".0/24";
        DataStore.setPodAddress(podAddress);

        PodManager manager = new PodManager();
        int noOfProcessors = Runtime.getRuntime().availableProcessors();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(noOfProcessors);

        RoutingTable routingTable = new RoutingTable(new ArrayList<TableEntry>());

        manager.sendPacket();
        manager.receivePacket();
    }

    public void sendPacket() throws InterruptedException {
        SendPacket sendPacket = new SendPacket();
        executor.submit(sendPacket);
    }

    public void receivePacket() {
        ReceivePacket receivePacket = new ReceivePacket();
        executor.submit(receivePacket);
    }

}
