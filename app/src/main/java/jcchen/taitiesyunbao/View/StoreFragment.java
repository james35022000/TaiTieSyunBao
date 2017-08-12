package jcchen.taitiesyunbao.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jcchen.taitiesyunbao.R;


/**
 * Created by JCChen on 2017/6/25.
 */

public class StoreFragment extends Fragment {

    private Context context;

    private View oldView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(oldView == null)
            oldView = inflater.inflate(R.layout.store_layout, container, false);

        ViewGroup parent = (ViewGroup) oldView.getParent();
        if(parent != null)
            parent.removeView(oldView);

        return oldView;
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
