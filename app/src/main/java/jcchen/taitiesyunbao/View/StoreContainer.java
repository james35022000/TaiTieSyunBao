package jcchen.taitiesyunbao.View;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.StoreInfo;


/**
 * Created by JCChen on 2017/8/8.
 */

public class StoreContainer extends FrameLayout implements Container {

    private List<StoreInfo> storeList;

    private RecyclerView store_recyclerView;
    private StoreRecyclerViewAdapter adapter;

    private StorePresenter presenter;

    private StoreContainer container;
    private Context context;

    public StoreContainer (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.container = this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.presenter = new StorePresenter(this);
        store_recyclerView = (RecyclerView) getChildAt(0);
        storeList = new ArrayList<>();
        adapter = new StoreRecyclerViewAdapter(context, storeList);
        store_recyclerView.setAdapter(adapter);
        store_recyclerView.setLayoutManager(new LinearLayoutManager(context));

        presenter.loadStoreInfo();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void showItem(Object object) {
        storeList.add((StoreInfo) object);
        adapter.notifyItemChanged(storeList.size() - 1);
        adapter.notifyItemChanged(storeList.size());
    }

    @Override
    public void loadingState(boolean state) {
        adapter.setLoadingState(state);
    }

}
