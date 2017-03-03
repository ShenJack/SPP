package com.example.shenjack.spp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

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

    public BluetoothChatService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mNewState = mState;
        mHandler = handler;
    }


    private synchronized void connect(BluetoothDevice device,boolean secure){
        if(mState == STATE_CONNECTTING){
            if(mConnectThread != null){
                mConnectThread.
            }
        }
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
    }
}
