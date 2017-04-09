package com.example.jack_pc.trainordersystem;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

import java.io.Serializable;

import static android.graphics.PixelFormat.UNKNOWN;

/**
 * Created by Jack-PC on 2017/4/10.
 */

public class SerialDrawable extends Drawable implements Serializable {

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public int getOpacity() {
        return UNKNOWN;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }
}
