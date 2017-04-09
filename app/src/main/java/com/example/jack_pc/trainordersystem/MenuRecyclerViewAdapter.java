package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<CardStruct> list;
    private CardView cardView;

    public MenuRecyclerViewAdapter(Context context, List<CardStruct> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);

        cardView = (CardView) v.findViewById(R.id.menu_cardView);
        cardView.setOnLongClickListener(longClickListener);
        cardView.setOnClickListener(clickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        viewHolder.img_cardView.setImageDrawable(context.getResources()
                .getDrawable(list.get(index).img_drawableID));
        viewHolder.name_cardView.setText(context.getResources()
                .getString(list.get(index).name_StringID));
        viewHolder.price_cardView.setText(String.valueOf(list.get(index).price) + "元");
        viewHolder.amount_cardView.setAdapter(list.get(index).amountList);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_cardView;
        public TextView name_cardView, price_cardView;
        public Spinner amount_cardView;

        public ViewHolder(View view) {
            super(view);
            img_cardView = (ImageView) view.findViewById(R.id.img_cardView);
            name_cardView = (TextView) view.findViewById(R.id.name_cardView);
            price_cardView = (TextView) view.findViewById(R.id.price_cardView);
            amount_cardView = (Spinner) view.findViewById(R.id.amount_cardView);
        }
    }

    public View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(context, "已加入我的最愛", Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
