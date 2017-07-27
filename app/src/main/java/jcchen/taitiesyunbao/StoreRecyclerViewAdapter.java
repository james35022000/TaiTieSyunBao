package jcchen.taitiesyunbao;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import static jcchen.taitiesyunbao.Constant.LANGUAGE_TW;

/**
 * Created by JCChen on 2017/7/13.
 */

public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private Vector<StoreInfo> storeList;
    private RecyclerView store_recyclerView, comment_recyclerView;
    private ViewPager pic_info_viewPager;
    private FrameLayout store_info_frame;
    private TextView name_textView, tel_textView, address_textView;
    private ImageView comment_imageView;
    private BottomSheetBehavior bottomSheetBehavior;

    public StoreRecyclerViewAdapter(Context context, Vector<StoreInfo> storeList, View view) {
        this.context = context;
        this.storeList = storeList;
        this.store_recyclerView = (RecyclerView) view.findViewById(R.id.store_recyclerView);
        this.pic_info_viewPager = (ViewPager) view.findViewById(R.id.pic_info_viewPager);
        this.store_info_frame = (FrameLayout) view.findViewById(R.id.store_info_frame);
        this.name_textView = (TextView) view.findViewById(R.id.name_textView);
        this.tel_textView = (TextView) view.findViewById(R.id.tel_textView);
        this.address_textView = (TextView) view.findViewById(R.id.address_textView);
        this.comment_imageView = (ImageView) view.findViewById(R.id.comment_imageView);
        this.bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        this.comment_recyclerView = (RecyclerView) view.findViewById(R.id.comment_recyclerView);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.store_cardview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        viewHolder.name_textView.setText(storeList.get(index).getName());
        /*viewHolder.address_textView.setText(context.getResources().getString(R.string.address) +
                " : " + storeList.get(index).getAddress(LANGUAGE_TW));
        viewHolder.tel_textView.setText(context.getResources().getString(R.string.tel) +
                " : " + storeList.get(index).getTel());*/
        viewHolder.pic_viewPager.setAdapter(
                new StoreImagePagerAdapter(context, storeList.get(index).getImageUrl()));
        for (int i = 0; i < 5; i++) {
            if (i < Double.valueOf(storeList.get(index).getRate()).intValue())
                viewHolder.star_imageView[i].setSelected(true);
            else
                viewHolder.star_imageView[i].setSelected(false);
        }
        setDots(viewHolder, index);

        viewHolder.info_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInfoDisplay(viewHolder, index);
                setCommentView(index);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeList == null ? 0 : storeList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView store_cardView;
        public TextView name_textView, address_textView, tel_textView;
        public ViewPager pic_viewPager;
        public ImageView info_imageView, like_imageView;
        public ImageView[] star_imageView = new ImageView[5];
        public LinearLayout sliderDots;

        public ViewHolder(View v) {
            super(v);
            store_cardView = (CardView) v.findViewById(R.id.store_cardView);
            name_textView = (TextView) v.findViewById(R.id.name_textView);
            //address_textView = (TextView) v.findViewById(R.id.address_textView);
            //tel_textView = (TextView) v.findViewById(R.id.tel_textView);
            pic_viewPager = (ViewPager) v.findViewById(R.id.pic_viewPager);
            info_imageView = (ImageView) v.findViewById(R.id.info_imageView);
            like_imageView = (ImageView) v.findViewById(R.id.like_imageView);
            star_imageView[0] = (ImageView) v.findViewById(R.id.star0_imageView);
            star_imageView[1] = (ImageView) v.findViewById(R.id.star1_imageView);
            star_imageView[2] = (ImageView) v.findViewById(R.id.star2_imageView);
            star_imageView[3] = (ImageView) v.findViewById(R.id.star3_imageView);
            star_imageView[4] = (ImageView) v.findViewById(R.id.star4_imageView);
            sliderDots = (LinearLayout) v.findViewById(R.id.sliderDots);
        }
    }

    private void setDots(ViewHolder viewHolder, int index) {
        final int imageCount = storeList.get(index).getImageUrl().size();
        viewHolder.sliderDots.removeAllViews();
        if (imageCount > 1) {
            viewHolder.pic_viewPager.clearOnPageChangeListeners();
            final ImageView[] dots = new ImageView[imageCount];
            for (int i = 0; i < imageCount; i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                dots[i] = new ImageView(context);
                dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nonactive_dot));
                viewHolder.sliderDots.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active_dot));
            viewHolder.pic_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Log.i("ImageCount", String.valueOf(imageCount));
                    for (int i = 0; i < imageCount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(
                                context, R.drawable.nonactive_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(
                            context, R.drawable.active_dot));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void setInfoDisplay(final ViewHolder viewHolder, final int index) {
        store_recyclerView.setVisibility(View.GONE);
        store_info_frame.setVisibility(View.VISIBLE);

        pic_info_viewPager.setAdapter(viewHolder.pic_viewPager.getAdapter());

        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                ((Activity) context).getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(Double.valueOf(storeList.get(index).getLatitude()),
                        Double.valueOf(storeList.get(index).getLongitude()));
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

        name_textView.setText(storeList.get(index).getName());
        if(!storeList.get(index).getTel().equals("null"))
            tel_textView.setText(context.getResources().getString(R.string.tel) + ": " +
                                        storeList.get(index).getTel());
        address_textView.setText(context.getResources().getString(R.string.address) + ": " +
                                    storeList.get(index).getAddress(LANGUAGE_TW));


    }

    private void setCommentView(int index) {
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
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

        Vector<StoreComment> commentList = new Vector<>();
        RecyclerView.Adapter adapter = new StoreCommentRecyclerViewAdapter(context, commentList);
        new GetStoreComment(adapter, commentList, storeList.get(index)).execute("0");
        comment_recyclerView.setAdapter(adapter);
        comment_recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

}