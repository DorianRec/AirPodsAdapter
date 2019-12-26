package com.example.airpodsadapter;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceInfoActivity extends AppCompatActivity {
    BluetoothDevice bluetoothDevice;
    TextView output;
    List<ConnectThread> threads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_info);

        Intent intent = getIntent();
        // Get the Intent that started this activity and extract the string
        bluetoothDevice = intent.getParcelableExtra("BTDeviceName");
        TextView name = findViewById(R.id.name);
        TextView address = findViewById(R.id.address);
        TextView bondState = findViewById(R.id.bondState);
        TextView bluetoothClass = findViewById(R.id.bluetoothClass);
        output = findViewById(R.id.output);

        name.setText("Name: " + bluetoothDevice.getName());
        address.setText("Address: " + bluetoothDevice.getAddress());
        switch (bluetoothDevice.getBondState()) {
            case BluetoothDevice.BOND_NONE:
                bondState.setText("BondState: " + "BOND_NONE");
                break;
            case BluetoothDevice.BOND_BONDING:
                bondState.setText("BondState: " + "BOND_BONDING");
                break;
            case BluetoothDevice.BOND_BONDED:
                bondState.setText("BondState: " + "BOND_BONDED");
                break;
            default:
                bondState.setText("BondState: ");
                break;
        }

        //type.setText("Type: " + bluetoothDevice.getType());
        bluetoothClass.setText("BluetoothClass: " + bluetoothDevice.getBluetoothClass());

        ListView bluetoothDevicesListView = findViewById(R.id.uuids);
        final ArrayAdapter uuidsAdapter = new ArrayAdapter<ParcelUuid>(
                BluetoothDeviceInfoActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                bluetoothDevice.getUuids());

        bluetoothDevicesListView.setAdapter(uuidsAdapter);
    }

    public void openBT(View view) {
        /*
        UUID: Vendor specific           (00000000-deca-fade-deca-deafdecacaff)
        UUID: Service Discovery Serve.. (00001000-0000-1000-8000-00805f9b34fb)
        UUID: Audio Sink                (0000110b-0000-1000-8000-00805f9b34fb) x
        UUID: A/V Remote Control Target (0000110c-0000-1000-8000-00805f9b34fb)
        UUID: Advanced Audio Distribu.. (0000110d-0000-1000-8000-00805f9b34fb)
        UUID: A/V Remote Control        (0000110e-0000-1000-8000-00805f9b34fb) x
        UUID: Handsfree                 (0000111e-0000-1000-8000-00805f9b34fb) x
        UUID: PnP Information           (00001200-0000-1000-8000-00805f9b34fb)
        UUID: Vendor specific           (74ec2172-0bad-4d01-8f77-997b2be0722a) x
        */

        /*
        00000000-0000-1000-8000-00805f9b34fb
         */
        //acceptThread1 = new AcceptThread(UUID.fromString("0000110b-0000-1000-8000-00805f9b34fb"));
        //acceptThread2 = new AcceptThread(UUID.fromString("0000110e-0000-1000-8000-00805f9b34fb"));
        //acceptThread3 = new AcceptThread(UUID.fromString("0000111e-0000-1000-8000-00805f9b34fb"));
        //acceptThread4 = new AcceptThread(UUID.fromString("74ec2172-0bad-4d01-8f77-997b2be0722a"));
        //acceptThread5 = new AcceptThread(UUID.fromString("00000000-0000-1000-8000-00805f9b34fb"));
        for (ParcelUuid uuid : bluetoothDevice.getUuids()) {
            threads.add(new ConnectThread(bluetoothDevice, uuid.getUuid()));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void closeBT(View view) {
        for (ConnectThread thread : threads) {
            thread.cancel();
        }
        System.out.println("Finished all threads");
    }
}
