package com.example.shenjack.spp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener{





    ListView list_new_devices;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private ArrayAdapter<String> newDevicesArrayAdapter;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_list);

        list_new_devices = (ListView) findViewById(R.id.list_new_devices);

        ListView list_paired_devices = (ListView) findViewById(R.id.list_paired_devices);
        final Button bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanForNewDevices();
                v.setVisibility(View.GONE);
            }
        });
        ListView list_new_devices = (ListView) findViewById(R.id.list_new_devices);

        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.bluetooth_item);
        newDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.bluetooth_item);
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        for (BluetoothDevice device :
                pairedDevices) {
            pairedDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());
        }

        list_paired_devices.setAdapter(pairedDevicesArrayAdapter);

        list_new_devices.setAdapter(newDevicesArrayAdapter);

        list_paired_devices.setOnItemClickListener(null);

        list_new_devices.setOnItemClickListener(null);

        IntentFilter Found_filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver,Found_filter);
        IntentFilter Finished_filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver,Finished_filter);

    }
    private void scanForNewDevices(){
        setTitle("Scanning");
        if(mBtAdapter.isDiscovering()){mBtAdapter.cancelDiscovery();}
        mBtAdapter.startDiscovery();
        findViewById(R.id.list_new_devices).setVisibility(View.VISIBLE);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setTitle("Scanning finish");
                Toast.makeText(context, "Select a device", Toast.LENGTH_LONG).show();
                if(newDevicesArrayAdapter.getCount() == 0){
                    String noDevice = getResources().getText(R.string.none_device).toString();
                    newDevicesArrayAdapter.add(noDevice);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBtAdapter!=null){
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_scan:{
                scanForNewDevices();
                list_new_devices.setVisibility(View.VISIBLE);
                break;
            }
            default:{
                break;
            }
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mBtAdapter.cancelDiscovery();

            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length()-17);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS,address);

            setResult(RESULT_OK,intent);

            finish();
        }
    };
}