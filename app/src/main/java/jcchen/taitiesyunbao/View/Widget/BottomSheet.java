package jcchen.taitiesyunbao.View.Widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import static jcchen.taitiesyunbao.Entity.Constant.BOTTOMSHEET_STATUS_HIDE;
import static jcchen.taitiesyunbao.Entity.Constant.BOTTOMSHEET_STATUS_OVERSHOOT;
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

    private onAnimationListener animationListener;

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
            case BOTTOMSHEET_STATUS_OVERSHOOT:
                controlPoint -= overShootHeight;
                break;
            case BOTTOMSHEET_STATUS_PEEK:
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
            // Show BottomSheet animation start.
            ValueAnimator show_VA = ValueAnimator.ofInt(0, peekHeight);
            show_VA.setDuration(400);
            show_VA.setInterpolator(new AccelerateInterpolator(0.6f));
            show_VA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Status = BOTTOMSHEET_STATUS_SHOWING;
                    currentHeight =  (int) animation.getAnimatedValue();

                    // Show BottomSheet animation end.
                    if(currentHeight == peekHeight) {
                        Status = BOTTOMSHEET_STATUS_OVERSHOOT;
                        if(animationListener != null)
                            animationListener.onShowAnimationEnd();

                        //  Overshoot animation start.
                        ValueAnimator overShoot_VA = ValueAnimator.ofInt(400, 0);
                        overShoot_VA.setDuration(500);
                        overShoot_VA.setInterpolator(new OvershootInterpolator(3f));
                        overShoot_VA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                overShootHeight = (int) animation.getAnimatedValue();
                                if(overShootHeight > 200)
                                    overShootHeight = 200;
                                invalidate();
                            }
                        });
                        overShoot_VA.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                // Overshoot animation end.
                                if(animationListener != null)
                                    animationListener.onOverShootAnimationEnd();
                            }
                        });
                        overShoot_VA.start();
                    }
                    else
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

    public void addAnimationListener(onAnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public int getStatus() {
        return Status;
    }

    public interface onAnimationListener {
        void onShowAnimationEnd() ;
        void onOverShootAnimationEnd();
    }
}
