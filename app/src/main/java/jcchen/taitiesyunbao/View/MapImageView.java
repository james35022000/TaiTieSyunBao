package jcchen.taitiesyunbao.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

import jcchen.taitiesyunbao.R;

/**
 * Created by JCChen on 2017/9/10.
 */

public class MapImageView extends AppCompatImageView {

    private final Paint paint_stroke, paint_clear;
    private Bitmap map;

    public MapImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint_stroke = new Paint();
        paint_stroke.setStrokeWidth(4);
        paint_stroke.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paint_stroke.setStyle(Paint.Style.STROKE);
        this.paint_clear = new Paint();
        paint_clear.setStrokeWidth(3);
        paint_clear.setColor(ContextCompat.getColor(context, R.color.colorBackground));
        paint_clear.setStyle(Paint.Style.FILL);
        this.map = null;
    }

    public void startAnimation(final List<Path> action, final Bitmap bitmap) {
        map = bitmap;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = new Canvas(map);
                canvas.drawColor(Color.TRANSPARENT);
                for(int i = 1; i < action.size(); i++) {
                    map.eraseColor(Color.TRANSPARENT);
                    canvas.drawPath(action.get(i), paint_clear);
                    canvas.drawPath(action.get(i), paint_stroke);
                    postInvalidate();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                map = null;
            }
        }).start();
    }

    public void showMap(Bitmap map) {
        this.map = map;
        invalidate();
        map = null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(map != null) {
            canvas.drawBitmap(map, (canvas.getWidth() - map.getWidth()) / 2, (canvas.getHeight() - map.getHeight()) / 2, null);
        }
        super.dispatchDraw(canvas);
    }
}
