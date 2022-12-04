package segmentedfilesystem;

import java.net.*;
import java.io.*;
import java.util.*;

public class FileRetriever {

        InetAddress netAddress;
        int portNumber;
        
        byte[] buf_Sed = new byte[256];
        byte[] buf_Rec = new byte[1028];
        
        DatagramSocket datagramSocket = null;
        DatagramPacket datagramPacket;

        // This constructor should take the server name and port number
	public FileRetriever(String netAddress, int portNumber) {
        
        try {
                this.netAddress = InetAddress.getByName(netAddress);
                this.portNumber = portNumber;
                System.out.println("Server Name : " + netAddress);
                System.out.println("Port Number : " + portNumber);

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

        try {
        DatagramSocket datagramSocket = new DatagramSocket();
        DatagramPacket datagramPacket = new DatagramPacket(buf_Sed, buf_Sed.length, netAddress, portNumber);
        
        datagramSocket.send(datagramPacket);
        PackageManager packageManager = new PackageManager();
        
        while (PackageManager.totalPackets == 0 || packageManager.packets.size() < PackageManager.totalPackets) {
                datagramPacket = new DatagramPacket(buf_Rec, buf_Rec.length);
                datagramSocket.receive(datagramPacket);
                packageManager.packetIns(datagramPacket);
                buf_Rec = new byte[1028];
        }

        datagramSocket.close();
        packageManager.fileOrganizer();
        packageManager.packetOrganizer();

        for (int i = 0; i < packageManager.packetOrg.size(); i++) {

                ArrayList<packet> packetList = packageManager.packetOrg.get(i);
                String fileName = packetList.get(0).fileName;
                
                File newFile = new File(fileName);
                
                System.setOut(new PrintStream(newFile));
                for (int j = 1; j < packageManager.packetOrg.get(i).size(); j++) {
                        System.out.write(packageManager.packetOrg.get(i).get(j).data);
                        System.out.flush();
                }
                System.out.flush();
        }
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        }
}
