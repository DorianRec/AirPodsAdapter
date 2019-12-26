package com.example.airpodsadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FindBluetoothDevicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bluetooth_devices);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }

    public void refresh(View view) {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /*
        If not enabled, request
         */
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        /*
        Find paired bluetooth devices
        (Gekoppelte Bluetooth Geraete)
         */
        Set<BluetoothDevice> pairedBTDevicesSet = mBluetoothAdapter.getBondedDevices();
        List<String> pairedBTDevicesNameList = new ArrayList<>();

        for (BluetoothDevice bluetoothDevice : pairedBTDevicesSet) {
            pairedBTDevicesNameList.add(bluetoothDevice.getName());
        }

        String[] pairedBTDevicesNamesArray = new String[pairedBTDevicesSet.size()];

        for (int i = 0; i < pairedBTDevicesSet.size(); i++) {
            pairedBTDevicesNamesArray[i] = pairedBTDevicesNameList.get(i);
        }


        ListView bluetoothDevicesListView = findViewById(R.id.listView);


        final ArrayAdapter listAdapter = new ArrayAdapter<>(
                FindBluetoothDevicesActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                pairedBTDevicesNamesArray);

        bluetoothDevicesListView.setAdapter(listAdapter);


        bluetoothDevicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // TODO Auto-generated method stub
                        String value = (String) listAdapter.getItem(position);
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
