package jcchen.taitiesyunbao.View.Widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by JCChen on 2017/10/11.
 */

public class BottomSheet extends View {
    private Context context;

    private int MaxHeight;
    private int peekHeight;
    private int currentHeight;

    private Paint paint;
    private Path path;

    public BottomSheet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        currentHeight = 0;
        MaxHeight = 0;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, android.R.color.white));
        paint.setAntiAlias(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(0, MaxHeight);
        path.lineTo(getWidth(), MaxHeight);
        path.lineTo(getWidth(), MaxHeight - currentHeight);
        path.lineTo(0, MaxHeight - currentHeight);
        path.lineTo(0, MaxHeight);
        canvas.drawPath(path, paint);
    }

    public void show() {
        if(this.peekHeight > 0) {
            MaxHeight = peekHeight;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, peekHeight);
            valueAnimator.setDuration(800);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentHeight =  (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }
        else {
            Log.e("BottomSheet.show()", "Set PeekHeight before show.");
        }
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
    }
}
