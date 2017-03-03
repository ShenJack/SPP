package com.example.shenjack.spp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.BreakIterator;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {


    private ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_item);

    private ArrayAdapter<String> newDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_item);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_list);



        ListView list_paired_devices = (ListView) findViewById(R.id.list_paired_devices);

        ListView list_new_devices = (ListView) findViewById(R.id.list_new_devices);

        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        for (BluetoothDevice device :
                pairedDevices) {
            pairedDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());
        }

        list_paired_devices.setAdapter(pairedDevicesArrayAdapter);

        list_new_devices.setAdapter(newDevicesArrayAdapter);

        list_paired_devices.setOnItemClickListener(null);

        list_new_devices.setOnItemClickListener(null);

    }
    private void scanForNewDevices(){
        setTitle("Scanning");
        BluetoothAdapter.getDefaultAdapter().startDiscovery();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setTitle("select_device");
                if(newDevicesArrayAdapter.getCount() == 0){
                    String noDevice = getResources().getText(R.string.none_device).toString();
                    newDevicesArrayAdapter.add(noDevice);
                }
            }
        }
    };
}
