package paddlefish.hal.bluetooth_interface;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Bluetooth class is a template to facilitate Bluetooth connection.
 * It basically helps creating connections with devices by providing some securities.
 *
 * When init() is called;
 * It checks if there is a bluetooth device.
 * It checks if it is enabled, if not, it shows BT enable entity to the user.
 * When discoverDevices() is called;
 * It discovers devices and provides interface to handle them.
 */
public class Bluetooth extends BroadcastReceiver implements IBluetoothConnect, IBluetoothManage {

    private BluetoothAdapter mBlueToothAdapter = null; // Bluetooth adapter
    private BroadcastReceiver mReceiver = null; // BroadcastReceiver to handle state changes and device discovery.
    private final static int REQUEST_ENABLE_BT = 1;

    private static Bluetooth minstance = null;

    private static Context mainActivity = null;

    private ConnectThread connectToDevice;
    private ConnectedThread connectedDevice;

    private static ArrayList<BluetoothDevice> discoveredDevices;

    private boolean broadcastRegistered = false;
    private boolean stateChangeRegistered = false;

    private static Message mHandler;

    private static ArrayList<IBluetoothHandler> bluetoothHandlers = new ArrayList<IBluetoothHandler>();

    ConnectionState connectionState = ConnectionState.NOT_CONNECTED;

    /*
        Constructor of the class.

        We need Main Activity context to call bluetooth enable entity.
     */
    protected Bluetooth(){

    }

    public static Bluetooth getInstance()
    {
        if ( minstance == null ) {
            minstance = new Bluetooth();
            discoveredDevices = new ArrayList<BluetoothDevice>();
            mHandler = new Message();

        }

        return minstance;
    }

    public void addBluetoothHandler (IBluetoothHandler btHandler)
    {
        bluetoothHandlers.add(btHandler);
    }

    /*
        Connect to specified bluetooth device.
     */
    public void connect(BluetoothDevice device)
    {
        // Cancel discovery because it will slow down the connection
        mBlueToothAdapter.cancelDiscovery();
        connectToDevice = new ConnectThread(device,this);
        connectToDevice.start();
        connectionState = ConnectionState.CONNECTING;
    }

    public void disconnect()
    {
        if (connectedDevice != null)
            connectedDevice.cancel();

        if (connectToDevice != null)
            connectToDevice.cancel();

        connectionState = ConnectionState.NOT_CONNECTED;
    }

    /*
        close() must be called by the user class. It destroys registered
        receivers. If it is not called, it may cause memory leaks because of the registered
        callback services.
     */
    public void close()
    {
        mBlueToothAdapter.cancelDiscovery();

        if (stateChangeRegistered) {
            mainActivity.unregisterReceiver(mReceiver);
            stateChangeRegistered = false;
        }

        if (broadcastRegistered) {
            mainActivity.unregisterReceiver(this);
            broadcastRegistered = false;
        }

        disconnect();

    }

    /*
        Bluetooth init function uses default bluetooth adapter.
        If there is no bluetooth devices in the system, it will show an error and
        exits program.

        If there is a bluetooth adapter, it checks if it is enabled. If not, it will
        call Bluetooth enable intent.

        Init function also sets broadcast call back for state changes. It is handled by
        BroadcastRx class which extends BroadcastReceiver.

     */
    public void init(Context mainActivity)
    {
        this.mainActivity = mainActivity;

        // TODO: if version is JELLY_BEAN_MR2 or higher use getSystemService(class)
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueToothAdapter == null)
        {
            // Warn user and kill the app
            Toast.makeText(mainActivity.getApplicationContext(), "This device doesn't support Bluetooth!", Toast.LENGTH_LONG).show();
            // TODO : throw an exception instead
            System.exit(0);
        }

        if (!mBlueToothAdapter.isEnabled()) {
            Toast.makeText(mainActivity.getApplicationContext(), "Please enable Bluetooth.", Toast.LENGTH_SHORT).show();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivity(enableBtIntent);
        }

        mReceiver = new BroadcastRx(mainActivity);
        // Register state change
        IntentFilter stateChangeIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mainActivity.registerReceiver(mReceiver, stateChangeIntentFilter);
        stateChangeRegistered = true;

        // Register this class as the BroadcastReceiver callback handler.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(this, filter);
        broadcastRegistered = true;
    }

    /*
        Get a list of already paired devices.
     */
    public ArrayList<BluetoothDevice> getPairedDeviceList()
    {
        ArrayList<BluetoothDevice> res = new ArrayList<BluetoothDevice>(mBlueToothAdapter.getBondedDevices());
        return res;
    }

    /*
        Paired device name list as a String array list
     */
    ArrayList<String> getPairedDeviceNameList()
    {
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<BluetoothDevice> pairedDevices = getPairedDeviceList();

        if (pairedDevices != null)
            for (BluetoothDevice device : pairedDevices)
                nameList.add(device.getName());

        return nameList;
    }

    /*
        Start discovering devices.

        The callback is handled by BroadcastRx class which extends BroadcastReceiver.
     */
    public void discoverDevices()
    {
        // Clear old discovered device list
        if (discoveredDevices != null)
            discoveredDevices.clear();
        // START DISCOVERING NEW DEVICES
        // Start discovery
        if (mBlueToothAdapter != null)
            mBlueToothAdapter.startDiscovery();
    }

    /*
        This function adds discovered device to the list and notify.
     */
    protected void addDiscoveredList(BluetoothDevice device)
    {
        discoveredDevices.add(device);
        for (IBluetoothHandler btHandler : bluetoothHandlers)
            btHandler.discoveryHandler(device);
    }

    /*
        Get discovered device list
     */
    public ArrayList<BluetoothDevice> getDiscoveredDeviceList() { return discoveredDevices; }

    /*
        This is a callback function that is called when device discovery finds a new device.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            addDiscoveredList(device);
            // Toast.makeText(context, "Discovered device : " + device.getName() + "--" + device.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void connectionEstablished(BluetoothSocket btSocket) {
        System.out.println("Connection established!");
        connectedDevice = new ConnectedThread(btSocket,this);
        connectedDevice.start();
        connectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void connectionFailed(String cause) {
        System.out.println("Connection failed! Cause : " + cause);
        connectionState = ConnectionState.FAILED;
    }

    @Override
    public void receiveData(byte[] data, int len) {
        for (IBluetoothHandler btHandler : bluetoothHandlers)
            btHandler.receiveHandler(data,len);
    }

    @Override
    public void communicationFailure(String cause) {
        connectionState = ConnectionState.FAILED;
    }

    public void sendData(byte[] data)
    {
        connectedDevice.write(data);
    }
}
