package com.example.jack_pc.trainordersystem;

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
 * Created by Jack-PC on 2017/4/11.
 */

public class BuyInfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private BuyRecyclerViewAdapter adapter;
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private ImageView back_imageView;
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
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.menu_floatingActionButton);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.buy_recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BuyRecyclerViewAdapter(getActivity(), buyItemListListener.getList());
        recyclerView.setAdapter(adapter);

        back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuFragment menuFragment = new MenuFragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.buy_info_fragment, menuFragment).commit();

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