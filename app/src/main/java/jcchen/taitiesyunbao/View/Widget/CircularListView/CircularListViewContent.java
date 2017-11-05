package jcchen.taitiesyunbao.View.Widget.CircularListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by JCChen on 2017/8/31.
 */

public class CircularListViewContent extends RelativeLayout {

    private final float SCALE_MAX = 1f;
    private final float ANGLE_MAX = 30f;

    private float parentHeight;

    public CircularListViewContent(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setParentHeight(float parentHeight) {
        this.parentHeight = parentHeight;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        float angle = getAngle();
        float scale = getScale();
        canvas.save();
        {
            canvas.scale(scale, scale, getWidth(), getHeight() / 2);
            canvas.rotate(angle, getWidth(), getHeight() / 2);
            super.dispatchDraw(canvas);
        }
        canvas.restore();
    }

    public int getMid() {
        return (getTop() + getBottom()) / 2;
    }

    private float getAngle() {
        if(getMid() < parentHeight / 2f)
            return (1f - getPosRate()) * ANGLE_MAX;
        else
            return -(1f - getPosRate()) * ANGLE_MAX;
    }

    private float getScale() {
        return Math.max(getPosRate(), 0f) * SCALE_MAX;
    }

    /**
     * Get the position of content and calculate vertical distance to center of listView.
     *
     * @return
     */
    public float getPosRate() {
        if(getMid() < parentHeight / 2f)
            return (float)getMid() / (parentHeight / 2f);
        else
            return (parentHeight - (float)getMid()) / (parentHeight / 2f);
    }
}
