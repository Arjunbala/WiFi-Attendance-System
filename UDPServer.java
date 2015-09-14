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
		MulticastSocket s = new MulticastSocket(1901);
		s.joinGroup(group);
		while (true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);

			// receive the packet
			s.receive(receivePacket);
			String sentence = new String(receivePacket.getData());

			// find details of packet origin
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();

			// Construct the URL
			String urlToConnect = "http://" + IPAddress.getHostAddress()
					+ ":8200/attendance.txt";
			System.out.println("Connecting to " + urlToConnect);

			URL urlObj = new URL(urlToConnect);
			HttpURLConnection connection = (HttpURLConnection) urlObj
					.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		}
	}
}
