package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

/**
 * Created by JCChen on 2017/4/7.
 */

public class LikeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LikeRecyclerViewAdapter adapter;
    private MenuRecyclerViewAdapter.SaveUserData saveUserData;
    private MenuFragment.OnBuyItemListListener buyItemListListener;

    public interface OnLikeListener {
        Vector<ItemInfo> getLikeList();
        void setLikeList(Vector<ItemInfo> list);
        void addLikeList(ItemInfo ItemInfo);
        boolean delLikeList(ItemInfo ItemInfo);
        int isLikeItemExist(ItemInfo ItemInfo);
    }

    OnLikeListener likeListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        likeListener = (OnLikeListener) context;
        saveUserData = (MenuRecyclerViewAdapter.SaveUserData) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.like_frg_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.like_recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if(adapter == null) {
            adapter = new LikeRecyclerViewAdapter(getActivity(), likeListener, saveUserData,
                                                            buyItemListListener);
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && (recyclerView != null)) {
            adapter = new LikeRecyclerViewAdapter(getActivity(), likeListener, saveUserData,
                                                        buyItemListListener);
            recyclerView.setAdapter(adapter);
        }
    }

}
