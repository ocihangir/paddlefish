package net.aldemir.paddlefish.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import net.aldemir.paddlefish.R;
import net.aldemir.paddlefish.adapters.DashboardArrayAdapter;
import net.aldemir.paddlefish.model.Device;


public class DashboardActivity extends ActionBarActivity {

    private Context mContext;

    private ListView mListView;
    private ImageView mAppSettings, mAddDevice;

    private DashboardArrayAdapter mDashboardArrayAdapter;

    private Device[] mDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initMemberVariables();
        initViews();
        setCallbacks();

        mListView.setAdapter(mDashboardArrayAdapter);
    }

    public void initViews() {
        mListView = (ListView) findViewById(R.id.listview);
        mAppSettings = (ImageView) findViewById(R.id.appsettings);
        mAddDevice = (ImageView) findViewById(R.id.adddevice);
    }

    private void initMemberVariables() {
        mContext = this;

        mDevices = new Device[]{
                new Device("Temperature Sensor", R.drawable.device_temp),
                new Device("Accelerometer ADXL345", R.drawable.device_adxl345),
        };

        mDashboardArrayAdapter = new DashboardArrayAdapter(this, mDevices);
    }

    private void setCallbacks() {
        mAppSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AppSettingsActivity.class);
                mContext.startActivity(intent);
            }
        });

        mAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddDeviceActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

}