package com.example.praba1110.wifi_student;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Attendance extends AppCompatActivity  {
    Thread thread;
    String sentence;
    boolean check=true;
    TextView timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        timer= (TextView) findViewById(R.id.textView2);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            sentence=b.getString("rno");
        }
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress group = null;
                try {
                    group = InetAddress.getByName("239.255.255.250");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                MulticastSocket s = null;
                try {
                    s = new MulticastSocket(1900);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    s.joinGroup(group);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (check) {
                    byte[] receiveData = new byte[1024];
                    byte[] sendData=new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData,
                            receiveData.length);

                    // receive the packet
                    try {
                        s.receive(receivePacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //String sentence = new String(receivePacket.getData());
                    // find details of packet origin
                    InetAddress IPAddress = receivePacket.getAddress();
                    int port = receivePacket.getPort();

                    //return a reply
                    sendData = sentence.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    try {
                        s.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        })  ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendance, menu);
        return true;
    }
    public void takeattendance(View v) throws IOException {
        check=true;
        thread.start();
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                check=false;

            }
        }.start();
        Toast.makeText(Attendance.this, "Marked present", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
