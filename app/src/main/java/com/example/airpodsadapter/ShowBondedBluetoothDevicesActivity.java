package com.example.airpodsadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShowBondedBluetoothDevicesActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bonded_bluetooth_devices);

        refresh(null);
    }

    public void refresh(View view) {

        /*
        If not enabled, request
         */
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        /*
        Find bonded (paired) bluetooth devices
        (Gekoppelte Bluetooth Geraete)
         */
        Set<BluetoothDevice> pairedBTDevicesSet = bluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> pairedBTDevicesNameList = new ArrayList<>();

        for (BluetoothDevice bluetoothDevice : pairedBTDevicesSet) {
            pairedBTDevicesNameList.add(bluetoothDevice);
        }

        String[] pairedBTDevicesNamesArray = new String[pairedBTDevicesSet.size()];

        for (int i = 0; i < pairedBTDevicesSet.size(); i++) {
            pairedBTDevicesNamesArray[i] = pairedBTDevicesNameList.get(i).getName();
        }


        ListView bluetoothDevicesListView = findViewById(R.id.listView);


        final ArrayAdapter listViewAdapter = new ArrayAdapter<BluetoothDevice>(
                ShowBondedBluetoothDevicesActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                pairedBTDevicesNameList);

        bluetoothDevicesListView.setAdapter(listViewAdapter);

        /*
        Event for clicking on items
         */
        bluetoothDevicesListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) listViewAdapter.getItem(position);
                        String value = bluetoothDevice.getName();
                        //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ShowBondedBluetoothDevicesActivity.this,
                                BluetoothDeviceInfoActivity.class);

                        //BluetoothDeviceParcelable bluetoothDeviceParcelable =
                        //        new BluetoothDeviceParcelable(listAdapter.getItem(position));

                        //intent.putExtra("pairedBTDevice", BluetoothDeviceParcelable.CREATOR());
                        intent.putExtra("BTDeviceName", bluetoothDevice);
                        startActivity(intent);
                    }
                });
    }
}
