package com.example.belost;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.belost.UserSystem.Found;
import com.example.belost.UserSystem.Lost;
import com.example.belost.UserSystem.PostListener;
import com.example.belost.UserSystem.User;
import com.example.belost.UserSystem.UserListener;
import com.example.belost.UserSystem.UserUtil;
import com.example.belost.Util.FoundAdapter;
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

public class FoundActivity extends AppCompatActivity implements View.OnClickListener {

    private PictureUtil picu;
    Toolbar bar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<BmobObject> founds;
    private FoundAdapter foundAdapter;
    private RecyclerView recyclerView;
    private ImageButton main_bt_lost,main_bt_found,main_bt_mine;
    private FloatingActionButton bt_add;
    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found);
        bar=findViewById(R.id.found_toolbar2);
        setSupportActionBar(bar);
        swipeRefreshLayout=findViewById(R.id.found_swipe_refresh);
        bt_add=findViewById(R.id.found_bt_float);
        recyclerView = findViewById(R.id.found_Recyclerview);
        main_bt_found =findViewById(R.id.found_bt_found);
        main_bt_lost = findViewById(R.id.found_bt_lost);
        main_bt_mine = findViewById(R.id.found_bt_mine);
        main_bt_mine.setOnClickListener(this);
        main_bt_found.setOnClickListener(this);
        main_bt_lost.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bar.setTitle("发现");
       // init();
        founds = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        foundAdapter = new FoundAdapter(founds);
        recyclerView.setAdapter(foundAdapter);
        picu = new PictureUtil(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserUtil.queryFound(new UserListener() {
                    @Override
                    public void onSuccess(BmobObject object) {}
                    @Override
                    public void onQueryLostSuccess(List<Lost> obj) {}
                    @Override
                    public void onQueryFoundSuccess(List<Found> obj) {
                        founds.removeAll(founds);
                        founds.addAll(obj);
                        foundAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onFailed(BmobException e) {
                        NotificationUtil.showToast(e.toString());
                    }
                });
            }
        });
        UserUtil.queryFound(new UserListener() {
            @Override
            public void onSuccess(BmobObject object) {}
            @Override
            public void onQueryLostSuccess(List<Lost> obj) {}
            @Override
            public void onQueryFoundSuccess(List<Found> obj) {
                founds.removeAll(founds);
                founds.addAll(obj);
                foundAdapter.notifyDataSetChanged();}
            @Override
            public void onFailed(BmobException e) {
                NotificationUtil.showToast(e.toString());
            }
        });
    }


    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = BmobUser.getCurrentUser(User.class);
                for (int i=0;i<20;i++) {
                    Found lost = new Found();
                    lost.setUserName(user.getUsername());
                    lost.setAddress("重庆理工大学学生公寓门口");
                    lost.setBiaoqian("生活就是这样,你永远不知道下一刻会捡到什么！");
                    lost.setDate("2020年9月2日上午7点15");
                    lost.setName("雕牌洗衣服2包");
                    // lost.setImage(BitmapFactory.decodeResource(getResources(),R.drawable.image));
                    lost.setPhoneNumber("10086");
                    lost.setSubcribeTime(new BmobDate(new Date(System.currentTimeMillis())));
                    //lost.setPicture(BitmapFactory.decodeResource(getResources(),R.drawable.image));
                    founds.add(lost);
                    UserUtil.save(founds);
                }
            }
        }).start();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.found_bt_lost:
                bar.setTitle("看看有没有我掉的东西");
                Utility.Translate(this, LostActivity.class);

                break;
            case R.id.found_bt_found:
                bar.setTitle("看看大家捡到了什么");

                break;
            case R.id.found_bt_mine:
                bar.setTitle("个人中心");
                Utility.Translate(this, MineActivity.class);
                break;
            case R.id.found_bt_float:
                if(!BmobUser.isLogin()){NotificationUtil.showToast("请登录");return;}
                final User user=BmobUser.getCurrentUser(User.class);
                NotificationUtil.showAlertDialogPost(this, "好心人，请完善信息", new PostListener() {
                    @Override
                    public void onPosted(String name, String addr, String time, String phone) {
                        Found lost=new Found();
                        lost.setUserAddr(user.getAddress());
                        lost.setUserName(user.getUsername());
                        lost.setName(name);
                        lost.setNickName(user.getNickName());
                        lost.setPhoneNumber(phone);
                        lost.setAddress(addr);
                        lost.setDate(time);
                        lost.setBiaoqian("生活就是这样，永远不知道下一秒会捡到什么！");
                        lost.setSubcribeTime(new BmobDate(new Date(System.currentTimeMillis())));
                        UserUtil.addData(lost);
                    }
                }).show();

                break;
        }

    }
}
