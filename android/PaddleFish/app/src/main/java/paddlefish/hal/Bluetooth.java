package paddlefish.hal;

import android.bluetooth.*;

import java.util.ArrayList;
import java.util.List;

public class Bluetooth {

    private List<CommRxInterface> receiverList = new ArrayList<CommRxInterface>();
    BluetoothAdapter bluetooth = null;

    public Bluetooth()
    {

    }

    public void addReceiver(CommRxInterface commRx)
    {
        receiverList.add(commRx);
    }

    public void connect(BluetoothAdapter bluetoothDefAdapter)
    {
        bluetooth = bluetoothDefAdapter;
    }

}
