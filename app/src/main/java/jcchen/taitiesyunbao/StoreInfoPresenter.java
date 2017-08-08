package jcchen.taitiesyunbao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JCChen on 2017/8/8.
 */

public class StoreInfoPresenter {

    private List<String> InternetTaskPool;

    private final DatabaseReference databaseReference;

    public StoreInfoPresenter() {
        this.InternetTaskPool = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Stores");
    }

    public void getStoreInfo(final Container container) {
        databaseReference.child("Stations").child("二結").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    InternetTaskPool.add(ds.getKey());
                showStore(container);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showStore(final Container container) {
        for(int i = 0; i < 5; i++) {
            if (InternetTaskPool.size() == 0)
                break;
            databaseReference.child(InternetTaskPool.get(0)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final StoreInfo storeInfo = new StoreInfo();
                            storeInfo.setID(dataSnapshot.child("ID").getValue().toString());
                            storeInfo.setLatitude(dataSnapshot.child("Latitude").getValue().toString());
                            storeInfo.setLongitude(dataSnapshot.child("Longitude").getValue().toString());
                            storeInfo.setStation(dataSnapshot.child("Near_Station").getValue().toString());
                            storeInfo.setAddress(dataSnapshot.child("Address_tw").getValue().toString(),
                                    dataSnapshot.child("Address_en").getValue().toString());

                            new GetStoreInfo(container, storeInfo).execute();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            InternetTaskPool.remove(0);
        }
    }

    public int getTotalCount() {
        return InternetTaskPool.size();
    }


}
