package com.AndroidProject.taitiesyunbao;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Warning: One bug in initListener().
 * Created by JCChen on 2017/4/9.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<ItemInfo> list;
    private LikeFragment.OnLikeListener likeListener;
    private MenuFragment.OnBuyItemListListener buyItemListListener;

    public MenuRecyclerViewAdapter(Context context, List<ItemInfo> list
            , LikeFragment.OnLikeListener likeListener, MenuFragment.OnBuyItemListListener buyItemListListener) {
        this.context = context;
        this.list = list;
        this.likeListener = likeListener;
        this.buyItemListListener = buyItemListListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {

        viewHolder.pic_imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.white));
        new GetImage(viewHolder.pic_imageView).execute(list.get(index).getImgurID());
        viewHolder.name_textView.setText(list.get(index).getName());
        viewHolder.price_textView.setText(list.get(index).getPrice() + "元");
        viewHolder.amount_textView.setText(String.valueOf(list.get(index).getAmount()));
        viewHolder.info_textView.setText(list.get(index).getInfo());
        //viewHolder.info_textView.measure(0, 0);
        //viewHolder.info_Height.setText(Integer.toString(viewHolder.info_textView.getMeasuredHeight()));
        viewHolder.info_textView.setVisibility(View.GONE);

        setAmount(viewHolder, index);
        setLikeState(viewHolder, index);

        initListener(viewHolder, index);

        // Set MarginBottom of the last card in order not to cover the Like bottom.
        if(index == list.size()-1) {
            ViewGroup.LayoutParams layoutParams = viewHolder.menu_cardView.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams =
                                                (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.bottomMargin = 190;
            viewHolder.menu_cardView.setLayoutParams(marginLayoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView menu_cardView;
        public ImageView pic_imageView, like_imageView, info_imageView, plus_imageView, minus_imageView;
        public TextView name_textView, price_textView, amount_textView, info_textView, info_Height;
        public ViewHolder(View view) {
            super(view);
            pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
            menu_cardView = (CardView) view.findViewById(R.id.menu_cardView);
            like_imageView = (ImageView) view.findViewById(R.id.like_imageView);
            info_imageView = (ImageView) view.findViewById(R.id.info_imageView);
            plus_imageView = (ImageView) view.findViewById(R.id.plus_imageView);
            minus_imageView = (ImageView) view.findViewById(R.id.minus_imageView);
            name_textView = (TextView) view.findViewById(R.id.name_textView);
            price_textView = (TextView) view.findViewById(R.id.price_textView);
            amount_textView = (TextView) view.findViewById(R.id.amount_textView);
            info_textView = (TextView) view.findViewById(R.id.info_textView);
            info_Height = (TextView) view.findViewById(R.id.info_Height);
        }
    }

    private void initListener(ViewHolder viewHolder, int index) {
        final int j = index;
        final ViewHolder v = viewHolder;

        viewHolder.info_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //ValueAnimator valueAnimator;
                //int origHeight = view.getHeight();
                if(v.info_imageView.isSelected()) {
                    v.info_imageView.setSelected(false);
                    //valueAnimator = ValueAnimator.ofInt(origHeight,
                    //                        origHeight - Integer.parseInt(v.info_Height.getText().toString()));
                    v.info_textView.setVisibility(View.GONE);
                    //Log.i("Height", v.info_Height.getText().toString());
                }
                else {
                    v.info_imageView.setSelected(true);
                    v.info_textView.setVisibility(View.VISIBLE);
                    //valueAnimator = ValueAnimator.ofInt(origHeight,
                    //                        origHeight + Integer.parseInt(v.info_Height.getText().toString()));
                    //Log.i("Height", v.info_Height.getText().toString());
                }
                //valueAnimator.setDuration(500);
                //valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                //valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                //    public void onAnimationUpdate(ValueAnimator animation) {
                //        Integer value = (Integer) animation.getAnimatedValue();
                //        view.getLayoutParams().height = value.intValue();
                //        view.requestLayout();
                //    }
                //});
                //valueAnimator.start();
            }
        });

        viewHolder.like_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(v.like_imageView.isSelected()) {
                    v.like_imageView.setSelected(false);
                    list.get(j).setLikeState(false);
                    likeListener.delLikeList(list.get(j));
                }
                else {
                    view.setSelected(true);
                    list.get(j).setLikeState(true);
                    likeListener.addLikeList(list.get(j));
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
                }
            }
        });


    }

    private void setLikeState(ViewHolder viewHolder, int index) {
        if(list.get(index).getLikeState()) {
            viewHolder.like_imageView.setSelected(true);
        }
        else {
            viewHolder.like_imageView.setSelected(false);
        }
    }

    private void setAmount(ViewHolder viewHolder, int index) {
        viewHolder.amount_textView.setText(String.valueOf(list.get(index).getAmount()));
    }
}
