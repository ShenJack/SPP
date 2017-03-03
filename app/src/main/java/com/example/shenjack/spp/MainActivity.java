package com.example.shenjack.spp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private TextView textView;
    private Set<BluetoothDevice> bondedDevices;
    private ArrayList<BluetoothDevice> names = new ArrayList<BluetoothDevice>();
    private ListView deviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BluetoothReceiver,filter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        Switch swicthButton = (Switch) findViewById(R.id.switch1);
        deviceList = (ListView) findViewById(R.id.device_list);
//        toggleButton.setOnCheckedChangeListener(this);
//        swicthButton.setOnCheckedChangeListener(this);
        initialize();
        for (BluetoothDevice device:bondedDevices
             ){
            names.add(device);
        }

        deviceList.setAdapter(new deviceViewAdaper(this,R.layout.bluetooth_item,names));

    }

    private BroadcastReceiver BluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_NAME);

            }
        }
    };
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.toggleButton:{
                if(isChecked){
                    textView.setText("Checked");
                }else{
                    textView.setText("Unchecked");
                }
                break;
            }
            case R.id.switch1:{
                if(isChecked){
                    textView.setText("Switch ON");
                }else{
                    textView.setText("Switch OFF");
                }
                break;
            }

            default:
                Toast.makeText(this, "Unknown button", Toast.LENGTH_SHORT).show();

        }
    }


    private void initialize(){
        BluetoothUtils.turnOnBluetooth(this);
        bondedDevices = BluetoothUtils.getBondedDevices(this);
        Intent scan = new Intent(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        sendBroadcast(scan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_scan:{
                 Intent serverIntent = new Intent(this,DeviceListActivity.class);
                startActivityForResult(serverIntent,0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            connectDevice(data,true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(BluetoothReceiver);
        super.onDestroy();
    }
}
