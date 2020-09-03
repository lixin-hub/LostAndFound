package com.example.belost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.listener.OnCitySelectListener;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.example.belost.Jbox2d.MySurfaceView;
import com.example.belost.UserSystem.User;
import com.example.belost.Util.NotificationUtil;
import com.example.belost.Util.PictureUtil;
import com.example.belost.Util.Utility;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MineActivity extends AppCompatActivity implements View.OnClickListener{


    Toolbar bar;
    private PictureUtil pictureUtil;
    private SensorManager sensorManager;
    Sensor sensor;
    MySurfaceView m;
    SensorEventListener event;
    private ProgressDialog dialog;
    private LostActivity lostFragment;
    private ImageButton main_bt_lost,main_bt_found,main_bt_mine;
    private Button bt_cpw,bt_sginout;
    private ImageView image;
    private TextView home,nickname,homeText,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        bar = findViewById(R.id.mine_toolbar2);
        m=findViewById(R.id.mysurface);
        setSupportActionBar(bar);
        pictureUtil=new PictureUtil(this);
        bar.setTitle("个人中心");
        bar.setTitleMarginStart(10);
        userName=findViewById(R.id.mine_userName);
        home=findViewById(R.id.mine_home1);
        homeText=findViewById(R.id.home_text1);
        nickname=findViewById(R.id.mine_nikname);
        bt_cpw=findViewById(R.id.mine_change_password);
        bt_sginout=findViewById(R.id.mine_bt_sginout);
        main_bt_found = findViewById(R.id.mine_bt_found);
        main_bt_lost = findViewById(R.id.mine_bt_lost);
        main_bt_mine = findViewById(R.id.mine_bt_mine);
        image=findViewById(R.id.mine_image);
        image.setOnClickListener(this);
        nickname.setOnClickListener(this);
        home.setOnClickListener(this);
        bt_sginout.setOnClickListener(this);
        bt_cpw.setOnClickListener(this);
        main_bt_mine.setOnClickListener(this);
        main_bt_found.setOnClickListener(this);
        main_bt_lost.setOnClickListener(this);
        home.setOnClickListener(this);
        User user=BmobUser.getCurrentUser(User.class);
        homeText.setText(user.getAddress());
        if (user.getNickName()!=null){nickname.setText(user.getNickName());}
        userName.setText(user.getUsername());
        if (user.getImagPath()!=null){image.setImageURI(Uri.parse(user.getImagPath()));}
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
        event=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                m.gravityListener.ongravityChanged(sensorEvent);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.mine_bt_lost:
                Utility.Translate(this, LostActivity.class);
                break;
            case R.id.mine_bt_found:
                Utility.Translate(this, FoundActivity.class);
                break;
            case R.id.mine_bt_mine:
                break;
            case R.id.mine_change_password:
                NotificationUtil.showAlertDialogPassword(this).show();
                break;
            case R.id.mine_bt_sginout:
                BmobUser.logOut();
                NotificationUtil.showToast("注销成功");
                finish();
                break;
            case R.id.mine_nikname:
                if(!BmobUser.isLogin()){NotificationUtil.showToast("请登录");return;}
                NotificationUtil.showAlertDialogNickname(this,view, nickname).show();

                break;
            case R.id.mine_home1:
                if(!BmobUser.isLogin()){NotificationUtil.showToast("请登录");return;}
                NotificationUtil.showPickerView(this,new OnCitySelectListener(){
                    @Override
                    public void onCitySelect(String str) {

                    }

                    @Override
                    public void onCitySelect(String prov, String city, String area) {
                        homeText.setText(prov+" "+city+""+area);
                        User user=BmobUser.getCurrentUser(User.class);
                        user.setAddress(homeText.getText().toString());
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    NotificationUtil.showSnackbar(view,"更新成功");
                                }
                                else
                                {
                                    NotificationUtil.showSnackbar(view,"更新失败"+e.toString());
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.mine_image:
                pictureUtil.openAlbum();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pictureUtil.openAlbum();
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 判断手机系统版本号
                // 4.4及以上系统使用这个方法处理图片
                String path=pictureUtil.handleImageOnKitKat(data);
                Bitmap b= BitmapFactory.decodeFile(path);
                image.setImageBitmap(b);
               User user= BmobUser.getCurrentUser(User.class);
               user.setImagPath(path);
               user.update(new UpdateListener() {
                   @Override
                   public void done(BmobException e) {
                  if(e==null){
                      NotificationUtil.showToast("更新成功");
                  }  else { NotificationUtil.showToast("更新失败");}
                   }
               });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(this.toString(),"onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(event,sensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(event);
    }
}