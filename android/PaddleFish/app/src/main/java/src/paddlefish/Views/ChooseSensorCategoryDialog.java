package src.paddlefish.Views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import paddlefish.def.SensorCategory;
import src.paddlefish.ArrayAdapters.SensorChooseCategoryAdapter;
import src.paddlefish.MainActivity;
import src.paddlefish.Models.SensorItem;
import src.paddlefish.R;

/**
 * Created by USER on 25.09.2015.
 */
public class ChooseSensorCategoryDialog extends Dialog implements AdapterView.OnItemClickListener {

    private ListView lv;
    private SensorChooseCategoryAdapter lists;
    private Context myContext;
    public ChooseSensorCategoryDialog(Context context, int themeResId) {
        super(context, themeResId);
        myContext = context;
        setContentView(R.layout.dialog_choose_sensor_category);
        lv = (ListView)findViewById(R.id.dialog_listView);
        ArrayList<SensorItem> items = new ArrayList<SensorItem>();
        for (SensorCategory cat:SensorCategory.values())
        {
            if(!cat.getFolderName().isEmpty())
            {
                SensorItem item = new SensorItem();
                item.categoryName = cat.getFolderName();
                items.add(item);
            }
        }
        lists = new SensorChooseCategoryAdapter((Activity)myContext,R.layout.list_item_choose_sensor,items);
        lv.setAdapter(lists);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout layout = (LinearLayout)view.findViewById(R.id.list_item_choose_sensor_layout_addview);
        for(int i = 0; i < lists.getCount(); i++){
            if(position == i){
                continue;
            }
            View v = lv.getChildAt(i);
            LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.list_item_choose_sensor_layout_addview);
            linearLayout.setVisibility(View.GONE);
        }
        if (layout.getVisibility() == View.VISIBLE){
            layout.setVisibility(View.GONE);
        }else
        {
            layout.setVisibility(View.VISIBLE);
        }
    }



    public static void dissMissDialog(){

    }
}
