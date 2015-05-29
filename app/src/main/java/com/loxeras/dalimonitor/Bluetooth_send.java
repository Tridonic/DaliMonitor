package com.loxeras.dalimonitor;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

//File: 	send_command.java
//design: 	--
//date:		4.11.14
//(c) 2014 by Dario Duff
//Ist f?r das Senden von commands via Bluetooth an das verbundene Bluetooth ger?t zust$ndig.
////////////////////////////////////////////////////////////////////////////////

public class Bluetooth_send extends Thread {
    private BluetoothSocket btSocket;
    private OutputStream outStream = null;


    public void send(String msg){
        //sends a message vie Bluetooth
        BluetoothThread BluetoothThread;
        BluetoothThread = connect.get_thread();	//get bluetooth thread

        //254 221
        btSocket = BluetoothThread.get_connection();	//Get bluetooth connection
        byte[] msgBufferTermination = {0xA,0xD};		//Msg termination /n /r
        byte[] msgBuffer = msg.getBytes();  			//Translate message to byte

        try {
            outStream = btSocket.getOutputStream();		//get stream
            outStream.write(msgBuffer);					//send to device
            outStream.write(msgBufferTermination); 		//send termination to device
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
