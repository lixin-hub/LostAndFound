package com.example.belost.UserSystem;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public interface UserListener {
    public void onSuccess(BmobObject object);
    public void onQueryLostSuccess(List<Lost> obj);
    public void onQueryFoundSuccess(List<Found> obj);
    public void onFailed(BmobException e);
}
