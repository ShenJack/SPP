package com.example.shenjack.spp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shenjack on 17-2-25.
 */

public class deviceViewAdaper extends ArrayAdapter<BluetoothDevice> {
    private int resourceId;

    public deviceViewAdaper(Context context, int resource , ArrayList<BluetoothDevice> devices) {
        super(context, resource,devices);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice bluetoothDevice = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }else {
            view = convertView;
        }
        TextView device_name = (TextView) view.findViewById(R.id.device_name);
        device_name.setText(bluetoothDevice.getName());
        return view;
    }
}
