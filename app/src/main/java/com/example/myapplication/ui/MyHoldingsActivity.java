package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.FundHoldings;
import com.example.myapplication.ui.adapter.HoldingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyHoldingsActivity extends AppCompatActivity {

    private TextView tvMyholdingsTotalAmount;
    private TextView tvMyholdingsYesterdayEarning;
    private TextView tvMyholdingsTotalEarning;

    // 返回
    private ImageView ivMyholdingsBack;

    private RecyclerView rvMyholdings;

    private HoldingsAdapter holdingsAdapter;
    private List<FundHoldings> fundHoldingsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myholdings);

        initData();
        initView();


    }

    private void initData() {
        fundHoldingsList = new ArrayList<>();
        holdingsAdapter = new HoldingsAdapter(fundHoldingsList);

        for(int i=0; i<7;i++){
            FundHoldings fundHoldings = new FundHoldings("12343","xxxxxxxxx",
                    "897.6","-12.39","10.74","10.27");
             fundHoldingsList.add(fundHoldings);
        }

    }

    private void initView() {
        tvMyholdingsTotalAmount = findViewById(R.id.tv_myholdings_total_amount);
        tvMyholdingsYesterdayEarning  = findViewById(R.id.tv_myholdings_yesterday_earning);
        tvMyholdingsTotalEarning = findViewById(R.id.tv_myholdings_total_earning);

        ivMyholdingsBack = findViewById(R.id.iv_myholdings_back);
        ivMyholdingsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo 跳转到首页
            }
        });

        tvMyholdingsTotalAmount.setText("897.26");
        tvMyholdingsYesterdayEarning.setText("XX.XX");
        tvMyholdingsTotalEarning.setText("XX.XX");


        rvMyholdings = findViewById(R.id.rv_myholdings);
        rvMyholdings.setAdapter(holdingsAdapter);
        rvMyholdings.setItemAnimator(new DefaultItemAnimator());
        //  纵向滑动
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // 设置布局管理器
        rvMyholdings.setLayoutManager(mLinearLayoutManager);
        //添加Android自带的分割线
        rvMyholdings.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


    }
}