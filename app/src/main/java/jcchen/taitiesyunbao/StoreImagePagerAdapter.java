package jcchen.taitiesyunbao;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        ImageView pic_imageView = (ImageView) view.findViewById(R.id.pic_imageView);
        new GetImage(context, pic_imageView).execute(ImageUrl.get(position));

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
