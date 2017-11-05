package jcchen.taitiesyunbao.View.Container;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jcchen.taitiesyunbao.Entity.ImageAttr;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.MainActivity;
import jcchen.taitiesyunbao.View.Widget.BottomSheet;

import static jcchen.taitiesyunbao.Entity.Constant.LANGUAGE_TW;

/**
 * Created by JCChen on 2017/8/12.
 */

public class StoreInfoContainer extends FrameLayout implements Container {

    private ViewFlipper pic_info_viewFlipper;
    private TextView name_textView, tel_textView, address_textView;
    private ImageView comment_imageView;
    private FrameLayout map, filter, store_info_container;
    private Container comment_container;

    private MapFragment mapFragment;

    private StoreInfo storeInfo;

    private Context context;

    private Container container;

    public StoreInfoContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = this;
        this.container = this;
        this.pic_info_viewFlipper = (ViewFlipper) view.findViewById(R.id.pic_info_viewFlipper);
        this.name_textView = (TextView) view.findViewById(R.id.name_textView);
        this.tel_textView = (TextView) view.findViewById(R.id.tel_textView);
        this.address_textView = (TextView) view.findViewById(R.id.address_textView);
        this.comment_imageView = (ImageView) view.findViewById(R.id.comment_imageView);
        this.map = (FrameLayout) view.findViewById(R.id.map);
        this.filter = (FrameLayout) view.findViewById(R.id.filter);
        this.store_info_container = this;
        this.comment_container = (Container) view.findViewById(R.id.comment_container);

        ((MainActivity) context).setBackPress(this);
    }

    public void onDestroy() {
        ((CommentContainer)comment_container).onDestroy();
        this.removeView((View) comment_container);
        //comment_imageView.clearAnimation();
        pic_info_viewFlipper.clearAnimation();
        for(int i = 0; i < pic_info_viewFlipper.getChildCount(); i++)
            pic_info_viewFlipper.removeView(pic_info_viewFlipper.getChildAt(i));
        ((AppCompatActivity) context).getFragmentManager()
                .beginTransaction()
                .remove(mapFragment)
                .commit();
        mapFragment = null;
        storeInfo = null;
        context = null;
        container = null;
        this.removeAllViews();
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentById(R.id.content_frame))
                .commit();
        return true;
    }

    @Override
    public void showItem(Object object) {
        storeInfo = (StoreInfo) object;

        //  Load comment.
        ((CommentContainer)comment_container).loadComment(storeInfo);

        //  Set store pictures.
        for(int i = 0; i < storeInfo.getImage().size(); i++) {
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageAttr imageAttr = storeInfo.getImage().get(i);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(imageAttr.getImageUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                }

            });
            pic_info_viewFlipper.addView(imageView, i);
        }
        if(pic_info_viewFlipper.getChildCount() > 1) {
            pic_info_viewFlipper.setInAnimation(context, R.anim.slide_in_right);
            pic_info_viewFlipper.setOutAnimation(context, R.anim.slide_out_left);
            pic_info_viewFlipper.setFlipInterval(2000);
            pic_info_viewFlipper.showNext();
            pic_info_viewFlipper.startFlipping();
        }

        //  Set name.
        name_textView.setText(storeInfo.getName());
        //  Set address.
        address_textView.setText(storeInfo.getAddress(LANGUAGE_TW));
        //  Set tel.
        tel_textView.setText(storeInfo.getTel());

        //  Initialize Map.
        mapFragment = MapFragment.newInstance();
        ((AppCompatActivity) context).getFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
        /*mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if(storeInfo == null)
                    return;
                LatLng latLng = new LatLng(Double.valueOf(storeInfo.getLatitude()),
                        Double.valueOf(storeInfo.getLongitude()));
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng));
                //.title(storeList.get(index).getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                // Disable all gestures on map
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                // Disable marker click
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return true;
                    }
                });
            }
        });*/

        store_info_container.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        comment_imageView.setVisibility(View.VISIBLE);
                        comment_imageView.setAlpha(0f);
                        comment_imageView.requestLayout();
                        comment_imageView.animate().alpha(1).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if(comment_imageView != null)
                                    comment_imageView.setAlpha(1f);
                            }
                        }).start();
                        store_info_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        comment_imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommentContainer) comment_container).setPeekHeight((int) (getHeight() - (200 * context.getResources().getDisplayMetrics().density)));
                ((CommentContainer) comment_container).show();
                comment_imageView.setVisibility(GONE);
            }
        });
    }

    @Override
    public void loadingState(boolean state) {}

    @Override
    public Context getActivity() {
        return context;
    }
}
