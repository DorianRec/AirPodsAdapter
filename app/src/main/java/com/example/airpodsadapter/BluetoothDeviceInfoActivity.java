package com.example.airpodsadapter;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BluetoothDeviceInfoActivity extends AppCompatActivity {
    BluetoothDevice bluetoothDevice;
    TextView leftPlaying;
    TextView rightPlaying;
    TextView leftConnected;
    TextView rightConnected;
    TextView caseConnected;

    /**
     * Create a BroadcastReceiver for ACTION_FOUND.
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (!device.equals(bluetoothDevice)) {
                return;
            }
            if (BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED.equals(action)) {
                String bla = "";
                switch (intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0)) {
                    case BluetoothA2dp.STATE_PLAYING:
                        bla = "STATE_PLAYING";
                        break;
                    case BluetoothA2dp.STATE_NOT_PLAYING:
                        bla = "STATE_NOT_PLAYING";
                        break;
                }
                leftPlaying.setText("Left playing: " + bla);
                rightPlaying.setText("Right playing: " + bla);
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                // TODO
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                // TODO
            } else if (BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                // TODO
            } else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                // TODO
            } else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                String bla = "";
                switch (intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0)) {
                    case BluetoothHeadset.STATE_DISCONNECTED:
                        bla = "STATE_DISCONNECTED";
                        break;
                    case BluetoothHeadset.STATE_CONNECTING:
                        bla = "STATE_CONNECTING";
                        break;
                    case BluetoothHeadset.STATE_CONNECTED:
                        bla = "STATE_CONNECTED";
                        break;
                    case BluetoothHeadset.STATE_DISCONNECTING:
                        bla = "STATE_DISCONNECTING";
                        break;
                }

                leftConnected.setText("Left " + bla);
                rightConnected.setText("Right " + bla);
                caseConnected.setText("case " + bla);

            } else {
                System.out.println(intent);
                System.out.println(intent.getExtras());
            }

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_info);

        leftPlaying = findViewById(R.id.left_playing);
        rightPlaying = findViewById(R.id.right_playing);
        leftConnected = findViewById(R.id.left_connected);
        rightConnected = findViewById(R.id.right_connected);
        caseConnected = findViewById(R.id.case_connected);

        Intent intent = getIntent();
        // Get the Intent that started this activity and extract the string
        bluetoothDevice = intent.getParcelableExtra("BTDeviceName");
        TextView name = findViewById(R.id.name);
        TextView address = findViewById(R.id.address);
        TextView bondState = findViewById(R.id.bondState);
        TextView bluetoothClass = findViewById(R.id.bluetoothClass);

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
        bluetoothClass.setText("BluetoothClass: " + bluetoothDevice.getBluetoothClass());

        // Register for broadcasts when a device is discovered.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.ACL_CONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECTED");
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.device.action.NAME_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT");
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED");
        registerReceiver(receiver, intentFilter);

    }

        /*
        0000110e-0000-1000-8000-00805f9b34fb
        0000110b-0000-1000-8000-00805f9b34fb
        0000110e-0000-1000-8000-00805f9b34fb
        00000000-0000-1000-8000-00805f9b34fb
        00000000-0000-1000-8000-00805f9b34fb
        74ec2172-0bad-4d01-8f77-997b2be0722a
        */
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
}
