package segmentedfilesystem;

import java.util.*;
import java.net.*;

// The packet manager should be able to take a packet and add it to the list of packets
public class PackageManager {
    static int totalPackets = 0;
    static int tempPackets = 0;
    static int endNum = 0;
    ArrayList<packet> packets = new ArrayList<packet>();

    // This method should take a packet and add it to the list of packets
    public void packetIns(DatagramPacket packet) {
        int dataLength = packet.getLength();
        byte[] packetData = packet.getData();
        if ((1 & packet.getData()[0]) == 1) {
            packets.add(new packetData(packetData, dataLength));
        } else {
            packets.add(new packetHeader(packetData, dataLength));
        }
    }
    
    ArrayList<ArrayList<packet>> packetOrg = new ArrayList<ArrayList<packet>>();

    // This method should take the list of packets and write them to the appropriate files
    public void fileOrganizer(){
        for (packet pack : packets){
            for (int i = 0; i < 256; i++) {
                if (packetOrg.size() > i && packetOrg.get(i).get(0).fileID == pack.fileID){
                    packetOrg.get(i).add(pack);
                    break;
                } else if (packetOrg.size() <= i) {
                    packetOrg.add(new ArrayList<packet>());
                    packetOrg.get(i).add(pack);
                    break;
                }
            }
        }
    }

    // This method organizes the packets into files
    public void packetOrganizer(){
        for (ArrayList<packet> arrayList : packetOrg) {
            arrayList.sort(new packetCompare());
        }
    }
}

// This constructor defines packet manager
class packet {
    int packetNumber = 0;
    int fileID;
    byte[] data;
    String fileName;
}

// This constructor creates a packet object and adds it to the list of packets
class packetHeader extends packet {
    packetHeader(byte[] packet, int dataLength) {
        data = new byte[dataLength - 2];
        int index = 0;
        for (int i = 2; i < dataLength; ++i) {
            data[index++] = packet[i];
        }
        fileID = packet[1];
        fileName = (new String(data, 0, data.length)).replaceAll("\0", "");
    }
}

// This constructor makes a packet with data
class packetData extends packet {
    packetData(byte[] packet, int dataLength) {
        data = new byte[dataLength - 4];
        int index = 0;
        for (int i = 4; i < dataLength; ++i) {
            data[index++] = packet[i];
        }

        fileID = packet[1];
        packetNumber = 256 * Byte.toUnsignedInt(packet[2]) + Byte.toUnsignedInt(packet[3]);

        if ((3 & packet[0]) == 3) {
            PackageManager.tempPackets = packetNumber + 2;
            ++PackageManager.endNum;
            if (PackageManager.endNum == 3) {
                PackageManager.totalPackets = PackageManager.tempPackets;
            }
        }
    }
}

// This constructor compares the packet number to the total number of packets
class packetCompare implements Comparator<packet> {
    public int compare(packet a, packet b) {
        if (a.packetNumber > b.packetNumber) {
            return 1;
        } else if (b.getClass() == packetHeader.class) {
            return 1;
        } else if (a.packetNumber < b.packetNumber) {
            return -1;
        } else if (a.getClass() == packetHeader.class) {
            return -1;
        } else {
            return 0;
        }
    }
}
