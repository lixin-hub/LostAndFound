package com.example.belost.Jbox2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.SensorEvent;
import android.icu.text.CaseMap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompatSideChannelService;


import com.example.belost.R;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,GravityListener {
    public Context context;
    public List<MyBody> myBodies;
    private Paint paint;
    public MyBody slectedBody;
    float width,height;
    private Canvas canvas;
    private SurfaceHolder holder;
    private DrawThread drawThread;
    public World world;
    public GravityListener gravityListener;
    private Vec2 gravity=new Vec2(0,0f);
    MyBody myBody;
    public MySurfaceView(Context context){
        super(context);
        this.context=  context;

        paint=new Paint();
        paint.setAntiAlias(true);
        holder=this.getHolder();
        holder.addCallback(this);
      gravityListener=this;
    }

    public MySurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context =  context;
        paint = new Paint();
        paint.setAntiAlias(true);
        holder = this.getHolder();
        holder.addCallback(this);
        gravityListener=this;
        setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(slectedBody!=null) {slectedBody=null;}
                        float x = event.getX();
                        float y = event.getY();
                        float width = 0, height = 0, mx = 0, my = 0;
                        for (MyBody m : myBodies) {
                            if (m.body.m_userData == null) {
                                break;
                            }
                            int type = (int) m.body.m_userData;
                            if (type == Params.TYPE_RECT || type == Params.TYPE_RECT_BITMAP) {
                                mx = m.body.getPosition().x * Params.RATE;
                                my = m.body.getPosition().y * Params.RATE;
                                width = ((Rect) m).halfWidth * 2;
                                height = ((Rect) m).halfHeight * 2;
                            } else if (type == Params.TYPE_CIRCLE || type == Params.TYPE_CIRCLE_BITMAP) {
                                width = ((Circle) m).ridius * 2;
                                height = ((Circle) m).ridius * 2;
                                mx = m.body.getPosition().x * Params.RATE;
                                my = m.body.getPosition().y * Params.RATE;
                            }
                                if (x > mx - width / 2 && x < mx + width / 2 && y > my - height / 2 && y < my + height / 2) {
                                    slectedBody = m;
                                    m.onTouch(event, slectedBody);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (slectedBody!=null
                                &&event.getY()<MySurfaceView.this.height-slectedBody.height/2
                                && event.getX()<MySurfaceView.this.width-slectedBody.width/2
                                &&event.getX()>slectedBody.width/2
                                &&event.getY()>MySurfaceView.this.height/2
                           )
                       slectedBody.onMove(event,slectedBody);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(slectedBody!=null)
                     slectedBody.onUp(event,slectedBody);
                    default:break;
                }
                return true;
            }
        });

    }




    public MySurfaceView(Context context, AttributeSet attr, int id){
        super(context,attr,id);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
            if (drawThread==null){
            drawThread = new DrawThread(this);
            init();
            Params.DRAW_FLAG = true;
            drawThread.start();
            }else {
                Params.DRAW_FLAG=true;
                drawThread.start();
            }
        }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
drawThread.stop();
        Params.DRAW_FLAG=false;
    }

    public void onDraw() {
        canvas=holder.lockCanvas();
        if (canvas==null){
         //   NotificiationUtil.showToast("canvas is null");
            return;}
        canvas.drawColor(Color.WHITE);
        for (int i=0;i<myBodies.size();i++){
            myBodies.get(i).drawSelf(canvas,paint);
        }
        holder.unlockCanvasAndPost(canvas);

    }


    public void init() {
        world = new World(gravity);
        world.setAllowSleep(false);
        final BodyCreateUtil bodyCreateUtil = new BodyCreateUtil(world);
        myBodies = new ArrayList<>();
        width=this.getMeasuredWidth();
        height=this.getMeasuredHeight();
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.qipao);
        Bitmap bitmap1=BitmapToRoundUtil.toRoundBitmap(bitmap);
        myBodies.add(bodyCreateUtil.createRect(null,10,height/2,5,height/2, Color.TRANSPARENT,true,Params.TYPE_WALL));//left
        myBodies.add(bodyCreateUtil.createRect(null,width-10,height/2,5,height/2, Color.TRANSPARENT,true,Params.TYPE_WALL));//right
        myBodies.add(bodyCreateUtil.createRect(null,width/2,-10,width/2,5, Color.TRANSPARENT,true,Params.TYPE_WALL));//top
        myBodies.add(bodyCreateUtil.createRect(null,width/2,height,width/2,2, Color.BLACK,true,Params.TYPE_WALL));//bottom
        for (int i=0;i<10;i++){
            myBody=bodyCreateUtil.createCircle(bitmap1,20+2*i,height-10,60,Color.YELLOW,false,Params.TYPE_CIRCLE_BITMAP);
            myBodies.add(myBody);
        }
       // myBodies.add(bodyCreateUtil.createRect(bitmap2,100,100,80,80*r,Color.WHITE,false,Params.TYPE_RECT_BITMAP));

    }

    @Override
    public void ongravityChanged(SensorEvent event) {
       float x= event.values[0];
        float y=event.values[1];
        world.setGravity(new Vec2(-x,y));
    }
}
