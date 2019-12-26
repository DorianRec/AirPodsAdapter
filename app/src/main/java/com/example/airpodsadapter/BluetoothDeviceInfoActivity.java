package com.example.airpodsadapter;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BluetoothDeviceInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_info);

        Intent intent = getIntent();
        // Get the Intent that started this activity and extract the string
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra("BTDeviceName");
        TextView name = findViewById(R.id.name);
        TextView address = findViewById(R.id.address);
        name.setText("Name: " + bluetoothDevice.getName());
        address.setText("Address: " + bluetoothDevice.getName());
    }
}
