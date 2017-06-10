package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JCChen on 2017/4/7.
 */

public class SugFragment extends Fragment {
    private ImageView star1, star2, star3, star4, star5, send_imageView;
    private EditText feedBack_editText;
    private int score;
    private Context context;

    public interface FeedBack {
        void sendFeedBack(int score, String opinion);
    }

    FeedBack feedBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.feedBack = (FeedBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sug_frg_layout, container, false);

        star1 = (ImageView) view.findViewById(R.id.imgbtn001);
        star1.setSelected(true);
        star2 = (ImageView) view.findViewById(R.id.imgbtn002);
        star3 = (ImageView) view.findViewById(R.id.imgbtn003);
        star4 = (ImageView) view.findViewById(R.id.imgbtn004);
        star5 = (ImageView) view.findViewById(R.id.imgbtn005);
        send_imageView = (ImageView) view.findViewById(R.id.send_imageView);
        feedBack_editText = (EditText) view.findViewById(R.id.feedBack_editText);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        score = 1;
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
                score = 1;
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
                score = 2;
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
                score = 3;
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
                score = 4;
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
                score = 5;
                star1.setSelected(true);
                star2.setSelected(true);
                star3.setSelected(true);
                star4.setSelected(true);
                star5.setSelected(true);
            }
        });
        send_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!feedBack_editText.getText().toString().equals("")) {
                    feedBack.sendFeedBack(score, feedBack_editText.getText().toString());
                    feedBack_editText.setText("");
                    Toast.makeText(context, "謝謝您的寶貴意見!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
