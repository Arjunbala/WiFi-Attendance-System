package com.example.praba1110.wifi_teacher;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by praba1110 on 14/9/15.
 */
public class discovery {
    String[] devices=new String[100]; //list of roll numbers
    int number = 0; //number of responses
    int discovery_on = 1;
    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
                    System.in));
            DatagramSocket clientSocket = null;
            try {
                clientSocket = new DatagramSocket();
            } catch (SocketException e) {
                Log.d("ERROR",e.getMessage());
            }
            InetAddress IPAddress = null;
            try {
                IPAddress = InetAddress.getByName("239.255.255.250");
            } catch (UnknownHostException e) {
                Log.d("ERROR",e.getMessage());
            }
            byte[] sendData = new byte[1024];
            String sentence = null;

            sentence = "Test";

            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, IPAddress, 1900);
            try {
                if (clientSocket != null) {
                    clientSocket.send(sendPacket);
                }
            } catch (IOException e) {
                Log.d("ERROR",e.getMessage());
            }
            while(discovery_on==1) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    if (clientSocket != null) {
                        clientSocket.receive(receivePacket);
                    }
                } catch (IOException e) {
                    Log.d("ERROR",e.getMessage());
                }
                String modifiedSentence = new String(receivePacket.getData());
                devices[number++]=modifiedSentence;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("ERROR",e.getMessage());
                }
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    });
    public void start_discovery() {
       thread.start();
    }
    public void end_discovery() {
        discovery_on=0;

    }
    public String[] getDevices() {
        return devices;
    }
}