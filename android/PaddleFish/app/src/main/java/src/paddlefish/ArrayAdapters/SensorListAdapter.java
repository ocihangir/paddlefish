package src.paddlefish.ArrayAdapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import src.paddlefish.Models.SensorItem;
import src.paddlefish.R;

/**
 * Created by USER on 22.09.2015.
 */
public class SensorListAdapter extends ArrayAdapter<SensorItem> {
    private final List<SensorItem> objects;
    private final Activity context;

    public SensorListAdapter(Activity context, int resource, List<SensorItem> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    private static class SensorItemHolder{
        ImageView imageView;
        TextView textView;
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
            holder.textView = (TextView) convertView.findViewById(R.id.listitem_sensor_textView);
            convertView.setTag(holder);
        }else{
            holder = (SensorItemHolder) convertView.getTag();
        }

        SensorItem item = objects.get(position);
        holder.textView.setText(item.name);

        return convertView;
    }
}
