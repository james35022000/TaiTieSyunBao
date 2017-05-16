package com.AndroidProject.taitiesyunbao;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 * Created by JCChen on 2017/5/16.
 */

public class MenuInfoFragment extends Fragment {

    private ImageView img_cardView;
    private TextView price_cardView, info_cardView, toolbar_title;
    private Toolbar toolbar;
    private CardView cardView;
    private ItemInfo ItemInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_info_frg, container, false);
        // Initialize.
        img_cardView = (ImageView) view.findViewById(R.id.img_cardView);
        price_cardView = (TextView) view.findViewById(R.id.price_cardView);
        info_cardView = (TextView) view.findViewById(R.id.info_cardView);
        toolbar = (Toolbar) view.findViewById(R.id.menu_activity_toolbar);
        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        cardView = (CardView) view.findViewById(R.id.info_activ_cardView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       /* ItemInfo = (ItemInfo) intent.getSerializableExtra("List");

        img_cardView.setImageDrawable(ItemInfo.getImage());
        price_cardView.setText(ItemInfo.getPrice() + "元");
        info_cardView.setText(ItemInfo.getIntroduction());
        toolbar_title.setText(ItemInfo.getName());
        toolbar.setNavigationIcon(R.drawable.close_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
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
