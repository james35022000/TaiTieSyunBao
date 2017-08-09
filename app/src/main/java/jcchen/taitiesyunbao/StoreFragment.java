package jcchen.taitiesyunbao;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import jcchen.taitiesyunbao.View.Container;
import jcchen.taitiesyunbao.View.StoreContainer;


/**
 * Created by JCChen on 2017/6/25.
 */

public class StoreFragment extends Fragment {

    private Context context;

    private Vector<String> InternetTaskPool;

    private boolean isStoreLoading, isStoreFinished;

    private StoreContainer container;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_layout, container, false);

        container = (StoreContainer) view.findViewById(R.id.container);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Dialog alertDialog = new Dialog(context);
        //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //alertDialog.setContentView(R.layout.map);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //alertDialog.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPause", "null");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "null");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "null");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("onStop", "null");
    }

}
