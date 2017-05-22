package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by JCChen on 2017/5/21.
 */

public class PurchaseInfoArrayAdapter extends ArrayAdapter<BuyInfo> {
    Context context;
    int layoutResourceId;
    Vector<BuyInfo> buyInfo;

    public PurchaseInfoArrayAdapter(Context context, int layoutResourceId, Vector<BuyInfo> buyInfo) {
        super(context, layoutResourceId, buyInfo);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.buyInfo = buyInfo;
    }

    @Override
    public void add(BuyInfo object) {
        this.buyInfo.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        BuyInfo mBuyInfo = this.buyInfo.get(position);

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_text_layout, null);
        }

        if(mBuyInfo != null) {
            TextView name_textView, amount_textView, price_textView;
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            amount_textView = (TextView) view.findViewById(R.id.amount_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);

            name_textView.setText(mBuyInfo.getName());
            amount_textView.setText(Integer.toString(mBuyInfo.getAmount()));
            price_textView.setText(Integer.toString(mBuyInfo.getPrice() * mBuyInfo.getAmount()));
        }
        return view;
    }

    @Override
    public int getCount() {
        return this.buyInfo.size();
    }
}
