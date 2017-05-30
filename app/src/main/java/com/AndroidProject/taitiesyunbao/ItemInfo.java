package com.AndroidProject.taitiesyunbao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Items' information structure.
 *
 * Created by JCChen on 2017/4/9.
 */

public class ItemInfo implements Serializable {
    /*Not serializable */private Context context;

    //private int img_drawableID;
    private int id;
    private String imgurID;
    //private int name_StringID;
    private String name;
    private int price;
    //private int info_StringID;
    private String info;
    private int maxAmount;
    private int amount;
    private boolean like;

    // Constructor
    // public ItemInfo(Context context, int img_drawableID, int name_StringID, int price,
    //                    int info_StringID, int maxAmount) {
    public ItemInfo(Context context, String id, String imgurID, String name, String price
                    , String maxAmount, String info) {
        this.context = context;

        this.id = Integer.valueOf(id);
        this.imgurID = imgurID;
        this.name = name;
        //this.img_drawableID = img_drawableID;
        //this.name_StringID = name_StringID;
        this.price = Integer.parseInt(price);
        //this.info_StringID = info_StringID;
        this.amount = 0;
        this.like = false;
        this.maxAmount = Integer.parseInt(maxAmount);
        this.info = info;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    //public int getImageID() {
    //    return img_drawableID;
    //}

    //public Drawable getImage() {
    //    return context.getDrawable(img_drawableID);
    //}

    public String getImgurID() {
        return imgurID;
    }

    //public String getName() {
    //    return context.getResources().getString(name_StringID);
    //}

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getPrice() {
        return price;
    }

    //public String getIntroduction() {
    //    return context.getResources().getString(info_StringID);
    //}

    public String getInfo() {
        return info;
    }

    public int getId() {
        return id;
    }

    public void setLikeState(boolean state) {
        this.like = state;
    }

    public boolean getLikeState() {
        return like;
    }
}
