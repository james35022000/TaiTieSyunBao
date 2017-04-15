package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by JCChen on 2017/4/11.
 */

public class PurchaseRecyclerViewAdapter extends RecyclerView.Adapter<PurchaseRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private Vector<ItemInfo> buyList;


    public PurchaseRecyclerViewAdapter(Context context, Vector<ItemInfo> buyList) {
        this.context = context;
        this.buyList = buyList;
    }

    @Override
    public PurchaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                               .inflate(R.layout.buy_item_card_view, viewGroup, false);
        return new PurchaseRecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PurchaseRecyclerViewAdapter.ViewHolder viewHolder, int index) {
        initListener(viewHolder, index);

        viewHolder.pic_imageView.setImageResource(buyList.get(index).getImageID());
        viewHolder.name_textView.setText(buyList.get(index).getName());
        viewHolder.price_textView.setText(String.valueOf(buyList.get(index).getPrice()) + "元");
        viewHolder.amount_textView.setText(String.valueOf(buyList.get(index).getAmount()) + "個");
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

    private void initListener(PurchaseRecyclerViewAdapter.ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;

    }
}
