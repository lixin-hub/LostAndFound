package com.example.belost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belost.UserSystem.Found;
import com.example.belost.UserSystem.Lost;
import com.example.belost.UserSystem.UserListener;
import com.example.belost.UserSystem.UserUtil;
import com.example.belost.Util.NotificationUtil;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_userName,ed_password;
    private Button bt_login;
    private TextView text_forgetPassword,text_signup;
    private int REQUEST_CODE=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,"e07c907e0021cad8b3a8125deb56d695");
        BmobConfig config =new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId("e07c907e0021cad8b3a8125deb56d695")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
        if (BmobUser.isLogin()){
            Intent in=new Intent(this, LostActivity.class);
            startActivity(in);
            finish();
        }
        initView();//初始化控件,设置点击事件监听器
    }

    private void initView(){
        bt_login=findViewById(R.id.login_button);
        ed_userName=findViewById(R.id.login_inout_userName);
        ed_password=findViewById(R.id.login_inout_password);
        text_forgetPassword=findViewById(R.id.login_forget_password);
        text_signup=findViewById(R.id.login_singup);
        bt_login.setOnClickListener(this);
        text_forgetPassword.setOnClickListener(this);
        text_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button://登录按钮
                String password=ed_password.getText().toString().trim();
                String userName=ed_userName.getText().toString().trim();
                if(password.equals("")||userName.equals("")){NotificationUtil.showToast("请输入密码或账号");return;}
               UserUtil.login(new UserListener() {
                   @Override
                   public void onSuccess(BmobObject object) {

                       Intent intent=new Intent(LoginActivity.this, LostActivity.class);
                       startActivity(intent);
                       finish();
                   }

                   @Override
                   public void onQueryLostSuccess(List<Lost> obj) {

                   }

                   @Override
                   public void onQueryFoundSuccess(List<Found> obj) {

                   }

                   @Override
                   public void onFailed(BmobException e) {

                   }
               }, view, userName, password);

                break;
            case R.id.login_forget_password://忘记密码
         NotificationUtil.showSnackbar(view,"忘了就忘了吧，重新注册一个就行了！");
                break;
            case R.id.login_singup://注册
                Intent intent=new Intent(this,SignupActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE&&resultCode==RESULT_OK){
            assert data != null;
            String userName=data.getStringExtra("userName");
            String password=data.getStringExtra("password");
            ed_password.setText(password);
            ed_userName.setText(userName);
        }
    }
}
