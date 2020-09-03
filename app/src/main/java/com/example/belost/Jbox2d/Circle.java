package com.example.belost.Jbox2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class Circle extends MyBody {
    public float ridius;
    public float x,y;

    public Circle(Body body, Bitmap bitmap, float ridius, int color) {
      this.ridius = ridius;
        this.body=body;
        this.bitmap=bitmap;
        this.color=color;
        this.height=ridius*2;
        this.width=ridius*2;
        if (bitmap!=null){
        this.isBitmap=true;} else {this.isBitmap=false; }
    }


    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
        if (!isDraw)
            return;
        x = body.getPosition().x * Params.RATE;
        y = body.getPosition().y * Params.RATE;
        double angle=Math.toDegrees(body.getAngle());
        Matrix matrix=new Matrix();
        matrix.setRotate((float) angle,x,y);
        canvas.setMatrix(matrix);
        if (bitmap == null) {
            paint.setColor(color);
            canvas.drawCircle(x, y, ridius, paint);
        } else {
            RectF dst = new RectF(x - ridius, y - ridius, x + ridius, y + ridius);
            canvas.drawBitmap(bitmap, null, dst, paint);
        }
    }


    @Override
    public void onTouch(MotionEvent event,MyBody myBody) {

    }

    @Override
    public void onMove(MotionEvent event,MyBody myBody) {
        float x1 = event.getX();
        float y1 = event.getY();
        if (myBody!=null)
            myBody.body.setTransform(new Vec2(x1/Params.RATE,y1/Params.RATE),myBody.body.getAngle());
    }

    @Override
    public void onUp(MotionEvent event,MyBody myBody) {
    }
}
