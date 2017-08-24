package jcchen.taitiesyunbao.Presenter;

import java.util.List;

import jcchen.taitiesyunbao.Entity.StoreComment;
import jcchen.taitiesyunbao.Entity.StoreInfo;

/**
 * Created by JCChen on 2017/8/10.
 */

public interface OnStoreListener {

    void onInfoSuccess(StoreInfo storeInfo);
    void onInfoSuccess(List<String> taskPool);
    void onInfoError();

    void onCommentSuccess(StoreComment storeComment);
    void onCommentError();
}
