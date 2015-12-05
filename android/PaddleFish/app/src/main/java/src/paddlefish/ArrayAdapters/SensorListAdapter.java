package src.paddlefish.ArrayAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import paddlefish.sensor.GenSensor;
import src.paddlefish.Models.GenSensorItem;
import src.paddlefish.Models.SensorItem;
import src.paddlefish.R;

/**
 * Created by USER on 22.09.2015.
 */
public class SensorListAdapter extends ArrayAdapter<GenSensorItem> {
    private final List<GenSensorItem> objects;
    private final Activity context;

    public SensorListAdapter(Activity context, int resource, List<GenSensorItem> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    private static class SensorItemHolder{
        ImageView imageView;
        TextView categoryName;
        TextView sensorName;
    }

    @Override
    public int getCount() {
        if(objects == null){
            return 0;
        }
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SensorItemHolder holder;

        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.listitem_sensor,parent,false);
            holder = new SensorItemHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.listitem_sensor_imageView);
            holder.categoryName = (TextView) convertView.findViewById(R.id.listitem_sensor_category_textView);
            holder.sensorName = (TextView)convertView.findViewById(R.id.listitem_sensor_name_textView);
            convertView.setTag(holder);
        }else{
            holder = (SensorItemHolder) convertView.getTag();
        }

        GenSensorItem item = objects.get(position);
        holder.categoryName.setText(item.sensorItem.categoryName);
        holder.sensorName.setText(item.sensorItem.sensorName);

        return convertView;
    }
}
