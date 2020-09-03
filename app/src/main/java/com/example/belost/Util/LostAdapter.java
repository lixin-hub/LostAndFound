package com.example.belost.Util;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belost.R;
import com.example.belost.UserSystem.Lost;
import com.example.belost.UserSystem.User;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> {
    List<BmobObject> losts;

    public LostAdapter(List<BmobObject> losts) {
        this.losts = losts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lost_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Lost lost=(Lost) losts.get(position);
      holder.image.setImageResource(R.drawable.image);
      holder.phoneNunber.setText(lost.getPhoneNumber());
      holder.biaoqian.setText(lost.getBiaoqian());
      holder.subcribeTime.setText(lost.getSubcribeTime().getDate());
        String addr=lost.getUserAddr();
        if (addr==null){addr="";}
        if(lost.getNickName()!=null){holder.userName.setText(lost.getNickName()+" "+addr);}
        else{ holder.userName.setText(lost.getUserName()+" "+addr);}
      holder.lostName.setText(lost.getName());
      holder.lostAddress.setText(lost.getAddress());
      holder.lostPicture.setImageResource(R.drawable.picture);
      holder.lostTime.setText(lost.getDate());
    }


    @Override
    public int getItemCount() {
        return losts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView lostPicture,image;
        TextView lostName,userName,lostAddress,lostTime,subcribeTime,biaoqian,phoneNunber;

        public ViewHolder(View view){
          super(view);
          image=view.findViewById(R.id.lost_image);
          lostPicture=view.findViewById(R.id.lost_picture);
            lostName=view.findViewById(R.id.lost_name);
            lostAddress=view.findViewById(R.id.lost_address);
            userName =view.findViewById(R.id.lost_username);
            lostTime=view.findViewById(R.id.lost_time);
            subcribeTime=view.findViewById(R.id.lost_subcribe_time);
            biaoqian=view.findViewById(R.id.lost_biaoqian);
            phoneNunber=view.findViewById(R.id.lost_phone);
      }
    }
}
