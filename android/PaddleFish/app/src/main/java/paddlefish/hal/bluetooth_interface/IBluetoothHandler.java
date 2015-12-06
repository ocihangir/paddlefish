package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothDevice;

public interface IBluetoothHandler {
    public void discoveryHandler(BluetoothDevice device);
    public void receiveHandler(byte[] message, int len);
}
