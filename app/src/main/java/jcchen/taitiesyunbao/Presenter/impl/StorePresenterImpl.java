package jcchen.taitiesyunbao.Presenter.impl;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import jcchen.taitiesyunbao.Model.StoreModel;
import jcchen.taitiesyunbao.Model.impl.StoreModelImpl;
import jcchen.taitiesyunbao.Presenter.OnStoreListener;
import jcchen.taitiesyunbao.Presenter.StorePresenter;
import jcchen.taitiesyunbao.Entity.StoreComment;
import jcchen.taitiesyunbao.Entity.StoreInfo;
import jcchen.taitiesyunbao.View.Container.Container;
import jcchen.taitiesyunbao.View.MainActivity;

/**
 * Created by JCChen on 2017/8/25.
 */

public class StorePresenterImpl implements OnStoreListener, StorePresenter {
    private Container container;

    private StorePresenter storePresenter;

    private StoreModel storeModel;

    private Context context;


    public StorePresenterImpl(Container container) {
        this.container = container;
        this.context = container.getActivity();
        this.storePresenter = this;
        this.storeModel = new StoreModelImpl((OnStoreListener) storePresenter, context);
    }

    @Override
    public void loadStoreInfo() {
        storeModel.getStoreInfo("二結");
    }

    @Override
    public void loadStoreComment(StoreInfo storeInfo, int commentCnt) {
        //getStoreComment = new GetStoreComment(storeInfo, (OnStoreListener) storePresenter).execute(String.valueOf(commentCnt));
        storeModel.getStoreComment(storeInfo, String.valueOf(commentCnt));
    }

    @Override
    public void cancelStoreComment() {
        storeModel.cancelStoreComment();
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
                            //new GetStoreInfo(storeInfo, storePresenter).execute();
                            storeModel.getStoreInfo(storeInfo);
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
    public void onCommentSuccess(final StoreComment storeComment) {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                container.showItem(storeComment);
            }
        });
    }

    @Override
    public void onCommentError() {

    }
}
