package src.paddlefish;

import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;


import src.paddlefish.ArrayAdapters.SensorListAdapter;
import src.paddlefish.Models.SensorItem;


public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private SensorListAdapter listAdapter;
    private FloatingActionButton fab;

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


}
