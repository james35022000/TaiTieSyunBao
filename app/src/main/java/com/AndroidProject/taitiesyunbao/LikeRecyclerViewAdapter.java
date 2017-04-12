package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JCChen on 2017/4/10.
 */

public class LikeRecyclerViewAdapter extends RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ItemInfo> list;
    private LikeFragment.OnLikeListener likeListener;


    public LikeRecyclerViewAdapter(Context context, LikeFragment.OnLikeListener likeListener) {
        this.context = context;
        this.likeListener = likeListener;
        this.list = likeListener.getLikeList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.like_card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {

        initListener(viewHolder, index);
        setLikeState(viewHolder, index);

        viewHolder.pic_imageView.setImageResource(list.get(index).getImageID());
        viewHolder.name_textView.setText(list.get(index).getName());
        viewHolder.price_textView.setText(list.get(index).getPrice() + "元");

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView like_cardView;
        public ImageView pic_imageView, like_imageView, info_imageView;
        public TextView name_textView, price_textView;
        public ViewHolder(View view) {
            super(view);
            like_cardView = (CardView) view.findViewById(R.id.like_cardView);
            pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
            like_imageView = (ImageView) view.findViewById(R.id.like_imageView);
            info_imageView =(ImageView) view.findViewById(R.id.info_imageView);
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);
        }
    }

    private void initListener(ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;
        viewHolder.info_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuInfoActivity.class);
                intent.putExtra("List", list.get(j));
                context.startActivity(intent);
            }
        });

        viewHolder.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.like_imageView.isSelected()) {
                    v.like_imageView.setSelected(false);
                    likeListener.delLikeList(list.get(j));
                } else {
                    v.like_imageView.setSelected(true);
                    likeListener.addLikeList(list.get(j));
                }
            }
        });
    }

    private void setLikeState(ViewHolder viewHolder, int index) {
        if(likeListener.isLikeItemExist(list.get(index)) != -1) {
            viewHolder.like_imageView.setSelected(true);
        }
        else {
            viewHolder.like_imageView.setSelected(false);
        }
    }
}
