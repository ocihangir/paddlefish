package src.paddlefish.ArrayAdapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import paddlefish.def.SensorCategory;
import src.paddlefish.MainActivity;
import src.paddlefish.Models.SensorItem;
import src.paddlefish.R;

/**
 * Created by USER on 01.10.2015.
 */
public class SensorChooseCategoryAdapter extends ArrayAdapter<SensorItem> {

    private Activity context;
    private List<SensorItem> objects;

    public SensorChooseCategoryAdapter(Activity context, int resource, List<SensorItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        if (objects == null) {
            return 0;
        }
        return objects.size();
    }

    private static class CategoryHolder {
        TextView catName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryHolder holder;
        SensorItem sensorItem = objects.get(position);

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_choose_sensor, parent, false);
            try {
                createTextView(convertView, sensorItem);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            holder = new CategoryHolder();
            holder.catName = (TextView) convertView.findViewById(R.id.list_item_choose_sensor_textview);
            convertView.setTag(holder);
        } else {
            holder = (CategoryHolder) convertView.getTag();
        }
        holder.catName.setText(sensorItem.categoryName);
        return convertView;
    }

    private void createTextView(View convertView, final SensorItem sensorItem) throws SAXException, ParserConfigurationException, URISyntaxException {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(context);
        tv.setLayoutParams(params);
        ArrayList<String> devNames = MainActivity.curSt.getDevicesOfCat(SensorCategory.getCategory(sensorItem.categoryName));
        Log.d("names",SensorCategory.getCategory(sensorItem.categoryName)+"");
        if(devNames.size() == 0 || devNames.isEmpty() || devNames == null){
            tv.setText("Test");
            sensorItem.sensorName = "Test";
        }else{
            String devName = MainActivity.curSt.getDevicesOfCat(SensorCategory.getCategory(sensorItem.categoryName)).get(0);
            tv.setText(devName);
            sensorItem.sensorName = devName;
        }
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.list_item_choose_sensor_layout_addview);
        layout.addView(tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).dissmissDialogAndAddItem(sensorItem);
            }
        });
        layout.setVisibility(View.GONE);
    }
}
