package jcchen.taitiesyunbao;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Vector;

import static jcchen.taitiesyunbao.Constant.RESOURCE_GOOGLE;

/**
 * Created by JCChen on 2017/7/28.
 */

public class StoreCommentRecyclerViewAdapter extends RecyclerView.Adapter<StoreCommentRecyclerViewAdapter.ViewHolder> {

    private final int DEFAULT_CARD = 0;
    private final int FIRST_CARD = 1;
    private final int LOADING_CARD = 2;

    private boolean isCommentLoading = false;

    private Context context;

    private Vector<StoreComment> commentList;

    public StoreCommentRecyclerViewAdapter(Context context, Vector<StoreComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case FIRST_CARD:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.comment_first_cardview, viewGroup, false);
                break;
            case LOADING_CARD:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.comment_recyclerview_progressbar, viewGroup, false);
                break;
            default:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.store_comment_cardview, viewGroup, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        switch(getItemViewType(index)) {
            case FIRST_CARD:
                break;
            case LOADING_CARD:
                if(isCommentLoading)
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                else
                    viewHolder.progressBar.setVisibility(View.GONE);
                viewHolder.progressBar.requestLayout();
                break;
            default:
                viewHolder.comment_textView.setText(commentList.get(index).getComment());
                viewHolder.userName_textView.setText(commentList.get(index).getUserName());
                final ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.loadImage(commentList.get(index).getUserPicUrl(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        viewHolder.userPic_imageView.setImageBitmap(loadedImage);
                        Animation animation = new AlphaAnimation(0, 1);
                        AnimationSet animationSet = new AnimationSet(true);
                        animation.setDuration(500);
                        animationSet.addAnimation(animation);
                        viewHolder.userPic_imageView.startAnimation(animationSet);
                    }
                });
                viewHolder.time_textView.setText(commentList.get(index).getTime());
                for (int i = 0; i < 5; i++) {
                    if (i < Integer.valueOf(commentList.get(index).getRate()))
                        viewHolder.star_imageView[i].setSelected(true);
                    else
                        viewHolder.star_imageView[i].setSelected(false);
                }
                switch (commentList.get(index).getResource()) {
                    case RESOURCE_GOOGLE:
                        viewHolder.resource_imageView.setImageDrawable(ContextCompat
                                .getDrawable(context, R.drawable.powered_by_google_light));
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 1 : commentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? FIRST_CARD :
                (position == commentList.size()) ? LOADING_CARD : DEFAULT_CARD;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView userPic_imageView;
        public TextView userName_textView, comment_textView, time_textView;
        public ImageView resource_imageView;
        public ImageView[] star_imageView = new ImageView[5];
        public ProgressBar progressBar;
        public ViewHolder(View v) {
            super(v);
            userPic_imageView = (RoundedImageView) v.findViewById(R.id.userPic_imageView);
            userName_textView = (TextView) v.findViewById(R.id.userName_textView);
            comment_textView = (TextView) v.findViewById(R.id.comment_textView);
            time_textView = (TextView) v.findViewById(R.id.time_textView);
            resource_imageView = (ImageView) v.findViewById(R.id.resource_imageView);
            star_imageView[0] = (ImageView) v.findViewById(R.id.star0_imageView);
            star_imageView[1] = (ImageView) v.findViewById(R.id.star1_imageView);
            star_imageView[2] = (ImageView) v.findViewById(R.id.star2_imageView);
            star_imageView[3] = (ImageView) v.findViewById(R.id.star3_imageView);
            star_imageView[4] = (ImageView) v.findViewById(R.id.star4_imageView);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public void setLoadingState(boolean isCommentLoading) {
        this.isCommentLoading = isCommentLoading;
        notifyItemChanged(getItemCount());
    }

    public Vector<StoreComment> getCommentList() {
        return commentList;
    }
}
