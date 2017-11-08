package jcchen.taitiesyunbao.View.Container;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.Presenter.impl.StorePresenterImpl;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.Entity.StoreComment;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.Adapter.StoreCommentRecyclerViewAdapter;
import jcchen.taitiesyunbao.View.Widget.BottomSheet;

/**
 * Created by JCChen on 2017/8/12.
 */

public class CommentContainer extends RelativeLayout implements Container {

    private Context context;

    private RecyclerView comment_recyclerView;
    private StoreCommentRecyclerViewAdapter adapter;
    private List<StoreComment> commentList;
    private BottomSheet comment_bottomSheet;

    private int peekHeight;

    private StorePresenter presenter;

    public CommentContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.peekHeight = -1;
        setClipChildren(false);
    }

    public void loadComment(StoreInfo storeInfo) {
        presenter.loadStoreComment(storeInfo, 0);
    }

    public void onDestroy() {
        context = null;
        commentList = null;
        presenter = null;
        adapter = null;
        comment_recyclerView.setAdapter(null);
        comment_recyclerView.setLayoutManager(null);
        this.removeAllViews();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.presenter = new StorePresenterImpl(this);
        this.comment_recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerView);
        this.comment_bottomSheet = (BottomSheet) findViewById(R.id.comment_bottomSheet);
        commentList = new ArrayList<>();
        commentList.add(null);
        adapter = new StoreCommentRecyclerViewAdapter(context, commentList);
        //comment_recyclerView.setAdapter(adapter);
        comment_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        comment_recyclerView.setVisibility(GONE);
    }

    public void show() {
        if(this.peekHeight > 0) {
            comment_bottomSheet.setPeekHeight(peekHeight);
            comment_bottomSheet.addAnimationListener(new BottomSheet.onAnimationListener() {
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
            comment_bottomSheet.show();
        }
        else {
            Log.e("CommentContainer.show()", "Set PeekHeight before show.");
        }
    }

    /**
     * Show the content of BottomSheet after finishing showing animation.
     *
     * Suggest: Content show animation time should correspond to BottomSheet overShooting animation
     * (ref. BottomSheet.OverShoot()) to show better results.
     */
    private void contentShow() {
        adapter.setAnimationState(true);
        comment_recyclerView.setAdapter(adapter);
        comment_recyclerView.setVisibility(VISIBLE);
        comment_recyclerView.scheduleLayoutAnimation();
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
    }

    @Override
    public boolean onBackPressed() {
        presenter.cancelStoreComment();
        return true;
    }

    @Override
    public void showItem(Object object) {
        commentList.add((StoreComment) object);
        adapter.setAnimationState(false);
        adapter.notifyItemChanged(commentList.size());
    }

    @Override
    public void loadingState(boolean state) {

    }

    @Override
    public Context getActivity() {
        return context;
    }

}
