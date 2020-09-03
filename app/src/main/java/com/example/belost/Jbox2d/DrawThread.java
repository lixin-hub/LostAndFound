package com.example.belost.Jbox2d;

import android.annotation.SuppressLint;

public class DrawThread extends Thread {

    private MySurfaceView mySurfaceView;

    public DrawThread(MySurfaceView mySurfaceView){
        this.mySurfaceView=mySurfaceView;
    }


    @SuppressLint("WrongCall")
    @Override
    public void run() {
        super.run();
        while (Params.DRAW_FLAG){
            mySurfaceView.onDraw();
            mySurfaceView.world.step(1/60f,100,100);
        }
    }
}
