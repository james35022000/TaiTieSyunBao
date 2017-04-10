package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;
import java.util.Vector;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<CardStruct> list;
    private LikeFragment.onLikeListener likeListener;

    public MenuRecyclerViewAdapter(Context context, List<CardStruct> list
            , LikeFragment.onLikeListener likeListener) {
        this.context = context;
        this.list = list;
        this.likeListener = likeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_card_view, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {


        viewHolder.pic_imageView.setImageResource(list.get(index).getImageID(context));
        viewHolder.name_textView.setText(list.get(index).getName(context));
        viewHolder.price_textView.setText(list.get(index).getPrice(context) + "元");
        ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(context
                , R.layout.spinner_center_item, list.get(index).getAmountList(context));
        amountAdapter.setDropDownViewResource(R.layout.spinner_center_item);

        setLikeState(viewHolder, index);
        initListener(viewHolder, index);

        // Set MarginBottom of the last card in order not to cover the Like bottom.
        if(index == list.size()-1) {
            ViewGroup.LayoutParams layoutParams = viewHolder.menu_cardView.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams =
                                                (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins(0, 0, 0, 190);
            viewHolder.menu_cardView.setLayoutParams(marginLayoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView menu_cardView;
        public ImageView pic_imageView, like_imageView;
        public TextView name_textView, price_textView;
        public ViewHolder(View view) {
            super(view);
            pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
            menu_cardView = (CardView) view.findViewById(R.id.menu_cardView);
            like_imageView = (ImageView) view.findViewById(R.id.like_imageView);
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);
        }
    }

    private void initListener(ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuInfoActivity.class);
                intent.putExtra("List", list.get(j));
                context.startActivity(intent);
            }
        };
        v.menu_cardView.setOnClickListener(clickListener);


        v.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v.like_imageView.isSelected()) {
                    v.like_imageView.setSelected(false);
                    likeListener.delLikeList(list.get(j));
                }
                else {
                    view.setSelected(true);
                    likeListener.addLikeList(list.get(j));
                }
            }
        });
    }

    private void setLikeState(ViewHolder viewHolder, int index) {
        if(likeListener.isExist(list.get(index)) != -1) {
            viewHolder.like_imageView.setSelected(true);
        }
        else {
            viewHolder.like_imageView.setSelected(false);
        }
    }
}
