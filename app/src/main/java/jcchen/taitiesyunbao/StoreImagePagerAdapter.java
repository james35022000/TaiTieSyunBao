package jcchen.taitiesyunbao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

/**
 * Created by JCChen on 2017/7/16.
 */

public class StoreImagePagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Vector<String> ImageUrl;

    public StoreImagePagerAdapter(Context context, Vector<String> ImageUrl) {
        this.context = context;
        this.ImageUrl = ImageUrl;
    }

    @Override
    public int getCount() {
        return ImageUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.store_image_layout, null);

        final ImageView pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.loadImage(ImageUrl.get(position), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pic_imageView.setImageBitmap(loadedImage);
                Animation animation = new AlphaAnimation(0, 1);
                AnimationSet animationSet = new AnimationSet(true);
                animation.setDuration(500);
                animationSet.addAnimation(animation);
                pic_imageView.startAnimation(animationSet);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
