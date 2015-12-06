package src.paddlefish.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paddlefish.def.ControlInput;
import paddlefish.def.ControlInputValue;
import paddlefish.def.ControlType;
import paddlefish.def.SensorCategory;
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
    private TextView devInfoSensorType;
    private TextView devInfoManufacturer;
    private TextView devInfoDescription;

    private Spinner devCommDeviceAdress;

    private CheckBox devControlPowerDown;
    private CheckBox devControlMeasureActivity;
    private Spinner devControlWakeUpFrequency;

    private GenSensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_settings);
        objectPosition = getIntent().getIntExtra("object_position", 22);
        sensor = MainActivity.listAdapter.getItem(objectPosition).genSensor;

        initialize();
        fillFields();
    }


    private void initialize() {
        devInfoSensorType = (TextView) findViewById(R.id.activity_sensor_settings_device_info_sensor_type_tv);
        devInfoManufacturer = (TextView) findViewById(R.id.activity_sensor_settings_device_info_manufacturer_tv);
        devInfoDescription = (TextView) findViewById(R.id.activity_sensor_settings_device_info_description_tv);


        devCommDeviceAdress = (Spinner) findViewById(R.id.activity_sensor_settings_communication_deviceaddress_spinner);

        devControlPowerDown = (CheckBox) findViewById(R.id.activity_sensor_settings_communication_powerdown_checkbox);
        devControlMeasureActivity = (CheckBox) findViewById(R.id.activity_sensor_settings_communication_measureActivity_checkbox);
        devControlWakeUpFrequency = (Spinner) findViewById(R.id.activity_sensor_settings_communication_wakeup_frequency_spinner);
    }

    private void fillFields() {
        devInfoSensorType.setText(sensor.getIdentInfo().categ.toString());
        devInfoManufacturer.setText(sensor.getIdentInfo().manuf);
        devInfoDescription.setText(sensor.getIdentInfo().descr);

        // Set deviceAdress Spinner
        List deviceAdresses = new ArrayList();
        for (Map.Entry<Byte, Boolean> entry : sensor.getI2cInf().getDevAddrInf().entrySet()) {
            deviceAdresses.add(entry.getKey());

        }
        ArrayAdapter adressAdapter = new ArrayAdapter(this, R.layout.spinner_device_adress_item, R.id.spinner_device_address, deviceAdresses);
        //adressAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        devCommDeviceAdress.setAdapter(adressAdapter);
        //Set WakeUpFrequency Spinner
        List frequencies = new ArrayList();
        //sensor.getControlLst().get(0).cInputs.get(0).inValues.get(0).value;
        for (SensorControl control : sensor.getControlLst()) {
            for (ControlInput cInput : control.cInputs) {
                for (ControlInputValue value : cInput.inValues) {
                    if (control.type == ControlType.MEASURE) {
                        devControlMeasureActivity.setChecked(Boolean.valueOf(value.value.toString()));
                        Log.d("baq", value.value + "");
                    }else if(control.type == ControlType.POWERDOWN) {
                        devControlPowerDown.setChecked(Boolean.valueOf(value.value.toString()));
                        Log.d("baq",value.value+"");
                    }else if(control.type == ControlType.WAKEUP){
                        frequencies.add(value.value);
                }

            }
        }
    }

    ArrayAdapter frequencyAdapter = new ArrayAdapter(this, R.layout.spinner_wakeup_frequencies_item, R.id.spinner_wakeup_item_tv, frequencies);
    devControlWakeUpFrequency.setAdapter(frequencyAdapter);


}
}
