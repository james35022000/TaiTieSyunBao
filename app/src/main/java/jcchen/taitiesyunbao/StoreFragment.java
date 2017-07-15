package jcchen.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;


/**
 * Created by JCChen on 2017/6/25.
 */

public class StoreFragment extends Fragment {

    private Context context;

    private RecyclerView store_recyclerView;

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

        displayStore();
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

    private void displayStore() {
        Log.i("Display", "HERE");
        final Vector<StoreInfo> storeList = new Vector<>();
        final RecyclerView.Adapter adapter = new StoreRecyclerViewAdapter(context, storeList);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stores");
        store_recyclerView.setAdapter(adapter);
        store_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        databaseReference.child("Stations").child("二結").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Stores");
                    dr.child(ds.getKey().toString()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            new GetStoreInfo(adapter, storeList).execute(
                                    dataSnapshot.child("ID").getValue().toString(),
                                    dataSnapshot.child("Latitude").getValue().toString(),
                                    dataSnapshot.child("Longitude").getValue().toString(),
                                    dataSnapshot.child("Near_Station").getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
