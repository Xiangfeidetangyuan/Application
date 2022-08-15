package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

public class FundAdapter extends RecyclerView.Adapter  {
    private static final String TAG = FundAdapter.class.getSimpleName();
    private Context mContext;
    private List<Fund> mList;


    public FundAdapter(List<Fund> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext  = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_fund_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        // 给 子项设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"点击了"+ position);
                Intent intent = new Intent();
                intent.setClass(mContext,FundActivity.class);
                mContext.startActivity(intent);
            }
        });

        String fundName = mList.get(position).getName();
        viewHolder.tvFund.setText(fundName);

        Set<String> fundSet = AllFragment.fundSet;
        if(AllFragment.selectMode){
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 若非 用户点击，直接返回
                if(!buttonView.isPressed()){
                    return;
                }
                Log.d(TAG,position+"isChanged");
                if(fundSet.contains(fundName)){
                    // 如果 包含，则撤销
                    fundSet.remove(fundName);
                    // todo 更新Tab
                    EventBus.getDefault().post(mList.get(position));
                }else {
                    // 如果不包含 ，则添加
                    fundSet.add(fundName);
                    EventBus.getDefault().post(mList.get(position));
                }
                // todo 不要直接刷新数据，可以外部调用刷新数据
            }
        });
        if(fundSet.contains(fundName)){
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView tvFund;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            tvFund = itemView.findViewById(R.id.tv_fund);
        }
    }
}
