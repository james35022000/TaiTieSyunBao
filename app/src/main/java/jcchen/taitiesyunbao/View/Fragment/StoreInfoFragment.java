package jcchen.taitiesyunbao.View.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jcchen.taitiesyunbao.R;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.CustomView.StoreInfoContainer;

/**
 * Created by JCChen on 2017/8/13.
 */

public class StoreInfoFragment extends Fragment {

    private StoreInfo storeInfo;

    private View view;

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_info_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((StoreInfoContainer) view.findViewById(R.id.store_info_container)).showItem(storeInfo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        storeInfo = null;
        ((StoreInfoContainer) view.findViewById(R.id.store_info_container)).onDestroy();
        if(view.getParent() != null)
            ((ViewGroup) view).removeView(view.findViewById(R.id.store_info_container));
        view = null;
    }
}
