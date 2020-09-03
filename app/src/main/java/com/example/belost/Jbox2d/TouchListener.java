package com.example.belost.Jbox2d;

import android.view.MotionEvent;

public interface TouchListener {
    public void onTouch(MotionEvent event, MyBody myBody);
    public void onMove(MotionEvent event, MyBody myBody);
    public void onUp(MotionEvent event, MyBody myBody);
}
