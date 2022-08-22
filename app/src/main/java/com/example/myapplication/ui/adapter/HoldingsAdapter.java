package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.bean.Fund;
import com.example.myapplication.bean.FundHoldings;
import com.example.myapplication.ui.Event;
import com.example.myapplication.ui.FundActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

public class HoldingsAdapter extends RecyclerView.Adapter  {
    private static final String TAG = HoldingsAdapter.class.getSimpleName();
    private List<FundHoldings> mList;
    private Context mContext;

    public HoldingsAdapter(List<FundHoldings> list) {
        this.mList = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_holding_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        FundHoldings fundHoldings =  mList.get(position);
        viewHolder.tvItemHoldingsName.setText(fundHoldings.getFundName());
        viewHolder.tvItemHoldingsAmount.setText(fundHoldings.getFundAmount());
        String yesterdayEarnings = fundHoldings.getFundYesterdayEarnings();
        if(yesterdayEarnings.startsWith("-")){
            viewHolder.tvItemHoldingsYesterdayEarnings.setTextColor(mContext.getResources().getColor(R.color.green));
        }else{
            viewHolder.tvItemHoldingsYesterdayEarnings.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        viewHolder.tvItemHoldingsYesterdayEarnings.setText(yesterdayEarnings);
        String earnings = fundHoldings.getFundTotalEarnings();
        if(earnings.startsWith("-")){
            viewHolder.tvItemHoldingsEarnings.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            viewHolder.tvItemHoldingsEarnings.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        viewHolder.tvItemHoldingsEarnings.setText(earnings);
        String earningsRate = fundHoldings.getFundTotalRate();
        if (earningsRate.startsWith("-")) {
            viewHolder.tvItemHoldingsEarningsRate.setTextColor(mContext.getResources().getColor(R.color.green));
        }else{
            viewHolder.tvItemHoldingsEarningsRate.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        viewHolder.tvItemHoldingsEarningsRate.setText(earningsRate+"%");

     }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemHoldingsName;
        private TextView tvItemHoldingsAmount;
        private TextView tvItemHoldingsYesterdayEarnings;
        private TextView tvItemHoldingsEarnings;
        private TextView tvItemHoldingsEarningsRate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           tvItemHoldingsName = itemView.findViewById(R.id.tv_item_holdings_name);
           tvItemHoldingsAmount = itemView.findViewById(R.id.tv_item_holdings_amount);
           tvItemHoldingsYesterdayEarnings = itemView.findViewById(R.id.tv_item_holdings_yesterday_earnings);
           tvItemHoldingsEarnings = itemView.findViewById(R.id.tv_item_holdings_earnings);
           tvItemHoldingsEarningsRate = itemView.findViewById(R.id.tv_item_holdings_earnings_rate);
        }
    }

}
