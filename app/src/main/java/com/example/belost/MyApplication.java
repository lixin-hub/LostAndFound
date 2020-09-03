package com.example.belost;

import android.app.Application;
import android.content.Context;

import java.sql.Driver;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class MyApplication extends Application {
    private static Context contect;

    @Override
    public void onCreate() {
        super.onCreate();

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，

        contect=getApplicationContext();
    }
    public static Context getContext(){return contect;}
}
