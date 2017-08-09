package jcchen.taitiesyunbao.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Presenter.StoreInfoPresenter;
import jcchen.taitiesyunbao.View.Container;

/**
 * Created by JCChen on 2017/8/10.
 */

public class StoreModel {

    private StoreInfoPresenter storeInfoPresenter;

    public StoreModel(StoreInfoPresenter storeInfoPresenter) {
        this.storeInfoPresenter = storeInfoPresenter;
    }

    public void getStoreInfo (LatLng latLng_center) {

    }

    public void getStoreInfo (String place_name) {
        final List<String> tackPool = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Stores").child("Stations").child(place_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    tackPool.add(ds.getKey());
                storeInfoPresenter.onInfoSuccess(tackPool);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getStoreComment () {

    }
}