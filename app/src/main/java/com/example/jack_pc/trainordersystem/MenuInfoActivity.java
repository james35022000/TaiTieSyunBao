package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class MenuInfoActivity extends AppCompatActivity {

    private ImageView img_cardView;
    private TextView price_cardView, info_cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);

        Intent intent = getIntent();


        img_cardView = (ImageView) findViewById(R.id.img_cardView);
        price_cardView = (TextView) findViewById(R.id.price_cardView);
        info_cardView = (TextView) findViewById(R.id.info_cardView);

        CardStruct cardStruct = (CardStruct) intent.getSerializableExtra("List");
        img_cardView.setImageResource(cardStruct.getImageID(this));
        price_cardView.setText(cardStruct.getPrice(this) + "元");
        info_cardView.setText(cardStruct.getInfo(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_activity_toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(cardStruct.getName(this));
        toolbar.setNavigationIcon(R.drawable.remove_symbol);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        CardView cardView = (CardView) findViewById(R.id.info_activ_cardView);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        cardView.setOnClickListener(clickListener);
    }
}
