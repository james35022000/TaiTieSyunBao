package jcchen.taitiesyunbao.View.CircularListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import jcchen.taitiesyunbao.R;


/**
 * Created by JCChen on 2017/8/31.
 */

public class CircularListViewAdapter extends BaseAdapter {

    private Context context;

    private final int count = 10000;

    private List<String> region_list;
    private ListView select_listView;

    public CircularListViewAdapter(Context context, ListView select_listView, List<String> region_list) {
        this.context = context;
        this.select_listView = select_listView;
        this.region_list = region_list;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            ContentView contentView = (ContentView) LayoutInflater.from(context).inflate(R.layout.select_region_listview_content, null);
            contentView.setParentHeight(select_listView.getHeight());
            convertView = contentView;
        }
        TextView region_name = (TextView) convertView.findViewById(R.id.region_name);
        region_name.setText(region_list.get(Math.abs(position - count / 2) % region_list.size()));
        return convertView;
    }
}
