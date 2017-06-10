package com.AndroidProject.taitiesyunbao;

/**
 * Items' information structure.
 *
 * Created by JCChen on 2017/4/9.
 */

public class ItemInfo {

    private int id;
    private String imgurID;
    private String name;
    private int price;
    private String info;
    private int maxAmount;
    private int amount;
    private boolean like;
    private String Kind;

    // Constructor
    public ItemInfo(String id, String Kind, String imgurID, String name, String price
                    , String maxAmount, String info, boolean like) {

        this.id = Integer.valueOf(id);
        this.Kind = Kind;
        this.imgurID = imgurID;
        this.name = name;
        this.price = Integer.parseInt(price);
        this.amount = 0;
        this.like = false;
        this.maxAmount = Integer.parseInt(maxAmount);
        this.info = info;
        this.like = like;
    }

    // Constructor (Using for comparing two ItemInfo)
    public ItemInfo(String id) {
        this.id = Integer.valueOf(id);
    }

    public ItemInfo(String id, int amount) {
        this.id = Integer.valueOf(id);
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getImgurID() {
        return imgurID;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getPrice() {
        return price;
    }

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

    public String getKind() {
        return Kind;
    }
}
