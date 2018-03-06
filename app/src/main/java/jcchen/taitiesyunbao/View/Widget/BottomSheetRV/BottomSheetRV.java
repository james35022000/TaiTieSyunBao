package jcchen.taitiesyunbao.View.Widget.BottomSheetRV;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;


/**
 * Created by JCChen on 2018/2/11.
 */

public class BottomSheetRV extends RelativeLayout {
    private Context context;

    private BottomSheet bottomSheet;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private int peekHeight;

    public BottomSheetRV(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomSheet = new BottomSheet(context);
        addView(bottomSheet, params);

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.OVER_SCROLL_NEVER);
        recyclerView = new RecyclerView(context);
        recyclerView.setClipChildren(false);
        addView(recyclerView, params);

        adapter = null;
        peekHeight = -1;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setVisibility(GONE);
    }

    public void show() {
        if(peekHeight > 0) {
            // show bottomSheet
            bottomSheet.addAnimationListener(new BottomSheet.onAnimationListener() {
                @Override
                public void onShowAnimationStart() {
                    setClipChildren(false);
                }
                @Override
                public void onShowAnimationEnd() {
                    // show content.
                    contentShow();
                }
                @Override
                public void onOverShootAnimationEnd() {
                    setClipChildren(true);
                }
            });
            getLayoutParams().height = peekHeight;
            invalidate();
            bottomSheet.show();
        }
        else {
            Log.e("BottomSheetRV.show()", "Set PeekHeight before show.");
        }
    }

    public void dismiss() {

    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
        bottomSheet.setPeekHeight(peekHeight);
    }

    private void contentShow() {
        if(adapter != null) {
            adapter.setAnimationState(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(VISIBLE);
            recyclerView.scheduleLayoutAnimation();
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    adapter.setAnimationState(false);
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public void notifyItemChanged(int position) {
        adapter.setAnimationState(false);
        adapter.notifyItemChanged(position);
    }
}
