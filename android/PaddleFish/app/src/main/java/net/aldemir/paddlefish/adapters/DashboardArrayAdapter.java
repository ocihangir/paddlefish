package net.aldemir.paddlefish.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.aldemir.paddlefish.R;
import net.aldemir.paddlefish.model.Device;
import net.aldemir.paddlefish.ui.DeleteDeviceActivity;
import net.aldemir.paddlefish.ui.DeviceSettingsActivity;
import net.aldemir.paddlefish.ui.MonitorActivity;

/**
 * Created by mustafa on 15.06.15.
 */
public class DashboardArrayAdapter extends ArrayAdapter<Device> {

    private final Context mContext;
    private final Device[] mDevices;

    private TextView mDeviceName;
    private ImageView mIcon, mDeleteDevice, mDeviceSettings;

    public DashboardArrayAdapter(Context context, Device[] devices) {
        super(context, -1, devices);

        mContext = context;
        mDevices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_device, parent, false);

        mDeviceName = (TextView) rowView.findViewById(R.id.devicename);
        mIcon = (ImageView) rowView.findViewById(R.id.icon);
        mDeleteDevice = (ImageView) rowView.findViewById(R.id.delete);
        mDeviceSettings = (ImageView) rowView.findViewById(R.id.settings);

        mDeviceName.setText(mDevices[position].getDeviceName());
        mIcon.setImageDrawable(getContext().getResources().getDrawable(mDevices[position].getDeviceIcon()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MonitorActivity.class);
                mContext.startActivity(intent);
            }
        });
        mDeleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeleteDeviceActivity.class);
                mContext.startActivity(intent);
            }
        });
        mDeviceSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeviceSettingsActivity.class);
                mContext.startActivity(intent);
            }
        });


        return rowView;
    }

}