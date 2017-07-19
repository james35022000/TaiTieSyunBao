package jcchen.taitiesyunbao;


import android.support.v4.content.ContextCompat;

import java.util.Vector;

import static jcchen.taitiesyunbao.Constant.LANGUAGE_EN;

/**
 * Created by JCChen on 2017/7/15.
 */

public class StoreInfo {
    private String ID;
    private String Name;
    private String Address_tw, Address_en;
    private String Tel;
    private String Rate;
    private String Info;
    private String Latitude;
    private String Longitude;
    private Vector<String> Types;
    private String Station;
    private String Area;
    private Vector<String> ImageUrl;

    public StoreInfo(String ID, String Name, String Address_tw, String Address_en, String Tel, String Rate, String Info,
                     String Latitude, String Longitude, Vector<String> Types, String Station,
                     String Area, Vector<String> ImageUrl) {
        this.ID = ID;
        this.Name = Name;
        this.Address_tw = Address_tw;
        this.Address_en = Address_en;
        this.Tel = Tel;
        this.Info = Info;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Types = Types;
        this.Station = Station;
        this.Area = Area;
        this.ImageUrl = ImageUrl;
        this.Rate = Rate;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }

    public String getAddress(final int language) {
        if(language == LANGUAGE_EN)
            return this.Address_en;
        else
            return this.Address_tw;
    }

    public String getTel() {
        return this.Tel;
    }

    public String getRate() {
        if(Rate.equals("null"))
            Rate = "0";
        return this.Rate;
    }

    public String getInfo() {
        return this.Info;
    }

    public String getLatitude() {
        return this.Latitude;
    }

    public String getLongitude() {
        return this.Longitude;
    }

    public Vector<String> getTypes() {
        return this.Types;
    }

    public String getStation() {
        return this.Station;
    }

    public String getArea() {
        return this.Area;
    }

    public Vector<String> getImageUrl() {
        return this.ImageUrl;
    }
}
