package com.AndroidProject.taitiesyunbao;

/**
 * Created by JCChen on 2017/5/21.
 */

public class BuyInfo {
    private String name;
    private int amount, price;

    public BuyInfo(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

}
