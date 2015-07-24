/**
 * Copyright Spider R&D Club, NIT Trichy
 */

import java.io.*;
import java.net.*;

/**
 * Implements logic of a simple UDP Client
 * @author arjun
 *
 */
class UDPClient {
	public static void main(String args[]) {
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
		byte[] receiveData = new byte[1024];
		String sentence = null;
		try {
			sentence = inFromUser.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 1900);
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		try {
			clientSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("FROM SERVER:" + modifiedSentence);
		clientSocket.close();
	}
}