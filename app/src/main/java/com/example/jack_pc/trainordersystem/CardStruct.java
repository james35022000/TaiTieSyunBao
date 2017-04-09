package com.example.jack_pc.trainordersystem;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jack-PC on 2017/4/9.
 */

public class CardStruct implements Serializable {
    private int img_drawableID;
    private int name_StringID;
    private int price;
    private int info_StringID;
    private ArrayList<String> amountList;
    private int amount;

    // Default constructor
    public CardStruct(int img_drawableID, int name_StringID, int price,
                        int info_StringID, int maxAmount) {

        this.amountList = new ArrayList<>();

        this.img_drawableID = img_drawableID;
        this.name_StringID = name_StringID;
        this.price = price;
        this.info_StringID = info_StringID;
        this.amount = 0;

        maxAmount = maxAmount > 5? 5:maxAmount;
        for(int i = 0; i <= maxAmount; i++) {
            this.amountList.add(String.valueOf(i));
        }
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getImageID(Context context) {
        return img_drawableID;
    }

    public String getName(Context context) {
        return context.getResources().getString(name_StringID);
    }

    public int getPrice(Context context) {
        return price;
    }

    public String getInfo(Context context) {
        return context.getResources().getString(info_StringID);
    }

    public ArrayList<String> getAmountList(Context context) {
        return amountList;
    }

    public void getAmount() {

    }

}
