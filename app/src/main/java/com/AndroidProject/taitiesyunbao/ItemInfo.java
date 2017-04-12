package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Items' information structure.
 *
 * Created by JCChen on 2017/4/9.
 */

public class ItemInfo implements Serializable {
    /*Not serializable */private Context context;

    private int img_drawableID;
    private int name_StringID;
    private int price;
    private int info_StringID;
    private int maxAmount;
    private int amount;
    private boolean like;

    // Constructor
    public ItemInfo(Context context, int img_drawableID, int name_StringID, int price,
                        int info_StringID, int maxAmount) {
        this.context = context;


        this.img_drawableID = img_drawableID;
        this.name_StringID = name_StringID;
        this.price = price;
        this.info_StringID = info_StringID;
        this.amount = 0;
        this.like = false;
        // Limit max amount to 5;
        maxAmount = maxAmount > 5? 5:maxAmount;
        this.maxAmount = maxAmount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getImageID() {
        return img_drawableID;
    }

    public Drawable getImage() {
        return context.getDrawable(img_drawableID);
    }

    public String getName() {
        return context.getResources().getString(name_StringID);
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getPrice() {
        return price;
    }

    public String getIntroduction() {
        return context.getResources().getString(info_StringID);
    }

    public void setLikeState(boolean state) {
        this.like = state;
    }

    public boolean getLikeState() {
        return like;
    }
}
