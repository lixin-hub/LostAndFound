package com.example.belost.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnCitySelectListener;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.example.belost.MyApplication;
import com.example.belost.R;
import com.example.belost.UserSystem.PostListener;
import com.example.belost.UserSystem.User;
import com.example.belost.UserSystem.UserUtil;
import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class NotificationUtil {

    public static void showToast(String content){
        Toast toast=Toast.makeText(MyApplication.getContext(),content,Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showSnackbar(View view,String content){
       Snackbar snackbar=Snackbar.make(view,content,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public static AlertDialog.Builder  showAlertDialogPost(Activity context, String title, final PostListener listener_pos) {
        View view=context.getLayoutInflater().inflate(R.layout.post_layout,null);
        final EditText edit_name,edit_addr,edit_time,edit_phone;
        edit_addr=view.findViewById(R.id.post_address);
        edit_time=view.findViewById(R.id.post_time);
        edit_phone=view.findViewById(R.id.post_phone);
        edit_name=view.findViewById(R.id.post_name);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String addr=edit_addr.getText().toString();
                String name=edit_name.getText().toString();
                String time=edit_time.getText().toString();
                String phone=edit_phone.getText().toString();
                listener_pos.onPosted(name+"",addr+"",time+"",phone+"");
            }
        });
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        return  builder;
    }
    public static AlertDialog.Builder  showAlertDialogNickname(Activity context, final View view1, final TextView nickname) {
        final View view=context.getLayoutInflater().inflate(R.layout.change_nickname,null);
        final EditText edit_old;
        edit_old=view.findViewById(R.id.change_nick);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("修改昵称");
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mold=edit_old.getText().toString();
                final User user=BmobUser.getCurrentUser(User.class);
                user.setNickName(mold);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            NotificationUtil.showSnackbar(view1,"更新成功");
                            nickname.setText(user.getNickName());
                        }
                        else
                        {
                            NotificationUtil.showSnackbar(view1,"更新失败"+e.toString());
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        return  builder;
    }
    public static AlertDialog.Builder  showAlertDialogPassword(Activity context) {
        View view=context.getLayoutInflater().inflate(R.layout.change_password,null);
        final EditText edit_old,edit_new;
        edit_old=view.findViewById(R.id.change_old);
        edit_new=view.findViewById(R.id.change_new);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("修改密码");
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mold=edit_old.getText().toString();
                String mnew=edit_new.getText().toString();
                UserUtil.updatePassword(mold,mnew);
            }
        });
        builder.setNegativeButton("返回",null);
        builder.setView(view);
        return  builder;
    }
    public static ProgressDialog showProgressDialog(Context context,String title,String message){
        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        return  dialog;
    }
    public static void showPickerView(final Context context, OnCitySelectListener listener){
        CityPickerView mCityPickerView = new CityPickerView(context);
        // 设置点击外部是否消失
        mCityPickerView.setCancelable(true);
        // 设置滚轮字体大小
        mCityPickerView.setTextSize(20f);
        // 设置标题
        mCityPickerView.setTitle("选择城市");
        // 设置取消文字
        mCityPickerView.setCancelText("取消");
        // 设置取消文字颜色
        mCityPickerView.setCancelTextColor(Color.RED);
        // 设置取消文字大小
        mCityPickerView.setCancelTextSize(15f);
        // 设置确定文字
        mCityPickerView.setSubmitText("确定");
        // 设置确定文字颜色
        mCityPickerView.setSubmitTextColor(Color.BLUE);
        // 设置确定文字大小
        mCityPickerView.setSubmitTextSize(15f);
        // 设置头部背景
        mCityPickerView.setHeadBackgroundColor(Color.GRAY);
        mCityPickerView.setOnCitySelectListener(listener);
        mCityPickerView.show();

    }
}
