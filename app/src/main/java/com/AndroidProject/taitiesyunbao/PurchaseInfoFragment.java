package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Show purchase information.
 * Created by JCChen on 2017/4/11.
 */

public class PurchaseInfoFragment extends Fragment {

    // Access buyList from MainActivity.
    private MenuFragment.OnBuyItemListListener buyItemListListener;
    // Declare layout items.
    private ImageView back_imageView, next_imageView;
    private ListView list_listView;
    private TextView total_textView;
    private FloatingActionButton floatingActionButton;

    private PurchaseInfoArrayAdapter buyList_arrayAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Initialize.
        buyItemListListener = (MenuFragment.OnBuyItemListListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_info_layout, container, false);
        // Initialize.
        back_imageView = (ImageView) view.findViewById(R.id.back_imageView);
        next_imageView = (ImageView) view.findViewById(R.id.next_imageView);
        list_listView = (ListView) view.findViewById(R.id.list_listView);
        total_textView = (TextView) view.findViewById(R.id.total_textView);
        floatingActionButton = (FloatingActionButton) getParentFragment()
                                                    .getView()
                                                    .findViewById(R.id.menu_floatingActionButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Vector<ItemInfo> buyList = buyItemListListener.getBuyList();
        int total = 0, height = 0;

        buyList_arrayAdapter = new PurchaseInfoArrayAdapter(getActivity(),
                            R.layout.listview_text_layout, new Vector<BuyInfo>());

        for(int i = 0; i < buyList.size(); i++) {
            buyList_arrayAdapter.add(new BuyInfo(buyList.get(i).getName(),
                    buyList.get(i).getAmount(), buyList.get(i).getPrice()));
            total += buyList.get(i).getAmount()*buyList.get(i).getPrice();
            View v = buyList_arrayAdapter.getView(i, null, list_listView);
            v.measure(0, 0);
            height += v.getMeasuredHeight();
        }

        list_listView.setAdapter(buyList_arrayAdapter);
        ViewGroup.LayoutParams params = list_listView.getLayoutParams();
        params.height = height + (list_listView.getDividerHeight() *
                                        (buyList_arrayAdapter.getCount() - 1));
        list_listView.setLayoutParams(params);

        total_textView.setText(Integer.toString(total));

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

        next_imageView.setOnClickListener(new View.OnClickListener() {
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
