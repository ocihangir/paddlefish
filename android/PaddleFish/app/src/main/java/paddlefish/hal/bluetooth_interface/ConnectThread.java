package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/*
    This implementation was taken from https://developer.android.com/guide/topics/connectivity/bluetooth.html
 */

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final IBluetoothConnect mmConnectionManager;

    // Tried UUIDs only the SPP_UUID worked. See
    // https://stackoverflow.com/questions/3397071/service-discovery-failed-exception-using-bluetooth-on-android
    // private static final UUID MY_UUID = UUID.randomUUID(); Won't work
    // private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"); Won't work
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Works

    public ConnectThread(BluetoothDevice device, IBluetoothConnect btManager) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmConnectionManager = btManager;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);

            // Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            // tmp = (BluetoothSocket) m.invoke(device, 1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ConnectThread Constructor failed. error : " + e.toString());
        }
        mmSocket = tmp;
    }

    public void run() {
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            System.out.println("ConnectThread run failed. error : " + connectException.toString());
            // Unable to connect; close the socket and get out
            mmConnectionManager.connectionFailed(connectException.getMessage());
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                System.out.println("ConnectThread close failed. error : " + closeException.toString());
                mmConnectionManager.connectionFailed(closeException.getMessage());
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        // manageConnectedSocket(mmSocket);
        mmConnectionManager.connectionEstablished(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException closeException) {
            System.out.println("ConnectThread cancel failed. error : " + closeException.toString());
            mmConnectionManager.connectionFailed(closeException.getMessage());
        }
    }
}