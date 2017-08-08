package jcchen.taitiesyunbao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JCChen on 2017/8/8.
 */

public class StoreContainer extends FrameLayout implements Container {
    private RecyclerView store_recyclerView;

    private List<StoreInfo> storeList;

    private Context context;

    private StoreRecyclerViewAdapter adapter;

    public StoreContainer (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        store_recyclerView = (RecyclerView) getChildAt(0);
        storeList = new ArrayList<>();
        adapter = new StoreRecyclerViewAdapter(context, storeList);
        store_recyclerView.setAdapter(adapter);
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
