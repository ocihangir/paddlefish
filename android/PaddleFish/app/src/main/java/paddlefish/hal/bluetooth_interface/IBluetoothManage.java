package paddlefish.hal.bluetooth_interface;

public interface IBluetoothManage {
    public void receiveData(byte[] data, int len);
    public void communicationFailure(String cause);
}
