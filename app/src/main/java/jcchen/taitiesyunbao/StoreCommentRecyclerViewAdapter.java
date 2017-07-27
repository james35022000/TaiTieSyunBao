package jcchen.taitiesyunbao;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Vector;

/**
 * Created by Jack-PC on 2017/7/28.
 */

public class StoreCommentRecyclerViewAdapter extends RecyclerView.Adapter<StoreCommentRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private Vector<StoreComment> commentList;

    public StoreCommentRecyclerViewAdapter(Context context, Vector<StoreComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.store_comment_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.comment_TextView.setText(commentList.get(index).getComment());
        viewHolder.userName_TextView.setText(commentList.get(index).getUserName());
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(commentList.get(index).getUserPicUrl(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.userPic_ImageView.setImageBitmap(loadedImage);
                Animation animation = new AlphaAnimation(0, 1);
                AnimationSet animationSet = new AnimationSet(true);
                animation.setDuration(500);
                animationSet.addAnimation(animation);
                viewHolder.userPic_ImageView.startAnimation(animationSet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userPic_ImageView;
        public TextView userName_TextView, comment_TextView;
        public ViewHolder(View v) {
            super(v);
            userPic_ImageView = (ImageView) v.findViewById(R.id.userPic_imageView);
            userName_TextView = (TextView) v.findViewById(R.id.userName_textView);
            comment_TextView = (TextView) v.findViewById(R.id.comment_textView);
        }
    }
}
