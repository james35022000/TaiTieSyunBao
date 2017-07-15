package jcchen.taitiesyunbao;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by JCChen on 2017/7/13.
 */

public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private Vector<StoreInfo> storeList;

    public StoreRecyclerViewAdapter(Context context, Vector<StoreInfo> storeList) {
        this.context = context;
        this.storeList = storeList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.store_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        viewHolder.name_textView.setText(storeList.get(index).getName());
        viewHolder.info_textView.setText(storeList.get(index).getInfo());
        StoreImagePagerAdapter adapter =
                new StoreImagePagerAdapter(context, storeList.get(index).getImageUrl());
        viewHolder.pic_viewPager.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return storeList == null ? 0 : storeList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView store_cardView;
        public TextView name_textView, info_textView;
        public ViewPager pic_viewPager;
        public ImageView like_imageView;
        public ViewHolder(View v) {
            super(v);
            store_cardView = (CardView) v.findViewById(R.id.store_cardView);
            name_textView = (TextView) v.findViewById(R.id.name_textView);
            info_textView = (TextView) v.findViewById(R.id.info_textView);
            pic_viewPager = (ViewPager) v.findViewById(R.id.pic_viewPager);
            like_imageView = (ImageView) v.findViewById(R.id.like_imageView);
        }
    }
}