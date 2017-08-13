package jcchen.taitiesyunbao.Presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import jcchen.taitiesyunbao.Model.GetStoreComment;
import jcchen.taitiesyunbao.Model.GetStoreInfo;
import jcchen.taitiesyunbao.Model.StoreModel;
import jcchen.taitiesyunbao.StoreComment;
import jcchen.taitiesyunbao.StoreInfo;
import jcchen.taitiesyunbao.View.Container.Container;

/**
 * Created by JCChen on 2017/8/8.
 */

public class StorePresenter implements OnStoreListener {

    private Container container;

    private StorePresenter storePresenter;

    private StoreModel storeModel;

    public StorePresenter(Container container) {
        this.container = container;
        this.storePresenter = this;
        this.storeModel = new StoreModel(storePresenter);
    }

    public void loadStoreInfo() {
        storeModel.getStoreInfo("二結");
    }

    public void loadStoreComment(StoreInfo storeInfo, int commentCnt) {
        new GetStoreComment(storeInfo, storePresenter).execute(String.valueOf(commentCnt));
    }

    @Override
    public void onInfoSuccess(final List<String> taskPool) {

        while(taskPool.size() != 0) {
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
                            container.loadingState(true);
                            new GetStoreInfo(storeInfo, storePresenter).execute();
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
        container.loadingState(false);
    }

    @Override
    public void onInfoError() {
        container.loadingState(false);
    }

    @Override
    public void onCommentSuccess(StoreComment storeComment) {
        container.showItem(storeComment);
    }

    @Override
    public void onCommentError() {

    }

}
