package jcchen.taitiesyunbao.View.Container;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.Presenter.impl.StorePresenterImpl;
import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.Entity.StoreComment;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.Adapter.StoreCommentRecyclerViewAdapter;
import jcchen.taitiesyunbao.View.Widget.BottomSheetRV.BottomSheet;
import jcchen.taitiesyunbao.View.Widget.BottomSheetRV.BottomSheetRV;

/**
 * Created by JCChen on 2017/8/12.
 */

public class CommentContainer extends BottomSheetRV implements Container {

    private Context context;

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
        presenter = null;
        this.removeAllViews();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter = new StorePresenterImpl(this);
        commentList = new ArrayList<>();
        commentList.add(null);
        setAdapter(new StoreCommentRecyclerViewAdapter(context, commentList));
    }

    @Override
    public boolean onBackPressed() {
        presenter.cancelStoreComment();
        return true;
    }

    @Override
    public void showItem(Object object) {
        commentList.add((StoreComment) object);
        notifyItemChanged(commentList.size());
    }

    @Override
    public void loadingState(boolean state) {

    }

    @Override
    public Context getActivity() {
        return context;
    }

}
