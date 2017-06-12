package com.AndroidProject.taitiesyunbao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 石家 on 2017/6/11.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {

    private ItemData[] itemsData;

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        protected TextView text_title;
        protected TextView text_content;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            text_title = (TextView) v.findViewById(R.id.text_title);
            text_content = (TextView) v.findViewById(R.id.text_content);
        }
    }

    public Myadapter(ItemData[] itemsData) {
        this.itemsData = itemsData;
    }

    @Override
    public Myadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        Myadapter.ViewHolder vh = (Myadapter.ViewHolder) new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text_title.setText(itemsData[position].getTitle());
        holder.text_content.setText(itemsData[position].getContent());
        holder.imageView.setImageResource(itemsData[position].getImgID());
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
