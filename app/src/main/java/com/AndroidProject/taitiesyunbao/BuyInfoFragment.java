package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by JCChen on 2017/4/11.
 */

public class BuyInfoFragment extends Fragment {

    private BuyRecyclerViewAdapter adapter;

    private MenuFragment.OnBuyItemListListener buyItemListListener;

    private ImageView back_imageView, fore_imageView;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_info_layout, container, false);

        back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
        fore_imageView = (ImageView) view.findViewById(R.id.fore_imageView);
        recyclerView = (RecyclerView) view.findViewById(R.id.buy_recyclerView);
        floatingActionButton = (FloatingActionButton) getParentFragment().getView().findViewById(R.id.menu_floatingActionButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new BuyRecyclerViewAdapter(getActivity(), buyItemListListener.getBuyList());
        recyclerView.setAdapter(adapter);

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                               .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                               .remove(fragmentManager.findFragmentById(R.id.menu_fragment))
                               .commit();
                floatingActionButton.setVisibility(View.VISIBLE);

            }
        });

        fore_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
}
