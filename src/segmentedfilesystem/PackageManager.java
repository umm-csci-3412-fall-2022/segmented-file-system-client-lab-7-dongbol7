package segmentedfilesystem;

import java.util.*;
import java.net.*;

// The packet manager should be able to take a packet and add it to the list of packets
public class PackageManager {
    static int totalPackets = 256;
    ArrayList<packet> packets = new ArrayList<packet>();

    // This constructor defines packet manager
    class packet {
        int packetNumber = 0;
        int fileID;
        byte[] data;
        String fileName;
    }

    // This constructor creates a packet object and adds it to the list of packets
    class packetHeader extends packet {

        packetHeader(DatagramPacket packet) {
            byte[] orgData = packet.getData();
            data = new byte[packet.getLength() - 2];
            int index = 0;
            for (int i = 2; i < packet.getLength(); ++i) {
                data[index++] = orgData[i];
            }
            fileID = orgData[1];
    
            fileName = new String(data, 0, data.length);
            System.out.println(fileName);
        }
    }

    // This constructor makes a packet with data
    class packetData extends packet {

        packetData(DatagramPacket packet) {
            byte[] dataOrg = packet.getData();
            data = new byte[packet.getLength() - 4];
            int index = 0;
            for (int i = 4; i < packet.getLength(); ++i) {
                data[index++] = dataOrg[i];
            }
            fileID = dataOrg[1];
            packetNumber = dataOrg[2] * 256 + dataOrg[3];
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


    // This method should take a packet and add it to the list of packets
    public void packetIns(DatagramPacket packet) {
        if ((1 & packet.getData()[0]) == 1) {
            packets.add(new packetHeader(packet));
        } else {
            packets.add(new packetData(packet));
        }
    }
    
    // This method should take the list of packets and write them to the appropriate files
    ArrayList<ArrayList<packet>> packetOrg = new ArrayList<ArrayList<packet>>();
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

        for (ArrayList<packet> arrayList : packetOrg) {
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.println(arrayList.get(i).packetNumber);
            }
        }
    }
}