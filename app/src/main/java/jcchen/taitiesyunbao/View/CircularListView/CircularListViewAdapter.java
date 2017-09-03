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

    public int getListSize() {
        return region_list.size();
    }

    @Override
    public Object getItem(int position) {
        return region_list.get((int) getItemId(position));
    }

    @Override
    public long getItemId(int position) {
        if(position >= count / 2)
            return (position - count / 2) % region_list.size();
        else
            return Math.abs((count / 2 - position - 1) % region_list.size() - region_list.size() + 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.select_region_listview_content, null);
        }
        ((CircularListViewContent) convertView).setParentHeight(select_listView.getHeight());
        TextView region_name = (TextView) convertView.findViewById(R.id.region_name);
        region_name.setText((String) getItem(position));
        return convertView;
    }
}
