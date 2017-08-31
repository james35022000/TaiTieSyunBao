package jcchen.taitiesyunbao.View.CircularListView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;


/**
 * Created by JCChen on 2017/8/30.
 */

public class SelectRegionListView extends ListView {

    public SelectRegionListView(Context context) {
        super(context);
    }

    public SelectRegionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        try {
            Field f_vScale = AbsListView.class.getDeclaredField("mVelocityScale");
            f_vScale.setAccessible(true);
            f_vScale.set(this, 0f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
