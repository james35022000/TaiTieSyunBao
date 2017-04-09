package com.example.jack_pc.trainordersystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class MenuInfoActivity extends AppCompatActivity {

    private ImageView img_cardView;
    private TextView name_cardView, price_cardView, info_cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);

        img_cardView = (ImageView) findViewById(R.id.img_cardView);
        name_cardView = (TextView) findViewById(R.id.name_cardView);
        price_cardView = (TextView) findViewById(R.id.price_cardView);
        info_cardView = (TextView) findViewById(R.id.info_cardView);
    }
}
