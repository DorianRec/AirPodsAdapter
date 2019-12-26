package com.example.airpodsadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private String TAG = "TAG";
    private UUID MY_UUID;

    public ConnectThread(BluetoothDevice device, UUID uuid) {
        this.MY_UUID = uuid;
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        System.out.println("ran");
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            Log.e(TAG, "Unable to connect; close the socket and return.", connectException);
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        System.out.println("DEBUG: handling socket!");
        InputStream mmInputStream = null;
        try {
            mmInputStream = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Could not get input stream", e);
        }
        int readBufferPosition = 0;
        byte[] readBuffer = new byte[1024];
        final byte delimiter = 10; //This is the ASCII code for a newline character
        final Handler handler = new Handler();

        int bytesAvailable = 0;
        try {
            bytesAvailable = mmInputStream.available();
        } catch (IOException e) {
            Log.e(TAG, "Input stream available check went wrong.", e);
        }
        if (bytesAvailable > 0) {
            byte[] packetBytes = new byte[bytesAvailable];
            try {
                mmInputStream.read(packetBytes);
            } catch (IOException e) {
                Log.e(TAG, "Could not read from input stream", e);
            }
            for (int i = 0; i < bytesAvailable; i++) {
                byte b = packetBytes[i];
                if (b == delimiter) {
                    byte[] encodedBytes = new byte[readBufferPosition];
                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                    final String data;
                    data = new String(encodedBytes, StandardCharsets.US_ASCII);

                    readBufferPosition = 0;

                    handler.post(new Runnable() {
                        public void run() {
                            System.out.println("System.out.println: " + MY_UUID + ": " + data);
                        }
                    });
                } else {
                    readBuffer[readBufferPosition++] = b;
                }
            }
        }

    }
}