package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.bean.Fund;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

public class FundAdapter extends RecyclerView.Adapter  {
    private static final String TAG = FundAdapter.class.getSimpleName();
    private Context mContext;
    private List<Fund> mList;
    private int fragmentPosition;


    public FundAdapter(List<Fund> list,int position) {
        this.mList = list;
        this.fragmentPosition = position;
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
        viewHolder.ivFundListRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"点击了"+ position);
                Intent intent = new Intent();
                intent.setClass(mContext,FundActivity.class);
                mContext.startActivity(intent);
            }
        });
        Fund fund =  mList.get(position);
        String fundName = fund.getFundName();
        String fundIdAndName = fund.getFundId()+" - "+ fundName;
        viewHolder.tv_fundList_fundId_and_name.setText(fundIdAndName);

       Set<String> fundIdAndNameSet = Constant.fundIdAndNameSet;
        if(Constant.selectMode){
            viewHolder.checkBoxFundList.setVisibility(View.VISIBLE);
        }else {
            viewHolder.checkBoxFundList.setVisibility(View.GONE);
        }
        viewHolder.checkBoxFundList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 若非 用户点击，直接返回
                if(!buttonView.isPressed()){
                    return;
                }
                Log.d(TAG,position+"isChanged");
                if(fundIdAndNameSet.contains(fundIdAndName)){
                    // 如果 包含，则撤销
                    fundIdAndNameSet.remove(fundIdAndName);
                    // todo 更新Tab
                    EventBus.getDefault().post(new Event(false,fund));
                }else {
                    // 如果不包含 ，则添加
                    // 添加前先进行数量校验
                    if(fundIdAndNameSet.size() == 5){
                        EventBus.getDefault().post(new Event(true));
                        viewHolder.checkBoxFundList.setChecked(false);
                    }else{
                        fundIdAndNameSet.add(fundIdAndName);
                        EventBus.getDefault().post(new Event(true,fund));
                    }
                }
                // todo 不要直接刷新数据，可以外部调用刷新数据
            }
        });
        if(fundIdAndNameSet.contains(fundIdAndName)){
            viewHolder.checkBoxFundList.setChecked(true);
        }else {
            viewHolder.checkBoxFundList.setChecked(false);
        }

        if (fragmentPosition == 0){
            viewHolder.tvFundListFundType.setText(new StringBuilder().append(fund.getFundType()).append(" |").toString());
        }else {
            viewHolder.tvFundListFundType.setVisibility(View.GONE);
        }
        viewHolder.tvFundListAnnualYieldsNumber.setText(fund.getFundAnnualYields());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxFundList;
        private TextView tv_fundList_fundId_and_name;
        private TextView tvFundListAnnualYieldsNumber;
        private TextView tvFundListFundType;
        private ImageView ivFundListRight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxFundList= itemView.findViewById(R.id.checkBox_fundList);
            tv_fundList_fundId_and_name = itemView.findViewById(R.id.tv_fundList_fundId_and_name);
            tvFundListAnnualYieldsNumber = itemView.findViewById(R.id.tv_fundList_annualYields_number);
            tvFundListFundType= itemView.findViewById(R.id.tv_fundList_fundType);
            ivFundListRight = itemView.findViewById(R.id.iv_fundList_right);
        }
    }
}
