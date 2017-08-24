package jcchen.taitiesyunbao.Presenter;


import jcchen.taitiesyunbao.Entity.StoreInfo;

/**
 * Created by JCChen on 2017/8/8.
 */

public interface StorePresenter {

    void loadStoreInfo();

    void loadStoreComment(StoreInfo storeInfo, int commentCnt);

    void cancelStoreComment();
}
