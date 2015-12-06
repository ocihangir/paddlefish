package src.paddlefish;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import java.util.jar.Manifest;

import javax.xml.parsers.ParserConfigurationException;

import paddlefish.def.SensorCategory;
import paddlefish.hal.bluetooth_interface.Bluetooth;
import paddlefish.protocol.CommController;
import paddlefish.run.State;
import paddlefish.sensor.GenSensor;
import src.paddlefish.ArrayAdapters.SensorListAdapter;
import src.paddlefish.Models.GenSensorItem;
import src.paddlefish.Models.SensorItem;
import src.paddlefish.Views.ChooseSensorCategoryDialog;
import src.paddlefish.Views.SensorSettingsActivity;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity
{
    private SensorCategory category;
    private SensorItem item;
    private ListView listView;
    public static SensorListAdapter listAdapter;
    private FloatingActionButton fab;
    private ChooseSensorCategoryDialog dialog;

    Bluetooth btDevice = null;

    private static boolean bluetooth_disable = true;

    // State holder
    public static State curSt;

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // BGozde
        AssetManager assetManager = getResources().getAssets();
        try {
            curSt = new State(assetManager);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // EGozde
        if (!bluetooth_disable) {
        btDevice = Bluetooth.getInstance();

        btDevice.init(this);

        CommController commController = null;

        try {
            commController = CommController.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<BluetoothDevice> pairedDevices = btDevice.getPairedDeviceList();

        btDevice.connect(pairedDevices.get(0));
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);

        listView = (ListView) findViewById(R.id.main_listView);
        ArrayList<GenSensorItem> list = new ArrayList<GenSensorItem>();

        listAdapter = new SensorListAdapter(this, R.layout.listitem_sensor, list);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SensorSettingsActivity.class);
                intent.putExtra("object_position", position);
                startActivity(intent);
            }
        });


        //Add an item to list dynamically ----> Open settings dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ChooseSensorCategoryDialog(MainActivity.this, R.style.MyDialog);
                dialog.show();
            }
        });
    }


    public void dissmissDialogAndAddItem(SensorItem sensorItem) {
        dialog.dismiss();
        try {
            GenSensor sensor = curSt.addDevice(sensorItem.sensorName);
            GenSensorItem item = new GenSensorItem();
            item.sensorItem = sensorItem;
            item.genSensor = sensor;
            listAdapter.add(item);
            listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public static State getState(){
        if(state == null){
            state = new State();
        }
        return state;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btDevice.close();
    }

    /*
            onActivityResult handler is used to track Bluetooth turn on status.
            If the user answers no to enable activity, we will handle it here
            and show user a message, perhaps exit the application.
         */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if ( requestCode == REQUEST_ENABLE_BT ) {
            if (resultCode != RESULT_OK) {
                // Warn user or kill the app
                Toast.makeText(getApplicationContext(), "You must enable Bluetooth to use this application.", Toast.LENGTH_LONG).show();
                // System.exit(0);
            }
        }
    }

}
