package com.AndroidProject.taitiesyunbao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by JCChen on 2017/4/9.
 */

public class MenuInfoActivity extends AppCompatActivity {

    private ImageView img_cardView;
    private TextView price_cardView, info_cardView, toolbar_title;
    private Toolbar toolbar;
    private CardView cardView;
    private ItemInfo ItemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);

        Intent intent = getIntent();

        img_cardView = (ImageView) findViewById(R.id.img_cardView);
        price_cardView = (TextView) findViewById(R.id.price_cardView);
        info_cardView = (TextView) findViewById(R.id.info_cardView);
        toolbar = (Toolbar) findViewById(R.id.menu_activity_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        cardView = (CardView) findViewById(R.id.info_activ_cardView);

        ItemInfo = (ItemInfo) intent.getSerializableExtra("List");

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
        });
    }
}
