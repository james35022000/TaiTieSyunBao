package jcchen.taitiesyunbao.View.CustomView;


import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.StoreComment;
import jcchen.taitiesyunbao.StoreInfo;
import jcchen.taitiesyunbao.View.MainActivity;
import jcchen.taitiesyunbao.View.Adapter.StoreCommentRecyclerViewAdapter;

/**
 * Created by JCChen on 2017/8/12.
 */

public class CommentContainer extends FrameLayout implements Container {

    private Context context;

    private RecyclerView comment_recyclerView;
    private BottomSheetBehavior bottomSheetBehavior;
    private StoreCommentRecyclerViewAdapter adapter;
    private List<StoreComment> commentList;

    private StorePresenter presenter;

    public CommentContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    public void loadComment(StoreInfo storeInfo) {
        presenter.loadStoreComment(storeInfo, 0);
    }

    public void onDestroy() {
        context = null;
        commentList = null;
        presenter.onDestroy();
        presenter = null;
        adapter = null;
        comment_recyclerView.setAdapter(null);
        comment_recyclerView.setLayoutManager(null);
        this.removeAllViews();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.presenter = new StorePresenter(this);
        this.comment_recyclerView = (RecyclerView) getChildAt(0);
        commentList = new ArrayList<>();
        commentList.add(null);
        adapter = new StoreCommentRecyclerViewAdapter(context, commentList);
        comment_recyclerView.setAdapter(adapter);
        comment_recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public boolean onBackPressed() {
        bottomSheetBehavior = BottomSheetBehavior.from(this.getRootView().findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        return true;
    }

    @Override
    public void showItem(Object object) {
        commentList.add((StoreComment) object);
        adapter.notifyItemChanged(commentList.size());
    }

    @Override
    public void loadingState(boolean state) {

    }
}
