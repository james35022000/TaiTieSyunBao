package jcchen.taitiesyunbao.View.Container;

/**
 * Created by JCChen on 2017/8/8.
 */

public interface Container {
    boolean onBackPressed();
    void showItem(Object object);
    void loadingState(boolean state);
}
