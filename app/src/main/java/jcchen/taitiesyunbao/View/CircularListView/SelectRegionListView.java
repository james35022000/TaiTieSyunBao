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

    public void smoothScroll() {
        int center_index = 0;
        float last_pos = Float.MAX_VALUE;
        for(int i = 0; i < getChildCount(); i++) {
            float pos = 1f - ((ContentView) getChildAt(i)).getPosRate();
            if(pos < last_pos)
                last_pos = pos;
            else {
                center_index = i - 1;
                break;
            }
        }
        try {
            float scroll_offset = (1f - ((ContentView) getChildAt(center_index)).getPosRate()) * (float) getHeight() / 2f;
            if(((ContentView)getChildAt(center_index)).getMid() < getHeight() / 2)
                smoothScrollBy(-(int)scroll_offset, 200);
            else
                smoothScrollBy((int)scroll_offset, 200);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
