package jcchen.taitiesyunbao.Model;


import com.google.android.gms.maps.model.LatLng;

import jcchen.taitiesyunbao.Entity.StoreInfo;

/**
 * Created by JCChen on 2017/8/10.
 */

public interface StoreModel {

    void getStoreInfo(LatLng latLng);

    void getStoreInfo(String place_name);

    void getStoreInfo(StoreInfo storeInfo);

    void cancelStoreInfo();

    void getStoreComment(StoreInfo storeInfo, String num);

    void cancelStoreComment();

}
