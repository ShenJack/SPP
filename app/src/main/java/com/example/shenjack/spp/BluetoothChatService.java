package com.example.shenjack.spp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Message;
import android.provider.SyncStateContract;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by shenjack on 17-3-4.
 */


public class BluetoothChatService {

    private ConnectThread mConnectThread;
    private BluetoothAdapter mAdapter;
    private Handler mHandler;
    private int mState;
    private int mNewState;

    private static final UUID MY_UUID_SECURE =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private static final int STATE_NONE = 0;
    private static final int STATE_LISTEN = 1;
    private static final int STATE_CONNECTTING = 2;
    private static final int STATE_CONNECTED = 3;
    private int state;

    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
    }


    private synchronized void updateUserInterfaceTitle() {
        mState = getState();
        mNewState = mState;

        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    public synchronized int getState() {
        return state;
    }

    public synchronized void start(){
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if(mConnectedThread != null){
            mC
        }
    }

    private void connectionLost(){
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST,"Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;

        updateUserInterfaceTitle();

        BluetoothChatService.this.start();
    }

    private synchronized void connect(BluetoothDevice device,boolean secure){
        if(mState == STATE_CONNECTTING){
            if(mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }


        if(mConnectThread!=null){
            mConnectThread.cancel();
            mConnectThread=null;
        }

        mConnectThread = new ConnectThread(device,secure);
        mConnectThread.start();
        updateUserInterfaceTitle();
    }

    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device , boolean secure) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure? "Secure":"Insecure";

            try{
                if(secure){
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }else {
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmSocket = tmp;
            mState = STATE_CONNECTTING;

        }



        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket mmSocket,String socketType) {
            this.mmSocket = mmSocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try{
                tmpIn = mmSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

            mState = STATE_CONNECTED;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (mState == STATE_CONNECTED){
                try{
                    bytes = mmInStream.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }

        public void write(byte[] buffer){
            try{
                mmOutStream.write(buffer);


                mHandler.obtainMessage(Constants.MESSAGE_WRITE,-1,-1,buffer)
                        .sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
