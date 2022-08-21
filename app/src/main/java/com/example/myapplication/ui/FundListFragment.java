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
    private RecyclerView rvFund;
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

    private boolean isNeedSearch;
    private boolean selectMode =false;
    private boolean isNeedUpdateSelectMode = false;


    private String content = "";
    private int order = 0;


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
        Log.d(TAG,"onCreate:"+position);
        initData();
    }

    public void initData(){
        mList = new ArrayList<>();
        mFundAdapter = new FundAdapter(mList,position);
        mFundAdapter.setFundAdapterListener(new FundAdapter.FundAdapterListener() {
            @Override
            public boolean getSelectMode() {
                return selectMode;
            }
        });
        isNeedSearch = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fund_list, container, false);
        initView(view);
        Log.d(TAG,"onCreateView:"+position);
        getSearchData(content,order);
        return view;
    }

    public void  initView(View view){

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
        this.content = content;
        this.order = order;
        // 请求数据  content、fundType、order、pageNum、pageSize = 10
        String fundType = getFundType();
        int pageNum = currentPage;
        int pageSize = 15;
        Log.d(TAG,"pos:"+position+" getSearchData： content: "+ content+ " fundType: "+ fundType +" order: " +order +" pageNum :"+pageNum + " isNeedSearch "+isNeedSearch);

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

        // todo 解析 totalPages
        // totalPages
        // 取消更新
        isNeedSearch = false;
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
        rvFund.post(new Runnable() {
            @Override
            public void run() {
                // todo 注意数据的起始位置
                mFundAdapter.notifyItemRangeInserted(oldSize,list.size());
                //mFundAdapter.notifyDataSetChanged();
            }
        });
        // 判断是否请求完毕
        if(currentPage == totalPages){
            isLastPage = true;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart:"+position);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume:"+position);
        if(isNeedSearch){
            getSearchData(content,order);
        }else if (isNeedUpdateSelectMode){
            mFundAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据模式  浏览模式-多选模式
     */
    public void  updateDataMode(){
        mFundAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        Log.d(TAG,"onPause:"+position);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG,"onPause:"+position);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG,"onDestroyView:"+position);
        super.onDestroyView();
    }

    public void setNeedSearch(String content,int order) {
        if(content.equals(this.content) && this.order == order){
           // 搜索条件 不变，不用更新
            isNeedSearch = false;
        }else{
            isNeedSearch = true;
            this.content = content;
            this.order = order;
        }
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        isNeedUpdateSelectMode = true;
    }
}