/**
 * Copyright Spider R&D Club, NIT Trichy
 */

import java.io.*;
import java.net.*;

/**
 * Implements logic of a simple UDP Client
 * 
 * @author arjun
 * 
 */
class UDPClient {

	/**
	 * Main Function
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		broadcast();
		listenForAttendance();
	}

	/**
	 * Broadcasts a UDP Packet to all who listen on the Multicast Group
	 */
	protected static void broadcast() {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println(e.getMessage());
		}
		InetAddress IPAddress = null;
		try {
			IPAddress = InetAddress.getByName("239.255.255.250");
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		}
		byte[] sendData = new byte[1024];
		String sentence = null;
		try {
			sentence = inFromUser.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 1901);
		try {
			clientSocket.setSoTimeout(2000);
		} catch (SocketException e) {
			System.out.println(e.getMessage());
		}
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		clientSocket.close();
	}

	protected static void listenForAttendance() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(8200);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		while (true) {
			Socket client = null;
			try {
				client = ss.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					if (line.length() == 0)
						break;
					System.out.println(line);
				}
				in.close();
				OutputStream os = client.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("HTTP/1.1 200 OK\n");
                bw.flush();
                bw.close();
				client.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}