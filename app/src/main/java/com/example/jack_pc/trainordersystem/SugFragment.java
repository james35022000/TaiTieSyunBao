package com.example.jack_pc.trainordersystem;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Jack-PC on 2017/4/7.
 */

public class SugFragment extends Fragment {
    private ImageView star1, star2, star3, star4, star5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sug_frg_layout, container, false);

        star1 = (ImageView) view.findViewById(R.id.imgbtn001);
        star2 = (ImageView) view.findViewById(R.id.imgbtn002);
        star3 = (ImageView) view.findViewById(R.id.imgbtn003);
        star4 = (ImageView) view.findViewById(R.id.imgbtn004);
        star5 = (ImageView) view.findViewById(R.id.imgbtn005);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
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

    private void initListener() {
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star2.setSelected(false);
                star3.setSelected(false);
                star4.setSelected(false);
                star5.setSelected(false);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star2.setSelected(true);
                star3.setSelected(false);
                star4.setSelected(false);
                star5.setSelected(false);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star2.setSelected(true);
                star3.setSelected(true);
                star4.setSelected(false);
                star5.setSelected(false);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star2.setSelected(true);
                star3.setSelected(true);
                star4.setSelected(true);
                star5.setSelected(false);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setSelected(true);
                star2.setSelected(true);
                star3.setSelected(true);
                star4.setSelected(true);
                star5.setSelected(true);
            }
        });
    }
}
