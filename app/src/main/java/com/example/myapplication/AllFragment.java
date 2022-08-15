package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllFragment extends Fragment {
   private View view;
   private RecyclerView rvFund;
   private RecyclerView.LayoutManager mLayoutManager;
   private FundAdapter fundAdapter;
   private List<Fund> list;
   //被选择的 基金 代码/名称
   public static Set<String> fundSet = new HashSet<>();
   public static boolean selectMode = false;

   // 第几个Tab
   private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all, container, false);
        initView();
        // Inflate the layout for this fragment
        return view;
    }



    public void  initView(){
        list = new ArrayList<>();
        list.add(new Fund("12","Money"));
        list.add(new Fund("23","Bond"));
        list.add(new Fund("34","Blend"));
        fundAdapter = new FundAdapter(list);
        rvFund = view.findViewById(R.id.rv_fund);
        rvFund.setAdapter(fundAdapter);

        // 添加动画
        rvFund.setItemAnimator(new DefaultItemAnimator());
        //  纵向滑动
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // 设置布局管理器
        rvFund.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        fundAdapter.notifyDataSetChanged();
    }

    public void  updateData(){
        selectMode = ! selectMode;
        fundAdapter.notifyDataSetChanged();
    }


}