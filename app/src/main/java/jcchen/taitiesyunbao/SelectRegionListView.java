package jcchen.taitiesyunbao;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;


/**
 * Created by JCChen on 2017/8/30.
 */

public class SelectRegionListView extends ListView {

    private int State;

    public SelectRegionListView(Context context) {
        super(context);
    }

    public SelectRegionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        try {
            Field f_vScale = AbsListView.class.getDeclaredField("mVelocityScale");
            f_vScale.setAccessible(true);
            f_vScale.set(this, 0f);

            Field f_pScroller = AbsListView.class.getDeclaredField("PositionScroller");
            f_pScroller.setAccessible(true);
            Field f_DUR = f_pScroller.getType().getDeclaredField("SCROLL_DURATION");
            f_DUR.setAccessible(true);
            f_DUR.set(this, 500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    public void setState(int State) {
        this.State = State;
    }

    public int getState() {
        return this.State;
    }
}
