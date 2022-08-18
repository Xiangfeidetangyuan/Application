package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.bean.Fund;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FundListFragment extends Fragment {

   private RecyclerView rvFund;
   private RecyclerView.LayoutManager mLayoutManager;
   private FundAdapter fundAdapter;
   private List<Fund> list;

    // 标识 要接收 的参数
    private static final String ARG_PARAM1 = "param1";
   // 第几个Tab
   private int position;

    public FundListFragment() {
        // Required empty public constructor
    }

    public static FundListFragment newInstance(int position) {
        FundListFragment fragment = new FundListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1,position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fund_list, container, false);
        initData();
        initView(view);
        return view;
    }

    public void initData(){
        list = new ArrayList<>();
        list.add(new Fund("12","sda","Money","23"));
        list.add(new Fund("123","sda","Money","23"));
        list.add(new Fund("1234","sda","Money","23"));
        list.add(new Fund("123456","sda","Money","23"));
        list.add(new Fund("23","sda","Bond","23"));
        list.add(new Fund("34","sda","Blend","23"));
    }

    public void  initView(View view){

        fundAdapter = new FundAdapter(list,position);
        rvFund = view.findViewById(R.id.rv_fund);
        rvFund.setAdapter(fundAdapter);

        // 添加动画
        rvFund.setItemAnimator(new DefaultItemAnimator());
        //  纵向滑动
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // 设置布局管理器
        rvFund.setLayoutManager(mLayoutManager);

        //添加Android自带的分割线
        rvFund.addItemDecoration(new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();
        fundAdapter.notifyDataSetChanged();
    }

    /**
     * 设置 数据模式
     */
    public void setDataMode(boolean selectMode){
        Constant.selectMode = selectMode;
        fundAdapter.notifyDataSetChanged();
    }
    public void  updateData(){
        fundAdapter.notifyDataSetChanged();
    }


}