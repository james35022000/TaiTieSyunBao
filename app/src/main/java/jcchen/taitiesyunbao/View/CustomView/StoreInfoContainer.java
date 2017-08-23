package jcchen.taitiesyunbao.View.CustomView;

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

import jcchen.taitiesyunbao.ImageAttr;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.StoreInfo;
import jcchen.taitiesyunbao.View.MainActivity;

import static jcchen.taitiesyunbao.Constant.LANGUAGE_TW;

/**
 * Created by JCChen on 2017/8/12.
 */

public class StoreInfoContainer extends FrameLayout implements Container {

    private ViewFlipper pic_info_viewFlipper;
    private TextView name_textView, tel_textView, address_textView;
    private ImageView comment_imageView;
    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottom_sheet;
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
        this.bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        this.bottom_sheet = (NestedScrollView) view.findViewById(R.id.bottom_sheet);
        this.map = (FrameLayout) view.findViewById(R.id.map);
        this.filter = (FrameLayout) view.findViewById(R.id.filter);
        this.store_info_container = (FrameLayout) view.findViewById(R.id.store_info_container);
        this.comment_container = (Container) view.findViewById(R.id.comment_container);

        ((MainActivity) context).setBackPress(this);
    }

    public void onDestroy() {
        ((CommentContainer)comment_container).onDestroy();
        this.removeView((View) comment_container);
        comment_imageView.clearAnimation();
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
        mapFragment.getMapAsync(new OnMapReadyCallback() {
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
        });

        // Hide bottom sheet initially.
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch(newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        ((MainActivity) context).popBackPress(comment_container);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        ((MainActivity) context).setBackPress(comment_container);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        ((MainActivity) context).setBackPress(comment_container);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // Set bottom sheet peek height and height.
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
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) bottom_sheet.getLayoutParams();
                        params.height = store_info_container.getHeight();
                        bottom_sheet.setLayoutParams(params);
                        bottomSheetBehavior.setPeekHeight((int)(store_info_container.getHeight() - (200 * context.getResources().getDisplayMetrics().density)));
                        store_info_container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
        // Control bottom sheet by sliding comment_imageView.
        comment_imageView.setOnTouchListener(new View.OnTouchListener() {
            int last_y = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last_y = (int) motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int y = (int) motionEvent.getRawY() - last_y;
                        if(y < 0) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        else if(y > 0) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                        last_y = (int) motionEvent.getRawY();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void loadingState(boolean state) {}

    @Override
    public Context getMainThread() {
        return context;
    }
}
