package jcchen.taitiesyunbao.View.CustomView;

import android.content.Context;

/**
 * Created by JCChen on 2017/8/8.
 */

public interface Container {
    boolean onBackPressed();
    void showItem(Object object);
    void loadingState(boolean state);
    Context getMainThread();
}
