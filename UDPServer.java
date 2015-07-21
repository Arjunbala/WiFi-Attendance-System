/**
 * Copyright Spider R&D Club, NIT Trichy
 */

import java.io.*;
import java.net.*;

/**
 * Implements logic of a UDP Server that listens for Multicasts
 * 
 * @author arjun
 * 
 */
class UDPServer {
	public static void main(String args[]) throws Exception {
		// SSDP listens on a IP and port as below
		InetAddress group = InetAddress.getByName("239.255.255.250");
		MulticastSocket s = new MulticastSocket(1900);
		s.joinGroup(group);
		byte[] receiveData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);

			// receive the packet
			s.receive(receivePacket);
			String sentence = new String(receivePacket.getData());

			// print the contents
			System.out.println("RECEIVED: " + sentence);

			// find details of packet origin
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			System.out.println("IPADDRESS: " + IPAddress.toString()
					+ "\nPORT: " + port + "\n");
		}
	}
}