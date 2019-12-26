package com.example.airpodsadapter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    //UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
    UUID MY_UUID;
    private String TAG = "TAG";
    private String NAME = "NAME";
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public AcceptThread(UUID uuid) {
        MY_UUID = uuid;
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        System.out.println("DEBUG: " + "ran");
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
                System.out.println("DEBUG: " + "socket accepted");
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the connect socket", e);
                }
                break;
            } else {
                System.out.println("DEBUG: socket null");
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
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