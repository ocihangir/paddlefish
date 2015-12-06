package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/*
    This implementation was taken from https://developer.android.com/guide/topics/connectivity/bluetooth.html
 */

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private final IBluetoothManage mmConnection;

    public ConnectedThread(BluetoothSocket socket, IBluetoothManage btConnection) {
        mmSocket = socket;
        mmConnection = btConnection;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("ConnectedThread Constructor failed. error : " + e.toString());
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                //        .sendToTarget();
                mmConnection.receiveData(buffer, bytes);
            } catch (IOException e) {
                System.out.println("ConnectedThread Run failed. error : " + e.toString());
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            System.out.println("ConnectedThread Write failed. error : " + e.toString());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            System.out.println("ConnectedThread Close failed. error : " + e.toString());
        }
    }
}