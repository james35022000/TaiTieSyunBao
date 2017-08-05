package jcchen.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

import static jcchen.taitiesyunbao.Constant.LOADING_HANDLER_BEGIN;
import static jcchen.taitiesyunbao.Constant.LOADING_HANDLER_END;


/**
 * Created by JCChen on 2017/6/25.
 */

public class StoreFragment extends Fragment {

    private Context context;

    private RecyclerView store_recyclerView;

    private Vector<String> InternetTaskPool;

    private boolean isStoreLoading, isStoreFinished;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_frg_layout, container, false);

        store_recyclerView = (RecyclerView) view.findViewById(R.id.store_recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Dialog alertDialog = new Dialog(context);
        //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //alertDialog.setContentView(R.layout.map);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //alertDialog.show();


        displayStore(view);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void displayStore(View view) {
        final Vector<StoreInfo> storeList = new Vector<>();
        final RecyclerView.Adapter adapter = new StoreRecyclerViewAdapter(context, storeList, view);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stores");

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                final Handler h = this;
                switch(msg.what) {
                    case LOADING_HANDLER_BEGIN:
                        isStoreLoading = true;
                        ((StoreRecyclerViewAdapter)store_recyclerView.getAdapter()).setLoadingState(isStoreLoading);
                        for(int i = InternetTaskPool.size() >= 5 ? 5 : InternetTaskPool.size(); i > 0; i--) {
                            final int j = i;
                            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Stores");
                            dr.child(InternetTaskPool.get(0)).addListenerForSingleValueEvent(
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

                                            new GetStoreInfo(adapter, storeList, storeInfo, h, j == 1).execute();
                                            //if(j == 1) {
                                            //    Message msg = new Message();
                                            //    msg.what = HANDLER_END;
                                            //    h.sendMessage(msg);
                                            //}
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            InternetTaskPool.remove(0);
                        }
                        break;
                    case LOADING_HANDLER_END:
                        isStoreLoading = false;
                        ((StoreRecyclerViewAdapter)store_recyclerView.getAdapter()).setLoadingState(isStoreLoading);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        InternetTaskPool = new Vector<>();

        databaseReference.child("Stations").child("二結").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            InternetTaskPool.add(ds.getKey());
                        Message msg = new Message();
                        msg.what = LOADING_HANDLER_BEGIN;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        store_recyclerView.setAdapter(adapter);
        store_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        store_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalCount = store_recyclerView.getLayoutManager().getItemCount() - 1;
                int lastVisibleItem = ((LinearLayoutManager)store_recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if(!isStoreLoading && totalCount < lastVisibleItem + 2 && InternetTaskPool.size() > 0) {
                    isStoreLoading = true;
                    ((StoreRecyclerViewAdapter)store_recyclerView.getAdapter()).setLoadingState(isStoreLoading);
                    Message msg = new Message();
                    msg.what = LOADING_HANDLER_BEGIN;
                    handler.sendMessage(msg);
                }
            }
        });
    }


}
