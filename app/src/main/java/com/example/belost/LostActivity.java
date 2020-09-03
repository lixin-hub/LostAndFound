package com.example.belost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.belost.UserSystem.Found;
import com.example.belost.UserSystem.Lost;
import com.example.belost.UserSystem.PostListener;
import com.example.belost.UserSystem.User;
import com.example.belost.UserSystem.UserListener;
import com.example.belost.UserSystem.UserUtil;
import com.example.belost.Util.LostAdapter;
import com.example.belost.Util.NotificationUtil;
import com.example.belost.Util.PictureUtil;
import com.example.belost.Util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

public class LostActivity extends AppCompatActivity implements View.OnClickListener{
    private PictureUtil picu;
    private Bitmap b;
    FloatingActionButton bt_add;
    Toolbar bar;
    private List<BmobObject> losts;
    private LostAdapter lostAdapter;
    private ProgressDialog dialog;
    private RecyclerView recyclerView;
    private ImageButton main_bt_lost,main_bt_found,main_bt_mine;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_lost);
        bar=findViewById(R.id.toolbar2);
        setSupportActionBar(bar);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.main_Recyclerview);
        main_bt_found =findViewById(R.id.main_bt_found);
        main_bt_lost = findViewById(R.id.main_bt_lost);
        main_bt_mine = findViewById(R.id.main_bt_mine);
        bt_add=findViewById(R.id.main_bt_float);
        bt_add.setOnClickListener(this);
        main_bt_mine.setOnClickListener(this);
        main_bt_found.setOnClickListener(this);
        main_bt_lost.setOnClickListener(this);
        bar.setTitle("失物列表");
        //init();
        losts = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        lostAdapter = new LostAdapter(losts);
        recyclerView.setAdapter(lostAdapter);
        picu = new PictureUtil(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserUtil.queryLost(new UserListener() {
                    @Override
                    public void onSuccess(BmobObject object) {

                    }

                    @Override
                    public void onQueryLostSuccess(List<Lost> obj) {
                        losts.removeAll(losts);
                        losts.addAll(obj);
                        lostAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onQueryFoundSuccess(List<Found> obj) {

                    }

                    @Override
                    public void onFailed(BmobException e) {

                    }
                });
            }
        });
        UserUtil.queryLost(new UserListener() {
            @Override
            public void onSuccess(BmobObject object) {

            }

            @Override
            public void onQueryLostSuccess(List<Lost> obj) {
                losts.removeAll(losts);
                losts.addAll(obj);
                lostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onQueryFoundSuccess(List<Found> obj) {

            }

            @Override
            public void onFailed(BmobException e) {

            }
        });
    }


    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = BmobUser.getCurrentUser(User.class);
                for (int i=0;i<10;i++) {
                    Lost lost = new Lost();
                    lost.setUserName(user.getUsername());
                    lost.setAddress("重庆理工大学两江人工智能学院");
                    lost.setBiaoqian("生活就是这样");
                    lost.setDate("8.23");
                    lost.setName("人民币100元");
                    // lost.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.image));
                    lost.setPhoneNumber("15823626666");
                    lost.setSubcribeTime(new

                            BmobDate(new Date(System.currentTimeMillis())));
                    //lost.setPicture(BitmapFactory.decodeResource(getResources(),R.drawable.image));
                    losts.add(lost);
                    UserUtil.save(losts);
                }
            }
        }).start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                picu.openAlbum();
            } else {
                Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == this.RESULT_OK) {
                // 判断手机系统版本号
                // 4.4及以上系统使用这个方法处理图片
                String path=picu.handleImageOnKitKat(data);
                b= BitmapFactory.decodeFile(path);

            }
        }
    }
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.main_bt_lost:
                bar.setTitle("看看有没有我掉的东西");
                break;
            case R.id.main_bt_found:
                bar.setTitle("看看大家捡到了什么");
                Utility.Translate(this, FoundActivity.class);
                break;
            case R.id.main_bt_mine:
                Utility.Translate(this, MineActivity.class);
                bar.setTitle("个人中心");
                break;
            case R.id.main_bt_float:
                if(!BmobUser.isLogin()){NotificationUtil.showToast("请登录");return;}
                final User user=BmobUser.getCurrentUser(User.class);
                NotificationUtil.showAlertDialogPost(this, "丢东西了，不要太伤心啊!", new PostListener() {
                    @Override
                    public void onPosted(String name, String addr, String time, String phone) {
                        Lost lost=new Lost();
                        lost.setAddress(user.getAddress());
                        lost.setUserName(user.getUsername());
                        lost.setName(name);
                        lost.setNickName(user.getNickName());
                        lost.setAddress(addr);
                        lost.setDate(time);
                        lost.setPhoneNumber(phone);
                        lost.setBiaoqian("生活就是这样，永远不知道下一秒会失去到什么！");
                        lost.setSubcribeTime(new BmobDate(new Date(System.currentTimeMillis())));
                        UserUtil.addData(lost);
                    }
                }).show();
                break;
        }
    }

}
