package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by vela on 10/9/15.
 */
public class BroadcastRx extends BroadcastReceiver {

    protected Context context = null;

    private final static int REQUEST_ENABLE_BT = 1;

    public BroadcastRx(Context mainActivity)
    {
        this.context = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (BluetoothAdapter.STATE_OFF == intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, 0))
            {
                Toast.makeText(context, "Please enable Bluetooth.", Toast.LENGTH_SHORT).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enableBtIntent);
            }
            //Toast.makeText(getApplicationContext(), "Connection state : " + intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0), Toast.LENGTH_SHORT).show();
        }
    }
}
