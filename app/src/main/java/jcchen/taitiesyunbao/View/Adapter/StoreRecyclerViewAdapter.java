package jcchen.taitiesyunbao.View.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.Fragment.StoreInfoFragment;

/**
 * Created by JCChen on 2017/7/13.
 */

public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {

    private final int STORE_CARD = 0;
    private final int LOADING_CARD = 1;

    private boolean isStoreLoading = true;

    private Context context;
    private List<StoreInfo> storeList;

    public StoreRecyclerViewAdapter(Context context, List<StoreInfo> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    public void setLoadingState(boolean isStoreLoading) {
        this.isStoreLoading = isStoreLoading;
        notifyItemChanged(getItemCount() - 1);
    }

    @Override
    public StoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch(viewType) {
            case LOADING_CARD:
                return new StoreRecyclerViewAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.store_recyclerview_progressbar, viewGroup, false));
            case STORE_CARD:
                return new StoreRecyclerViewAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.store_cardview, viewGroup, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final StoreRecyclerViewAdapter.ViewHolder viewHolder, final int index) {
        switch(getItemViewType(index)) {
            case LOADING_CARD:
                if(isStoreLoading)
                    viewHolder.loading_linearLayout.setVisibility(View.VISIBLE);
                else
                    viewHolder.loading_linearLayout.setVisibility(View.GONE);
                break;
            case STORE_CARD:
                //  Set name.
                viewHolder.name_textView.setText(storeList.get(index).getName());
                //  Set rate.
                for (int i = 0; i < 5; i++) {
                    if (i < Double.valueOf(storeList.get(index).getRate()).intValue())
                        viewHolder.star_imageView[i].setSelected(true);
                    else
                        viewHolder.star_imageView[i].setSelected(false);
                }
                //  Set height of imageView.
                ViewTreeObserver viewTreeObserver = viewHolder.pic_viewPager.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup.LayoutParams layoutParams = viewHolder.pic_viewPager.getLayoutParams();
                        int viewPagerWidth = viewHolder.pic_viewPager.getWidth();
                        float viewPagerHeight = (float) (viewPagerWidth * 0.7);

                        layoutParams.width = viewPagerWidth;
                        layoutParams.height = (int) viewPagerHeight;

                        viewHolder.pic_viewPager.setLayoutParams(layoutParams);
                        viewHolder.pic_viewPager.requestLayout();
                        viewHolder.pic_viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                //  Set picture of imageView.
                viewHolder.pic_viewPager.setAdapter(
                        new StoreImagePagerAdapter(context, storeList.get(index).getImage()));
                //  Set picture resource(name & url).
                if (storeList.get(index).getImage().size() > 0) {
                    if (!storeList.get(index).getImage().get(0).getProvider().equals("")) {
                        viewHolder.photoby_textView.setVisibility(View.VISIBLE);
                        viewHolder.attr_textView.setText(Html.fromHtml("<u>" + storeList.get(index).getImage().get(0).getProvider() + "</u>"));
                    } else {
                        viewHolder.photoby_textView.setVisibility(View.INVISIBLE);
                        viewHolder.attr_textView.setText("");
                    }
                }
                //  Set slide dots.
                setDots(viewHolder, index);
                //  Set info onClickListener.
                viewHolder.info_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StoreInfoFragment StoreInfoFragment = new StoreInfoFragment();
                        StoreInfoFragment.setStoreInfo(storeList.get(index));
                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.content_frame, StoreInfoFragment)
                                .commit();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return storeList == null ? 1 : storeList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1)? LOADING_CARD: STORE_CARD;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView store_cardView;
        public TextView name_textView, photoby_textView, attr_textView;
        public ViewPager pic_viewPager;
        public ImageView info_imageView, like_imageView;
        public ImageView[] star_imageView = new ImageView[5];
        public LinearLayout sliderDots, loading_linearLayout;

        public ViewHolder(View v) {
            super(v);
            store_cardView = (CardView) v.findViewById(R.id.store_cardView);
            name_textView = (TextView) v.findViewById(R.id.name_textView);
            photoby_textView = (TextView) v.findViewById(R.id.photoby_textView);
            attr_textView = (TextView) v.findViewById(R.id.attr_textView);
            pic_viewPager = (ViewPager) v.findViewById(R.id.pic_viewPager);
            info_imageView = (ImageView) v.findViewById(R.id.info_imageView);
            like_imageView = (ImageView) v.findViewById(R.id.like_imageView);
            star_imageView[0] = (ImageView) v.findViewById(R.id.star0_imageView);
            star_imageView[1] = (ImageView) v.findViewById(R.id.star1_imageView);
            star_imageView[2] = (ImageView) v.findViewById(R.id.star2_imageView);
            star_imageView[3] = (ImageView) v.findViewById(R.id.star3_imageView);
            star_imageView[4] = (ImageView) v.findViewById(R.id.star4_imageView);
            sliderDots = (LinearLayout) v.findViewById(R.id.sliderDots);
            loading_linearLayout = (LinearLayout) v.findViewById(R.id.loading_linearLayout);
        }
    }

    private void setDots(final StoreRecyclerViewAdapter.ViewHolder viewHolder, final int index) {
        final int imageCount = storeList.get(index).getImage().size();
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
                    for (int i = 0; i < imageCount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(
                                context, R.drawable.nonactive_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(
                            context, R.drawable.active_dot));
                    if(!storeList.get(index).getImage().get(position).getProvider().equals("")) {
                        viewHolder.attr_textView.setText(Html.fromHtml(
                                "<u>" + storeList.get(index).getImage()
                                        .get(position).getProvider() + "</u>"));
                        viewHolder.photoby_textView.setVisibility(View.VISIBLE);
                    }
                    else {
                        viewHolder.attr_textView.setText("");
                        viewHolder.photoby_textView.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
}