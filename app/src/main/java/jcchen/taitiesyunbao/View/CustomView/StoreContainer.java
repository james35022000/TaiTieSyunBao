package jcchen.taitiesyunbao.View.CustomView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.StoreInfo;
import jcchen.taitiesyunbao.View.Adapter.StoreRecyclerViewAdapter;


/**
 * Created by JCChen on 2017/8/8.
 */

public class StoreContainer extends FrameLayout implements Container {

    private List<StoreInfo> storeList;

    private RecyclerView store_recyclerView;
    private StoreRecyclerViewAdapter adapter;

    private StorePresenter presenter;

    private Context context;

    private int loadingCount;

    public StoreContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.presenter = new StorePresenter(this);
        this.loadingCount = 0;
        store_recyclerView = (RecyclerView) getChildAt(0);
        storeList = new ArrayList<>();
        adapter = new StoreRecyclerViewAdapter(context, storeList);
        store_recyclerView.setAdapter(adapter);
        store_recyclerView.setLayoutManager(new LinearLayoutManager(context));

        presenter.loadStoreInfo();
    }

    public void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        storeList = null;
        adapter = null;
        store_recyclerView.setAdapter(null);
        store_recyclerView.setLayoutManager(null);
        context = null;
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
        if(state)
            loadingCount++;
        else
            loadingCount--;

        if(loadingCount > 0)
            adapter.setLoadingState(true);
        else
            adapter.setLoadingState(false);
    }

}
