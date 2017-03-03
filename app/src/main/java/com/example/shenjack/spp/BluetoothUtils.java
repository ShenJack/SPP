package com.example.shenjack.spp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.Set;

/**
 * Created by shenjack on 17-2-25.
 */

public class BluetoothUtils{


    public static void turnOnBluetooth(Context context){
        BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
        Intent turnOnBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        context.startActivity(turnOnBT);
    }

    public static Set<BluetoothDevice> getBondedDevices(Context context){
        BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = BA.getBondedDevices();
        return bondedDevices;
    }

    public  static void startClientBtn(Context con)
    {
        Intent serverIntent = new Intent(con,ServerActivity.class);
        serverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
