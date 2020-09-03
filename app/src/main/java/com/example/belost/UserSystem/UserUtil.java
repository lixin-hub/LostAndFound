package com.example.belost.UserSystem;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;

import androidx.annotation.VisibleForTesting;

import com.example.belost.LostActivity;
import com.example.belost.Util.NotificationUtil;
import com.example.belost.Util.Params;
import com.google.android.material.snackbar.Snackbar;

import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.internal.operators.flowable.FlowablePublish;

public class UserUtil {
    public static void signUp(final UserListener listener, String userName, final String password){
                    User user=new User();
                    user.setUsername(userName);
                    user.setPassword(password);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User o, BmobException e) {
                            if (e == null) {
                                listener.onSuccess(o);
                                NotificationUtil.showToast("注册成功");
                            } else {
                                listener.onFailed(e);
                                NotificationUtil.showToast("注册失败" + "\n" + e.toString());
                            }
                        }
                    });
                    }
    public static void login(final UserListener listener, final  View view , String userName, String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(userName);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.onSuccess(user);
                    NotificationUtil.showSnackbar(view, "登录成功");
                } else {
                    listener.onFailed(e);
                    NotificationUtil.showSnackbar(view, "登录失败" + "\n" + e.toString());
                }

            }
        });
    }

    public static void addData(BmobObject object){

        object.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    NotificationUtil.showToast("添加数据成功，返回objectId为："+objectId);
                }else{
                    NotificationUtil.showToast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public static  void save(final List<BmobObject> lists)  {
        new BmobBatch().insertBatch(lists).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> results, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < results.size(); i++) {
                        BatchResult result = results.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            lists.get(i).setObjectId(result.getObjectId());
                            NotificationUtil.showToast("第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            NotificationUtil.showToast( "第" + i + "个数据批量添加失败：" + ex.getMessage() );

                        }
                    }
                } else {
                    NotificationUtil.showToast( "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public static void update(BmobObject object, final View view) {
        object.update(object.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                   NotificationUtil.showSnackbar(view,"更新成功");
                } else {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void delete(BmobObject object, final View view) {

        object.delete(object.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Snackbar.make(view, "删除成功", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void queryLost(final UserListener listener) {
        BmobQuery<Lost> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> lost, BmobException e) {
                if (e == null) {
                    Collections.reverse(lost);
                    listener.onQueryLostSuccess(lost);
                    NotificationUtil.showToast("查询成功");
                } else {
                 listener.onFailed(e);
                }
            }
        });
    }
    public static void queryFound(final UserListener listener) {
        BmobQuery<Found> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Found>() {
            @Override
            public void done(List<Found> lost, BmobException e) {
                if (e == null) {
                    Collections.reverse(lost);
                    listener.onQueryFoundSuccess(lost);
                    NotificationUtil.showToast("查询成功");
                } else {
                    listener.onFailed(e);
                }
            }
        });
    }
    public static void query(String id) {

        BmobQuery<BmobObject> bmobQuery = new BmobQuery<BmobObject>();
        bmobQuery.getObject(id, new QueryListener<BmobObject>() {
            @Override
            public void done(BmobObject bmobQuery, BmobException e) {

            }
        });
    }
    /**
     * 提供旧密码修改密码
     */
    public static void updatePassword(String oldPwd,String newPwd){
if(!BmobUser.isLogin()){NotificationUtil.showToast("请登录");return;}
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    NotificationUtil.showToast( "修改成功");
                } else {
                    NotificationUtil.showToast( "修改失败"+e.toString());
                }
            }
        });
    }
}
