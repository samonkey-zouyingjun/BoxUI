package com.zouyingjun.samonkey.boxui.entity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by Tony J on 2017/1/4.
 */

public class UDPSendClient {

    private int mPort = 22223;
    private String mServerIp;
    private byte[] mByte= new byte[512];
    private DatagramSocket mSocket;
    private DatagramPacket mPacket;

    public UDPSendClient(String serverIp){
        mServerIp = serverIp;
        creatClient();
    }

    public void destroyClient(){
        if(mSocket!=null){
            mSocket.close();
        }
    }

    public void sendMessage(String message){
        if(mPacket != null){
            mPacket.setData(message.getBytes(),0,message.getBytes().length);
            try {
                mSocket.send(mPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void creatClient() {
        try {
            mSocket = new DatagramSocket();
            mPacket = new DatagramPacket(mByte,mByte.length,new InetSocketAddress(mServerIp,mPort));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


}
