package com.example.belost.Jbox2d;

import android.content.Context;
import android.view.Display;

import com.example.belost.MineActivity;


public class ScreenUtil
{
    public static int getScreenWidth(MineActivity context){
        Display d=context.getWindowManager().getDefaultDisplay();
        return  d.getWidth();
    }
    public static int getScreenHeight(MineActivity context){
        Display d=context.getWindowManager().getDefaultDisplay();
        return  d.getHeight();
    }
}
