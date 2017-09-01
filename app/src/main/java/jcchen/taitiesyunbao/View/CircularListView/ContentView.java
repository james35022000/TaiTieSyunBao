package jcchen.taitiesyunbao.View.CircularListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by JCChen on 2017/8/31.
 */

public class ContentView extends RelativeLayout {

    private final float SCALE_MAX = 1f;
    private final float ANGLE_MAX = 30f;

    private float parentHeight;

    public ContentView(Context context, AttributeSet attributeSet) {
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

    private float getAngle() {
        if(getTop() < parentHeight / 2f)
            return (1f - getPosRate()) * ANGLE_MAX;
        else
            return -(1f - getPosRate()) * ANGLE_MAX;
    }

    private float getScale() {
        return Math.max(getPosRate(), 0f) * SCALE_MAX;
    }

    public float getPosRate() {
        if(getTop() < parentHeight / 2f)
            return (float)getTop() / (parentHeight / 2f);
        else
            return (parentHeight - (float)getTop()) / (parentHeight / 2f);
    }
}
