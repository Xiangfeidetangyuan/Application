package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.bean.Fund;
import com.example.myapplication.ui.adapter.FundAdapter;

import java.util.ArrayList;
import java.util.List;

public class FundListFragment extends Fragment {

    private static final String TAG = FundListFragment.class.getSimpleName();
    RecyclerView rvFund;
   private LinearLayoutManager mLinearLayoutManager;
   private FundAdapter mFundAdapter;
   private List<Fund> mList;

    // 标识 要接收 的参数
    private static final String ARG_PARAM1 = "param1";
   // 第几个Tab
   private int position;


    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPages = 2 ;


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
        getNextPage();
        return view;
    }

    public void initData(){
        mList = new ArrayList<>();
        }

    public void  initView(View view){
        mFundAdapter = new FundAdapter(mList,position);
        rvFund = view.findViewById(R.id.rv_fund);
        rvFund.setAdapter(mFundAdapter);

        // 添加动画
        rvFund.setItemAnimator(new DefaultItemAnimator());
        //  纵向滑动
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // 设置布局管理器
        rvFund.setLayoutManager(mLinearLayoutManager);

        //添加Android自带的分割线
        rvFund.addItemDecoration(new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL));
        rvFund.addOnScrollListener(new RecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            protected void loadMoreItems() {
               currentPage++;
               getNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

        });
    }

    public void getSearchData(String content,int order){
        currentPage = 1;
        isLastPage = false;
        // 请求数据  content、fundType、order、pageNum、pageSize = 10
        String fundType = getFundType();
        int pageNum = currentPage;
        int pageSize = 10;
        Log.d(TAG,"pos:"+position+" getSearchDate： content:"+ content+ " fundType: "+ fundType +" order: " +order +"pageNum:"+pageNum);

        // todo 网络调用
        List<Fund> list = new ArrayList<>();
        list.add(new Fund("sf","sda","Money","23"));
        list.add(new Fund("we","sda","Money","23"));
        list.add(new Fund("ke","sda","Money","23"));
        list.add(new Fund("vg","sda","Money","23"));
        list.add(new Fund("hy","sda","Money","23"));
        list.add(new Fund("in","sda","Money","23"));
        list.add(new Fund("gh","sda","Money","23"));
        list.add(new Fund("ry","sda","Money","23"));
        list.add(new Fund("vr","sda","Money","23"));
        list.add(new Fund("rt","sda","Money","23"));

        int oldSize = mList.size();
        // 清除之前数据
        if(mList.size() >0 ){
            mList.clear();
            mFundAdapter.notifyItemRangeRemoved(0,oldSize);
        }
        mList.addAll(list);
        mFundAdapter.notifyItemRangeInserted(0, mList.size());

    }
    private String getFundType(){
        if(position == 0){
            return "ALL";
        }else if(position == 1){
            return "MONEY";
        }else if(position == 2){
            return "BOND";
        }
        return "BLEND";
    }

    private void getNextPage() {
        // 请求数据  content、fundType、order、pageNum、pageSize = 7
        String fundType = getFundType();
        int pageNum = currentPage;
        String content = Constant.content;
        int order = Constant.order;
        Log.d(TAG,"pos:"+position+" getNextPage： content:"+ content+ " fundType: "+ fundType +" order: " +order +"pageNum:"+pageNum);
        // todo 网络调用
        List<Fund> list = new ArrayList<>();
        list.add(new Fund("01","sda","Money","23"));
        list.add(new Fund("12","sda","Money","23"));
        list.add(new Fund("23","sda","Money","23"));
        list.add(new Fund("34","sda","Money","23"));
        list.add(new Fund("45","sda","Money","23"));
        list.add(new Fund("56","sda","Money","23"));
        list.add(new Fund("67","sda","Money","23"));
        list.add(new Fund("78","sda","Money","23"));
        list.add(new Fund("89","sda","Money","23"));
        list.add(new Fund("91","sda","Money","23"));
        int oldSize = mList.size();
        mList.addAll(list);
        if(oldSize ==0){

        }else{
            rvFund.post(new Runnable() {
                @Override
                public void run() {
                    // todo 注意数据的起始位置
                    mFundAdapter.notifyItemRangeInserted(oldSize,list.size());
                    //mFundAdapter.notifyDataSetChanged();
                }
            });
        }

        // todo 解析 totalPages，并进行比较
        // 判断是否请求完毕
        if(currentPage == totalPages){
            isLastPage = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFundAdapter.notifyDataSetChanged();
    }

    public void  updateData(){
        mFundAdapter.notifyDataSetChanged();
    }


}