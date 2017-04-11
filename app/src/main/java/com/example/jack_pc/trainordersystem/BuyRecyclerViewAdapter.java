package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Jack-PC on 2017/4/11.
 */

public class BuyRecyclerViewAdapter extends RecyclerView.Adapter<BuyRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private Vector<CardStruct> buyList;


    public BuyRecyclerViewAdapter(Context context, Vector<CardStruct> buyList) {
        this.context = context;
        this.buyList = buyList;
    }

    @Override
    public BuyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.buy_item_card_view, viewGroup, false);
        return new BuyRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BuyRecyclerViewAdapter.ViewHolder viewHolder, int index) {
        initListener(viewHolder, index);

        viewHolder.pic_imageView.setImageResource(buyList.get(index).getImageID(context));
        viewHolder.name_textView.setText(buyList.get(index).getName(context));
        viewHolder.price_textView.setText(String.valueOf(buyList.get(index).getPrice(context)) + "元");
        viewHolder.amount_textView.setText(String.valueOf(buyList.get(index).getAmount(context)) + "個");
    }

    @Override
    public int getItemCount() {
        return buyList == null ? 0 : buyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView buy_cardView;
        public ImageView pic_imageView, back_imageView, fore_imageView;
        public TextView name_textView, price_textView, amount_textView;
        public ViewHolder(View view) {
            super(view);
            buy_cardView = (CardView) view.findViewById(R.id.buy_cardView);
            pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
            back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
            fore_imageView =(ImageView) view.findViewById(R.id.fore_imageView);
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);
            amount_textView = (TextView) view.findViewById(R.id.amount_textView);
        }
    }

    private void initListener(BuyRecyclerViewAdapter.ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;

    }
}
