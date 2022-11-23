package segmentedfilesystem;

import java.net.*;

public class FileRetriever {

	public FileRetriever(String server, int port) {
        // Save the server and port for use in `downloadFiles()`
        //...
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

	}

        public static void sendUDP(String host, int port, byte[] message) {
        // This method should send a UDP packet to the given
        // host and port, containing the given message.

                try {
                        // Create a new DatagramSocket, which will be used to send the data.
                        DatagramSocket socket = new DatagramSocket();
                        InetAddress address = InetAddress.getByName(host);
                        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
                        
                        // Send the packet
                        socket.send(packet);
                        socket.close();
                        
                } catch (Exception e) {
                        System.err.println(e);
                }
        }

        public static void receiveUDP(int port) {
        // This method should receive a UDP packet from the given
        // port, and return the data from that packet.

                try {
                        // Create a new DatagramSocket, which will be used to receive the data.
                        DatagramSocket socket = new DatagramSocket(port);
                        byte[] buffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        
                        while (true) {
                                // Receive the packet
                                socket.receive(packet);
                                byte[] data = packet.getData();
                                socket.close();
                                packet = new DatagramPacket(buffer, buffer.length);
                        }
                        
                } catch (Exception e) {
                        System.err.println(e);
                }
        }



}
