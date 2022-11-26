package segmentedfilesystem;
import java.net.*;
import java.io.*;

public class FileRetriever {

        InetAddress netAddress;
        int portNumber;

        // This constructor should take the server name and port number
	public FileRetriever(String netAddress, int portNumber) {
        
        try {
                this.netAddress = InetAddress.getByName(netAddress);
                this.portNumber = portNumber;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
	}

	public void downloadFiles() {
        // Do all the heavy lifting here.
        // This should
        //   * Connect to the server
        //   * Download packets in some sort of loop
        //   * Handle the packets as they come in by, e.g.,
        //     handing them to some PacketManager class
        // Your loop will need to be able to ask someone
        // if you've received all the packets, and can thus
        // terminate. You might have a method like
        // PacketManager.allPacketsReceived() that you could
        // call for that, but there are a bunch of possible
        // ways.

        byte[] buf_Sed = new byte[256];
        byte[] buf_Rec = new byte[1028];

        try {
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket datagramPacket = new DatagramPacket(buf_Sed, buf_Sed.length, netAddress, portNumber);
        
        datagramSocket.send(datagramPacket);
        PackageManager packageManager = new PackageManager();
        
        while (packageManager.packets.size() < PackageManager.totalPackets) {
                DatagramPacket packet = new DatagramPacket(buf_Rec, buf_Rec.length);
                datagramSocket.receive(packet);
                packageManager.packetIns(packet);
        }

        datagramSocket.close();
        packageManager.fileOrganizer();
        packageManager.packetOrganizer();

        for (int i = 0; i < packageManager.packetOrg.size(); ++i) {
                File newFile = new File(new String(packageManager.packetOrg.get(i).get(0).data, 0, packageManager.packetOrg.get(i).get(0).data.length));
                
                System.setOut(new PrintStream(newFile));
                for (int j = 1; j < packageManager.packetOrg.get(i).size(); j++) {
                        System.out.write(packageManager.packetOrg.get(i).get(j).data);
                        System.out.flush();
                }
                System.out.flush();
        }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        }
}
