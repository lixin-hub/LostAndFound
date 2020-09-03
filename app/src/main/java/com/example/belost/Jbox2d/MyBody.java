package com.example.belost.Jbox2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.jbox2d.dynamics.Body;
public abstract class MyBody implements TouchListener {
    public Body body;
    public int color;
    public boolean isDraw=true;
    public Bitmap bitmap;
    public boolean isBitmap;
    float width,height;
    public abstract void drawSelf(Canvas canvas, Paint paint);

}
