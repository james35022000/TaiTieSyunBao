package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<CardStruct> list;
    private CardView cardView;
    private Spinner spinner;

    public MenuRecyclerViewAdapter(Context context, List<CardStruct> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_card_view, viewGroup, false);

        cardView = (CardView) v.findViewById(R.id.menu_cardView);
        spinner= (Spinner) v.findViewById(R.id.amount_spinner);


        final ImageView imageView = (ImageView) v.findViewById(R.id.like_imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageView.isSelected()) {
                    imageView.setSelected(false);
                }
                else {
                    imageView.setSelected(true);
                }
            }
        });

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        viewHolder.img_cardView.setImageResource(list.get(index).getImageID(context));
        viewHolder.name_cardView.setText(list.get(index).getName(context));
        viewHolder.price_cardView.setText(list.get(index).getPrice(context) + "元");
        ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(context
                , R.layout.spinner_center_item, list.get(index).getAmountList(context));
        amountAdapter.setDropDownViewResource(R.layout.spinner_center_item);
        viewHolder.amount_cardView.setAdapter(amountAdapter);

        initListener(index);

        // Set MarginBottom of the last card in order not to cover the Like bottom.
        if(index == list.size()-1) {
            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
            ViewGroup.MarginLayoutParams marginLayoutParams =
                                                (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins(0, 0, 0, 190);
            cardView.setLayoutParams(marginLayoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_cardView;
        public TextView name_cardView, price_cardView;
        public Spinner amount_cardView;

        public ViewHolder(View view) {
            super(view);
            img_cardView = (ImageView) view.findViewById(R.id.pic_imageView);
            name_cardView = (TextView) view.findViewById(R.id.name_textView);
            price_cardView = (TextView) view.findViewById(R.id.price_textView);
            amount_cardView = (Spinner) view.findViewById(R.id.amount_spinner);
        }
    }

    private void initListener(int index) {
        final int j = index;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuInfoActivity.class);
                intent.putExtra("List", list.get(j));
                context.startActivity(intent);
            }
        };
        cardView.setOnClickListener(clickListener);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                list.get(j).setAmount(position);
                
            }
            public void onNothingSelected(AdapterView arg0) {
                // Do nothing
            }
        });
    }
}
