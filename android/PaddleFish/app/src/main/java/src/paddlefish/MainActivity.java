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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import paddlefish.def.SensorCategory;
import paddlefish.run.State;
import paddlefish.sensor.GenSensor;
import src.paddlefish.ArrayAdapters.SensorListAdapter;
import src.paddlefish.Models.GenSensorItem;
import src.paddlefish.Models.SensorItem;
import src.paddlefish.Views.ChooseSensorCategoryDialog;
import src.paddlefish.Views.SensorSettingsActivity;


public class MainActivity extends AppCompatActivity {
    private SensorCategory category;
    private SensorItem item;
    private ListView listView;
    public static SensorListAdapter listAdapter;
    private FloatingActionButton fab;
    private ChooseSensorCategoryDialog dialog;

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
        ArrayList<GenSensorItem> list = new ArrayList<GenSensorItem>();

        listAdapter = new SensorListAdapter(this, R.layout.listitem_sensor, list);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SensorSettingsActivity.class);
                intent.putExtra("object_position",position);
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
            GenSensor sensor = State.getInstance().addDevice(sensorItem.sensorName);
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

}
