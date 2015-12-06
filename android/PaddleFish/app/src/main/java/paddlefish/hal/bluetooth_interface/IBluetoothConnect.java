package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothSocket;

public interface IBluetoothConnect {
    public void connectionEstablished(BluetoothSocket btSocket);
    public void connectionFailed(String cause);
}
