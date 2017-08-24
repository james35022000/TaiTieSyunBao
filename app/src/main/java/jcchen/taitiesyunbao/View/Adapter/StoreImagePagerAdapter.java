package jcchen.taitiesyunbao.View.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Entity.ImageAttr;

/**
 * Created by JCChen on 2017/7/16.
 */

public class StoreImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ImageAttr> Image;
    private List<Drawable> drawable;

    public StoreImagePagerAdapter(Context context, List<ImageAttr> Image) {
        this.context = context;
        this.Image = Image;
        this.drawable = new ArrayList<>();
        for(int i = 0; i < Image.size(); i++)
            drawable.add(null);
    }

    public List<Drawable> getDrawable() {
        return drawable;
    }

    public List<ImageAttr> getImage() {
        return new ArrayList<>(Image);
    }

    @Override
    public int getCount() {
        return Image.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        final ImageView imageView = new ImageView(context);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(Image.get(position).getImageUrl(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                drawable.set(position, new BitmapDrawable(context.getResources(), loadedImage));
                Animation animation = new AlphaAnimation(0, 1);
                AnimationSet animationSet = new AnimationSet(true);
                animation.setDuration(500);
                animationSet.addAnimation(animation);
                imageView.startAnimation(animationSet);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(imageView, 0);
        return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
