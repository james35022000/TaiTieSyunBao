package jcchen.taitiesyunbao.View.CustomView;

/**
 * Created by JCChen on 2017/8/8.
 */

public interface Container {
    boolean onBackPressed();
    void showItem(Object object);
    void loadingState(boolean state);
}
