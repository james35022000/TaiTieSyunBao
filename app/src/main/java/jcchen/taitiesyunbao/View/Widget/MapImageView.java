package jcchen.taitiesyunbao.View.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.List;

import jcchen.taitiesyunbao.R;

/**
 * Created by JCChen on 2017/9/10.
 */

public class MapImageView extends AppCompatImageView {

    private final Paint paint_stroke, paint_clear;
    private Bitmap map;
    private Canvas animCanvas;

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

    public void setMap(Bitmap map) {
        this.map = map;
        animCanvas = new Canvas(map);
        animCanvas.drawColor(Color.TRANSPARENT);
    }

    public void startAnimation(final List<Path> action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < action.size(); i++) {
                    map.eraseColor(Color.TRANSPARENT);
                    animCanvas.drawPath(action.get(i), paint_clear);
                    animCanvas.drawPath(action.get(i), paint_stroke);
                    postInvalidate();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(map != null) {
            canvas.drawBitmap(map, (canvas.getWidth() - map.getWidth()) / 2, (canvas.getHeight() - map.getHeight()) / 2, null);
        }
    }
}
