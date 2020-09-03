package com.example.belost.Jbox2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class Rect extends MyBody {
    public float halfWidth,halfHeight;
    public float x,y;

    public Rect(Body body, Bitmap bitmap, float x, float y, float halfWidth, float halfHeight, int color){

        this.body=body;
        this.bitmap=bitmap;
        this.halfHeight=halfHeight;
        this.halfWidth=halfWidth;
        this.color=color;
        this.height=halfHeight*2;
        this.width=halfWidth*2;
        if(bitmap!=null){this.isBitmap=true;}else{this.isBitmap=false;}
    }

    @Override
    public void drawSelf(Canvas canvas, Paint paint) {
       if(!isDraw)
           return;
        x=body.getPosition().x*Params.RATE;
        y=body.getPosition().y*Params.RATE;
        double angle=Math.toDegrees(body.getAngle());
        Matrix matrix=new Matrix();
        matrix.setRotate((float) angle,x,y);
        canvas.setMatrix(matrix);
        if(bitmap==null){
            paint.setColor(color);
            canvas.drawRect(x-halfWidth,y-halfHeight,x+halfWidth,y+halfHeight,paint);
        }else {
            RectF dst = new RectF(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
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
    public void onUp(MotionEvent event, MyBody myBody) {

    }

}
