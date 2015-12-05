package src.paddlefish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import paddlefish.def.ControlInput;
import paddlefish.def.ControlType;
import paddlefish.def.SensorControl;
import paddlefish.sensor.GenSensor;
import src.paddlefish.MainActivity;
import src.paddlefish.Models.GenSensorItem;
import src.paddlefish.R;

/**
 * Created by USER on 02.10.2015.
 */
public class SensorSettingsActivity extends AppCompatActivity {
    private int objectPosition;
    private TextView tv;
    private GenSensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);
        tv = (TextView)findViewById(R.id.activity_sensor_settings_textview);
        objectPosition = getIntent().getIntExtra("object_position",22);
        tv.setText(String.valueOf(objectPosition));
        sensor = MainActivity.listAdapter.getItem(objectPosition).genSensor;
        StringBuilder builder = new StringBuilder();


    }
}
