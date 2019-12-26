package com.example.airpodsadapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Send button
     */
    public void findBT(View view) {
        Intent intent = new Intent(this, ShowBondedBluetoothDevicesActivity.class);
        startActivity(intent);
    }

    public void gotoShowBluetoothDevices(View view) {
        Intent intent = new Intent(this, ShowBluetoothDevicesActivity.class);
        startActivity(intent);
    }
}
