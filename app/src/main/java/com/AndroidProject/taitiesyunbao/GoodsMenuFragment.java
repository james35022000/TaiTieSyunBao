package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Display goods' information using RecyclerView and CardView.
 * Created by JCChen on 2017/5/30.
 */

public class GoodsMenuFragment extends Fragment  {

    // MainActivity context.
    private Context context;

    // Each Interface.
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    private LikeFragment.OnLikeListener likeListener;

    // Using RecyclerView as a container to replace ListView.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    // Store foods information.
    public ArrayList<ItemInfo> goodList;

    private String Kind;

    // Initialize Interface so that I can communicate with MainActivity.
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        likeListener = (LikeFragment.OnLikeListener) context;
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_good_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.good_recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Kind = getArguments().getString("Kind");
        goodList = new ArrayList<>();

        getGoodsList();

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

    private void getGoodsList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Goods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("Kind").getValue().toString().equals(Kind)) {
                        goodList.add(new ItemInfo(context,
                                ds.child("ID").getValue().toString(),
                                ds.child("ImgurID").getValue().toString(),
                                ds.child("Name").getValue().toString(),
                                ds.child("Price").getValue().toString(),
                                ds.child("Amount").getValue().toString(),
                                ds.child("Info").getValue().toString()));
                    }
                }
                // Set adapter to display content.
                adapter = new MenuRecyclerViewAdapter(getActivity(), goodList, likeListener,
                        buyItemListListener);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
}
