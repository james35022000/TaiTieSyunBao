package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

/**
 * Created by JCChen on 2017/4/10.
 */

public class LikeRecyclerViewAdapter extends RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private Vector<ItemInfo> list;
    private LikeFragment.OnLikeListener likeListener;
    private MenuRecyclerViewAdapter.SaveUserData saveUserData;
    private MenuFragment.OnBuyItemListListener buyItemListListener;


    public LikeRecyclerViewAdapter(Context context, LikeFragment.OnLikeListener likeListener,
                                        MenuRecyclerViewAdapter.SaveUserData saveUserData,
                                   MenuFragment.OnBuyItemListListener buyItemListListener) {
        this.context = context;
        this.likeListener = likeListener;
        this.list = likeListener.getLikeList();
        this.saveUserData = saveUserData;
        this.buyItemListListener = buyItemListListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.like_card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {

        viewHolder.info_textView.setVisibility(View.GONE);

        initListener(viewHolder, index);
        setLikeState(viewHolder, index);
        setAmount(viewHolder, index);

        new GetImage(context, viewHolder.pic_imageView).execute(list.get(index).getImgurID());
        viewHolder.name_textView.setText(list.get(index).getName());
        viewHolder.price_textView.setText(list.get(index).getPrice() + "元");
        viewHolder.info_textView.setText(list.get(index).getInfo());

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView like_cardView;
        public ImageView pic_imageView, like_imageView, info_imageView, minus_imageView
                            , plus_imageView;
        public TextView name_textView, price_textView, info_textView, amount_textView;
        public ViewHolder(View view) {
            super(view);
            like_cardView = (CardView) view.findViewById(R.id.like_cardView);
            pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
            like_imageView = (ImageView) view.findViewById(R.id.like_imageView);
            info_imageView = (ImageView) view.findViewById(R.id.info_imageView);
            minus_imageView = (ImageView) view.findViewById(R.id.minus_imageView);
            plus_imageView = (ImageView) view.findViewById(R.id.plus_imageView);
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);
            info_textView = (TextView) view.findViewById(R.id.info_textView);
            amount_textView = (TextView) view.findViewById(R.id.amount_textView);
        }
    }

    private void initListener(ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;
        viewHolder.info_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v.info_imageView.isSelected()) {
                    v.info_imageView.setSelected(false);
                    v.info_textView.setVisibility(View.GONE);
                }
                else {
                    v.info_imageView.setSelected(true);
                    v.info_textView.setVisibility(View.VISIBLE);
                }
            }
        });

        viewHolder.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.like_imageView.isSelected()) {
                    v.like_imageView.setSelected(false);
                    list.get(j).setLikeState(false);
                    likeListener.delLikeList(list.get(j));
                    saveUserData.removeItem(list.get(j));
                } else {
                    v.like_imageView.setSelected(true);
                    list.get(j).setLikeState(true);
                    likeListener.addLikeList(list.get(j));
                    saveUserData.storeItem(list.get(j));
                }
            }
        });

        viewHolder.plus_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = list.get(j).getAmount();
                if(amount < list.get(j).getMaxAmount()) {
                    v.amount_textView.setText(String.valueOf(amount + 1));
                    list.get(j).setAmount(amount + 1);
                    buyItemListListener.addBuyList(list.get(j));
                    saveUserData.storeItem(list.get(j));
                }
                else {
                    Toast.makeText(context, "已達到最大數量", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.minus_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = list.get(j).getAmount();
                if(amount > 0) {
                    v.amount_textView.setText(String.valueOf(amount - 1));
                    list.get(j).setAmount(amount - 1);
                    buyItemListListener.addBuyList(list.get(j));
                    if(amount == 1)
                        saveUserData.removeItem(list.get(j));
                    else
                        saveUserData.storeItem(list.get(j));
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

    private void setAmount(ViewHolder viewHolder, int index) {
        int i = buyItemListListener.isBuyItemExist(list.get(index));
        if(i != -1) {
            int amount = buyItemListListener.getBuyList().get(i).getAmount();
            viewHolder.amount_textView.setText(String.valueOf(amount));
            list.get(index).setAmount(amount);
        }
    }
}
