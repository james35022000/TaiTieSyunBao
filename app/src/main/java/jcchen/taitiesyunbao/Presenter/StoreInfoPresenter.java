package jcchen.taitiesyunbao.Presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import jcchen.taitiesyunbao.Model.GetStoreInfo;
import jcchen.taitiesyunbao.Model.StoreModel;
import jcchen.taitiesyunbao.StoreComment;
import jcchen.taitiesyunbao.StoreInfo;
import jcchen.taitiesyunbao.View.Container;
import jcchen.taitiesyunbao.View.StoreContainer;
import jcchen.taitiesyunbao.View.StoreRecyclerView;

/**
 * Created by JCChen on 2017/8/8.
 */

public class StoreInfoPresenter implements OnStoreListener {

    private boolean isLoading;

    private Container container;

    private StoreInfoPresenter storeInfoPresenter;

    private StoreModel storeModel;

    public StoreInfoPresenter(StoreContainer container) {
        this.isLoading = false;
        this.container = container;
        this.storeInfoPresenter = this;
        this.storeModel = new StoreModel(storeInfoPresenter);
    }

    public void loadStoreInfo() {
        storeModel.getStoreInfo("二結");
    }

    @Override
    public void onInfoSuccess(final List<String> taskPool) {

        for (int i = 0; i < taskPool.size(); i++) {
            FirebaseDatabase.getInstance().getReference("Stores").child(taskPool.get(0))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final StoreInfo storeInfo = new StoreInfo();
                            storeInfo.setID(dataSnapshot.child("ID").getValue().toString());
                            storeInfo.setLatitude(dataSnapshot.child("Latitude").getValue().toString());
                            storeInfo.setLongitude(dataSnapshot.child("Longitude").getValue().toString());
                            storeInfo.setStation(dataSnapshot.child("Near_Station").getValue().toString());
                            storeInfo.setAddress(dataSnapshot.child("Address_tw").getValue().toString(),
                                    dataSnapshot.child("Address_en").getValue().toString());

                            new GetStoreInfo(storeInfo, storeInfoPresenter).execute();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            taskPool.remove(0);
        }
    }

    @Override
    public void onInfoSuccess(StoreInfo storeInfo) {
        container.showItem(storeInfo);
    }

    @Override
    public void onInfoError() {}

    @Override
    public void onCommentSuccess(StoreComment storeComment) {

    }

    @Override
    public void onCommentError() {}

}
