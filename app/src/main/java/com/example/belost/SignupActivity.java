package com.example.belost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belost.UserSystem.Found;
import com.example.belost.UserSystem.Lost;
import com.example.belost.UserSystem.UserListener;
import com.example.belost.UserSystem.UserUtil;
import com.example.belost.Util.NotificationUtil;
import com.example.belost.Util.Params;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final EditText ed_username,ed_password,ed_password_again;
        Button bt_signup;
        ed_username=findViewById(R.id.signup_inout_userName);
        ed_password=findViewById(R.id.signup_inout_password);
        ed_password_again=findViewById(R.id.signup_inout_password_again);
        bt_signup=findViewById(R.id.signup_button);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = ed_password.getText().toString().trim();
                String passwordAgain = ed_password_again.getText().toString().trim();
                final String userName = ed_username.getText().toString().trim();
                if (userName.toCharArray().length < 6) {
                    NotificationUtil.showToast("账号长度必学在6——15之间");
                    return;
                }
                if (password.toCharArray().length < 6) {
                    NotificationUtil.showToast("密码长度必学在6——10之间");
                    return;
                }
                if (password == null) {
                    NotificationUtil.showToast("请输入密码");
                    return;
                }
                if (ed_password_again == null) {
                    NotificationUtil.showToast("请输入密码");
                    return;
                }
                if (userName == null) {
                    NotificationUtil.showToast("请输入账号");
                    return;
                }
                if (!password.equals(passwordAgain)) {
                    NotificationUtil.showToast("两次输入的密码不一致");
                    return;
                }
                UserUtil.signUp(new UserListener() {
                    @Override
                    public void onSuccess(BmobObject object) {
                        Intent intent = new Intent();
                        intent.putExtra("userName", userName);
                        intent.putExtra("password", password);
                        setResult(RESULT_OK, intent);
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
                }, userName, password);
            }
            });

    }
}
