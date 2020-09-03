package com.example.belost.Util;

import android.app.Activity;
import android.content.Intent;

public class Utility {
    public static void Translate(Activity one,Class anther){
        Intent intent=new Intent(one,anther);
        one.startActivity(intent);
        one.finish();
    }
}
