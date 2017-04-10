package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jack-PC on 2017/4/10.
 */

public class LikeRecyclerViewAdapter extends RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<CardStruct> list;
    private CardView like_cardView;
    private ImageView pic_imageView, like_imageView;
    private TextView name_textView, price_textView;


    public LikeRecyclerViewAdapter(Context context, List<CardStruct> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.like_card_view, viewGroup, false);

        like_cardView = (CardView) v.findViewById(R.id.like_cardView);
        pic_imageView = (ImageView) v.findViewById(R.id.pic_imageView);
        like_imageView = (ImageView) v.findViewById(R.id.like_imageView);
        name_textView = (TextView) v.findViewById(R.id.name_textView);
        price_textView = (TextView) v.findViewById(R.id.price_textView);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        pic_imageView.setImageResource(list.get(index).getImageID(context));
        name_textView.setText(list.get(index).getName(context));
        price_textView.setText(list.get(index).getPrice(context) + "元");

        initListener(index);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    private void initListener(int index) {
        final int j = index;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuInfoActivity.class);
                intent.putExtra("List", list.get(j));
                context.startActivity(intent);
            }
        };
        like_cardView.setOnClickListener(clickListener);

        like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like_imageView.isSelected()) {
                    like_imageView.setSelected(false);
                } else {
                    like_imageView.setSelected(true);
                }
            }
        });
    }
}
