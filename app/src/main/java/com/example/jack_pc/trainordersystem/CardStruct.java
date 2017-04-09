package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class CardStruct {
    public int img_drawableID;
    public int name_StringID;
    public int price;
    public int info_StringID;
    public ArrayAdapter<String> amountList;

    // Default constructor
    public CardStruct(Context context, int img_drawableID, int name_StringID, int price,
                        int info_StringID, int maxAmount) {
        this.img_drawableID = img_drawableID;
        this.name_StringID = name_StringID;
        this.price = price;
        this.info_StringID = info_StringID;

        ArrayList<String> amount = new ArrayList<>();
        maxAmount = maxAmount > 5? 5:maxAmount;
        for(int i = 0; i <= maxAmount; i++) {
            amount.add(String.valueOf(i));
        }
        this.amountList = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                                            amount);
    }

}
