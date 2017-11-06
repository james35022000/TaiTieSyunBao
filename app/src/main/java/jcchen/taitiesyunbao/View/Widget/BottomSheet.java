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
import android.view.animation.OvershootInterpolator;

import jcchen.taitiesyunbao.R;

import static jcchen.taitiesyunbao.Entity.Constant.BOTTOMSHEET_STATUS_HIDE;
import static jcchen.taitiesyunbao.Entity.Constant.BOTTOMSHEET_STATUS_PEEK;
import static jcchen.taitiesyunbao.Entity.Constant.BOTTOMSHEET_STATUS_SHOWING;

/**
 * Created by JCChen on 2017/10/11.
 */

public class BottomSheet extends View {
    private Context context;

    private int Status;

    private int MaxHeight;
    private int peekHeight;
    private int currentHeight;
    private int overShootHeight;

    private Paint paint;
    private Path path;

    public BottomSheet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        Status = BOTTOMSHEET_STATUS_HIDE;
        currentHeight = 0;
        MaxHeight = 0;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, android.R.color.white));
        paint.setAntiAlias(true);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int controlPoint = MaxHeight - currentHeight;
        super.onDraw(canvas);
        switch(Status) {
            case BOTTOMSHEET_STATUS_HIDE:
                break;
            case BOTTOMSHEET_STATUS_SHOWING:
                controlPoint -= (currentHeight < MaxHeight/3 ? 200*currentHeight/MaxHeight*4 : 200);
                break;
            case BOTTOMSHEET_STATUS_PEEK:
                controlPoint -= overShootHeight;
                break;
        }
        path.reset();
        path.moveTo(0, MaxHeight);
        path.lineTo(getWidth(), MaxHeight);
        path.lineTo(getWidth(), MaxHeight - currentHeight);
        path.quadTo(getWidth()/2, controlPoint, 0, MaxHeight - currentHeight);
        path.lineTo(0, MaxHeight);
        canvas.drawPath(path, paint);
    }

    public void show() {
        if(this.peekHeight > 0) {
            MaxHeight = peekHeight;
            ValueAnimator show_VA = ValueAnimator.ofInt(0, peekHeight);
            show_VA.setDuration(400);
            show_VA.setInterpolator(new AccelerateInterpolator(0.6f));
            show_VA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Status = BOTTOMSHEET_STATUS_SHOWING;
                    currentHeight =  (int) animation.getAnimatedValue();

                    if(currentHeight == peekHeight) {
                        Status = BOTTOMSHEET_STATUS_PEEK;
                        ValueAnimator peek_VA = ValueAnimator.ofInt(220, 0);
                        peek_VA.setDuration(500);
                        peek_VA.setInterpolator(new OvershootInterpolator(3f));
                        peek_VA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                overShootHeight = (int) animation.getAnimatedValue();
                                if(overShootHeight > 200)
                                    overShootHeight = 200;
                                invalidate();
                            }
                        });
                        peek_VA.start();
                    }

                    invalidate();
                }
            });
            show_VA.start();
        }
        else {
            Log.e("BottomSheet.show()", "Set PeekHeight before show.");
        }
    }

    public void setPeekHeight(int peekHeight) {
        this.peekHeight = peekHeight;
    }

    public int getStatus() {
        return Status;
    }

    public interface AnimationListener {
        void onAnimationStart();
        void onAnimationEnd();
    }
}
