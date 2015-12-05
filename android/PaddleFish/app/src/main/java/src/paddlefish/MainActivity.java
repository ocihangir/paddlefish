package src.paddlefish;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


import src.paddlefish.ArrayAdapters.SensorListAdapter;
import src.paddlefish.Models.SensorItem;


public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private SensorListAdapter listAdapter;
    private FloatingActionButton fab;

    BluetoothAdapter mBlueToothAdapter = null;

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_STATE_CHANGE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        listView = (ListView) findViewById(R.id.main_listView);
        ArrayList<SensorItem> list = new ArrayList<SensorItem>();
        for (int i = 0; i < 30; i++) {
            SensorItem item = new SensorItem();
            item.name = "erdem " + i;
            list.add(item);
        }
        listAdapter = new SensorListAdapter(this, R.layout.listitem_sensor, list);
        listView.setAdapter(listAdapter);

        //Add an item to list dynamically ----> Open settings dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAdapter.add(listAdapter.getItem(5));
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void initBluetooth()
    {
        // TODO: if version is JELLY_BEAN_MR2 or higher use getSystemService(class)
        mBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueToothAdapter == null)
        {
            // TODO: device doesn't support bluetooth
            // Warn user and kill the app
        }

        if (!mBlueToothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        /*
        // TODO: test this intentfilter
        // START DISCOVERING NEW DEVICES
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mStateChangeReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //TODO: catch when BT state changed
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Register
        IntentFilter stateChangeIntentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mStateChangeReceiver, stateChangeIntentFilter);
        */

        boolean paddleFishHWFound = false;

        Set<BluetoothDevice> pairedDevices = mBlueToothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        /*
        // TODO: test this functionality
        if ( !paddleFishHWFound )
        {
            // START DISCOVERING NEW DEVICES
            // Create a BroadcastReceiver for ACTION_FOUND
            final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    // When discovery finds a device
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Get the BluetoothDevice object from the Intent
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        // Add the name and address to an array adapter to show in a ListView
                        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            };
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // TODO: Don't forget to unregister during onDestroy
        }*/



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if ( requestCode == REQUEST_ENABLE_BT ) {
            if (resultCode == RESULT_OK) {

            } else {
                // TODO: user didn't enable the BT
                // Warn user and kill the app
            }
        } else { /* unknown activity request code */ }
    }


}
