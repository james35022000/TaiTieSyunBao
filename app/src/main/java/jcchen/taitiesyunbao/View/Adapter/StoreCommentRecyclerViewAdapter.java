package jcchen.taitiesyunbao.View.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Sampler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.View.Widget.RoundedImageView;
import jcchen.taitiesyunbao.Entity.StoreComment;

import static jcchen.taitiesyunbao.Entity.Constant.RESOURCE_GOOGLE;

/**
 * Created by JCChen on 2017/7/28.
 */

public class StoreCommentRecyclerViewAdapter extends RecyclerView.Adapter<StoreCommentRecyclerViewAdapter.ViewHolder> {

    private final int DEFAULT_CARD = 0;
    private final int FIRST_CARD = 1;
    private final int LOADING_CARD = 2;

    private boolean isCommentLoading = false;

    private boolean AnimationState = true;

    private Context context;

    private List<StoreComment> commentList;

    public StoreCommentRecyclerViewAdapter(Context context, List<StoreComment> commentList) {
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

            case DEFAULT_CARD:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.store_comment_cardview, viewGroup, false);
                break;

            default:
                view = null;
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

            case DEFAULT_CARD:
                viewHolder.comment_textView.setText(commentList.get(index).getComment());
                viewHolder.userName_textView.setText(commentList.get(index).getUserName());
                final ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.loadImage(commentList.get(index).getUserPicUrl(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        viewHolder.userPic_imageView.setImageBitmap(loadedImage);
                        /*Animation animation = new AlphaAnimation(0, 1);
                        AnimationSet animationSet = new AnimationSet(true);
                        animation.setDuration(500);
                        animationSet.addAnimation(animation);
                        viewHolder.userPic_imageView.startAnimation(animationSet);*/
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

            default:
                break;
        }

        if(AnimationState)
            contentAnimation(getItemViewType(index), viewHolder, index);
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

    public void setAnimationState(boolean AnimationState) {
        this.AnimationState = AnimationState;
    }

    private void contentAnimation(int CARD, final ViewHolder viewHolder, int index) {
        ObjectAnimator translationY, alphaIn;
        AnimatorSet animatorSet;
        switch(CARD) {
            case FIRST_CARD:
                // Star
                for(int i = 0; i < 5; i++) {
                    final int j = i;
                    translationY = ObjectAnimator.ofFloat(viewHolder.star_imageView[i], "translationY", 300, 0);
                    translationY.setDuration(250);
                    translationY.setInterpolator(new OvershootInterpolator(2f));
                    alphaIn = ObjectAnimator.ofFloat(viewHolder.star_imageView[i], "alpha", 0, 1);
                    alphaIn.setDuration(100);
                    animatorSet = new AnimatorSet();
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            viewHolder.star_imageView[j].setAlpha(0f);
                        }
                    });
                    animatorSet.playTogether(translationY, alphaIn);
                    animatorSet.setStartDelay(30 * i);
                    animatorSet.start();
                }

                // Comment
                translationY = ObjectAnimator.ofFloat(viewHolder.comment_editText, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1.5f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.comment_editText, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.comment_editText.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(90);
                animatorSet.start();

                // Button
                translationY = ObjectAnimator.ofFloat(viewHolder.send_imageView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1.5f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.send_imageView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.send_imageView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(150);
                animatorSet.start();
                break;

            case LOADING_CARD:
                break;

            case DEFAULT_CARD:
                // userPic
                translationY = ObjectAnimator.ofFloat(viewHolder.userPic_imageView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.userPic_imageView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.userPic_imageView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(150 + index * 60);
                animatorSet.start();

                // userName & time
                translationY = ObjectAnimator.ofFloat(viewHolder.userName_textView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.userName_textView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.userName_textView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(210 + index * 60);
                animatorSet.start();
                translationY = ObjectAnimator.ofFloat(viewHolder.time_textView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.time_textView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.time_textView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(210 + index * 60);
                animatorSet.start();

                // star
                for(int i = 0; i < 5; i++) {
                    final int j = i;
                    translationY = ObjectAnimator.ofFloat(viewHolder.star_imageView[i], "translationY", 300, 0);
                    translationY.setDuration(250);
                    translationY.setInterpolator(new OvershootInterpolator(0.7f));
                    alphaIn = ObjectAnimator.ofFloat(viewHolder.star_imageView[i], "alpha", 0, 1);
                    alphaIn.setDuration(100);
                    animatorSet = new AnimatorSet();
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            viewHolder.star_imageView[j].setAlpha(0f);
                        }
                    });
                    animatorSet.playTogether(translationY, alphaIn);
                    animatorSet.setStartDelay(270 + index * 60 + 15 * i);
                    animatorSet.start();
                }

                // comment
                translationY = ObjectAnimator.ofFloat(viewHolder.comment_textView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.comment_textView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.comment_textView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(300 + index * 60);
                animatorSet.start();

                // resource
                translationY = ObjectAnimator.ofFloat(viewHolder.resource_imageView, "translationY", 300, 0);
                translationY.setDuration(250);
                translationY.setInterpolator(new OvershootInterpolator(1f));
                alphaIn = ObjectAnimator.ofFloat(viewHolder.resource_imageView, "alpha", 0, 1);
                alphaIn.setDuration(100);
                animatorSet = new AnimatorSet();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        viewHolder.resource_imageView.setAlpha(0f);
                    }
                });
                animatorSet.playTogether(translationY, alphaIn);
                animatorSet.setStartDelay(330 + index * 60);
                animatorSet.start();
                break;

            default:
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView userPic_imageView;
        public TextView userName_textView, comment_textView, time_textView;
        public EditText comment_editText;
        public ImageView send_imageView, resource_imageView;
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
            comment_editText = (EditText) v.findViewById(R.id.comment_editText);
            send_imageView = (ImageView) v.findViewById(R.id.send_imageView);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }
    }

    public void setLoadingState(boolean isCommentLoading) {
        this.isCommentLoading = isCommentLoading;
        notifyItemChanged(getItemCount());
    }

    public List<StoreComment> getCommentList() {
        return commentList;
    }
}
