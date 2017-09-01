package jcchen.taitiesyunbao.View.CircularListView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;

import jcchen.taitiesyunbao.R;


/**
 * Created by JCChen on 2017/8/30.
 */

public class SelectRegionListView extends ListView {

    public final int SCROLL_TO_CENTER = -1;

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

    @Override
    public void setSelection(int position) {
        setSelectionFromTop(position, getHeight() / 2);
    }

    public void smoothScroll(int itemId) {
        if(itemId == SCROLL_TO_CENTER) {
            int center_index = -1;  //  Child index (0 ~ ChildCount)
            float last_pos = Float.MAX_VALUE;
            for (int i = 0; i < getChildCount(); i++) {
                float pos = 1f - ((ContentView) getChildAt(i)).getPosRate();
                if (pos < last_pos)
                    last_pos = pos;
                else {
                    center_index = i - 1;
                    break;
                }
            }
            float scroll_offset = (1f - ((ContentView) getChildAt(center_index)).getPosRate()) * (float) getHeight() / 2f;
            if ((getChildAt(center_index)).getTop() < getHeight() / 2)
                smoothScrollBy(-(int) scroll_offset, 200);
            else
                smoothScrollBy((int) scroll_offset, 200);
        }
        else {
            int contentHeight = getChildAt(0).getHeight();
            int id_select = (int) getAdapter().getItemId(getFirstVisiblePosition() + getChildCount() / 2);
            int count = absMin(absMin(itemId - id_select - ((CircularListViewAdapter)getAdapter()).getListSize(),
                            itemId - id_select + ((CircularListViewAdapter)getAdapter()).getListSize()), itemId - id_select);
            smoothScrollBy(count * contentHeight - (getHeight() / 2 - getChildAt(getChildCount() / 2).getTop()), 200);
        }
    }

    private int absMin(int a, int b) {
        if(Math.abs(a) < Math.abs(b))
            return a;
        else
            return b;
    }
}
