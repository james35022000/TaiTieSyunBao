package jcchen.taitiesyunbao.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import jcchen.taitiesyunbao.ImageAttr;

/**
 * Created by JCChen on 2017/7/16.
 */

public class StoreImagePagerAdapter extends PagerAdapter {
    private Context context;
    private Vector<ImageAttr> Image;
    private Vector<Drawable> drawable;

    public StoreImagePagerAdapter(Context context, Vector<ImageAttr> Image) {
        this.context = context;
        this.Image = Image;
        this.drawable = new Vector<>();
        for(int i = 0; i < Image.size(); i++)
            drawable.add(null);
    }

    public Vector<Drawable> getDrawable() {
        return drawable;
    }

    public Vector<ImageAttr> getImage() {
        return new Vector<>(Image);
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
