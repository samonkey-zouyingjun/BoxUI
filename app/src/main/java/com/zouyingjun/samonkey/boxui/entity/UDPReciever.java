package com.zouyingjun.samonkey.boxui.entity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Tony J on 2017/1/4.
 */

public class UDPReciever {

    private DatagramPacket mDatagramPacket;
    private DatagramSocket mDatagramSocket;
    private final int mPort = 22223;
    private byte[] mByte = new byte[512];
    private ServerCallBack mCallBack;

    public UDPReciever(ServerCallBack mCallBack){
        try {
            mDatagramSocket = new DatagramSocket(mPort);
            mDatagramPacket = new DatagramPacket(mByte,mByte.length);
            this.mCallBack = mCallBack;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private boolean Flag = true;
    public void setFlag(boolean flag){
        Flag = flag;
        if(mDatagramSocket!=null)
        mDatagramSocket.close();
    }
    public void recieverBroadcast(){
        while(Flag) {
            try {
                mDatagramSocket.receive(mDatagramPacket);
                String reciver = new String(mDatagramPacket.getData(), 0, mDatagramPacket.getLength());
                Thread.sleep(20);
                if(reciver!=null)
                mCallBack.callBack(reciver);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

public interface ServerCallBack {
    void callBack(String data);
}
}
