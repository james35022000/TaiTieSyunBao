package jcchen.taitiesyunbao.View.Widget.CircularListView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;


/**
 * Created by JCChen on 2017/8/30.
 */

public class SelectRegionListView extends ListView {

    public final int SCROLL_TO_CENTER = -1;

    /**
     * Constructor.
     *
     * @param context
     * @param attributeSet
     */
    public SelectRegionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        try {
            // Disable fling of listView.
            Field f_vScale = AbsListView.class.getDeclaredField("mVelocityScale");
            f_vScale.setAccessible(true);
            f_vScale.set(this, 0f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set the listView to the certain position.
     *
     * @param position => Absolute position in listView. Not index of visible children.
     */
    @Override
    public void setSelection(int position) {
        setSelectionFromTop(position, getHeight() / 2 - getChildAt(0).getHeight() / 2);
    }

    /**
     * Smooth scroll to certain item.
     * If itemId equals SCROLL_TO_CENTER, just scroll to nearest item at the mid.
     *
     * @param itemId
     */
    public void smoothScroll(int itemId) {
        if(itemId == SCROLL_TO_CENTER) {
            int center_index = -1;  //  Child index (0 ~ ChildCount)
            float last_pos = Float.MAX_VALUE;
            for (int i = 0; i < getChildCount(); i++) {
                float pos = 1f - ((CircularListViewContent) getChildAt(i)).getPosRate();
                if (pos < last_pos)
                    last_pos = pos;
                else {
                    center_index = i - 1;
                    break;
                }
            }
            float scroll_offset = (1f - ((CircularListViewContent) getChildAt(center_index)).getPosRate()) * (float) getHeight() / 2f;
            if (((CircularListViewContent)(getChildAt(center_index))).getMid() < getHeight() / 2)
                smoothScrollBy(-(int) scroll_offset, 200);
            else
                smoothScrollBy((int) scroll_offset, 200);
        }
        else {
            int contentHeight = getChildAt(0).getHeight();
            int id_select = (int) getAdapter().getItemId(getFirstVisiblePosition() + getChildCount() / 2);
            int count = absMin(absMin(itemId - id_select - ((CircularListViewAdapter)getAdapter()).getListSize(),
                            itemId - id_select + ((CircularListViewAdapter)getAdapter()).getListSize()), itemId - id_select);
            smoothScrollBy(count * contentHeight - (getHeight() / 2 - ((CircularListViewContent)getChildAt(getChildCount() / 2)).getMid()), 200);
        }
    }

    /**
     * Compare two numbers a and b. If absolute value of a is bigger than absolute value of b,
     * then return original b, otherwise, return a.
     *
     * @param a
     * @param b
     * @return Return original a(or b).
     */
    private int absMin(int a, int b) {
        if(Math.abs(a) < Math.abs(b))
            return a;
        else
            return b;
    }
}
