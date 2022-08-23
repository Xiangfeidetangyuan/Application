package com.example.myapplication.ui;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biz.user.BuyConfirmActivity;
import com.example.myapplication.Constant;
import com.example.myapplication.R;
import com.example.myapplication.bean.Fund;
import com.example.myapplication.ui.adapter.TabAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author zhuangyuan.ji
 */
public class FundListActivity extends AppCompatActivity {
    private static final String TAG = FundListActivity.class.getSimpleName();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private Button btnCalculate;
    private Button btnClear;
    private Button btnConfirm;
    private SearchView searchView;
    private EditText etSearchView;
    private ImageView ivFundListBack;
    private ImageView ivFundListMyHoldings;
    // 选择基金 提示
    private RelativeLayout rvFundListSelectTool;

    // 选择 基金的个数
    private TextView tvFundListSelectNum;

    private Spinner spinnerFundList;

    private List<String> tabTitleList;
    private List<Fragment> fragmentList;

    private boolean selectMode;

    private int pageSize=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);

        initData();
        initView();
        setUpTabBadge();
        getInitData();
    }
    private void initData() {
        tabTitleList = new ArrayList<>();
        tabTitleList.add("All");
        tabTitleList.add("Money");
        tabTitleList.add("Bond");
        tabTitleList.add("Blend");

        selectMode = false;

        FragmentManager fm = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        for (int i = 0; i < tabTitleList.size(); i++) {
            fragmentList.add(FundListFragment.newInstance(i));
        }
        tabAdapter = new TabAdapter(fm, getLifecycle(),fragmentList);
    }

    private void initView(){
        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout =  findViewById(R.id.tabLayout);

        viewPager2.setAdapter(tabAdapter);
        // 设置禁止滑动
        viewPager2.setUserInputEnabled(false);
        // 设置离屏加载
        viewPager2.setOffscreenPageLimit(3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        btnCalculate = findViewById(R.id.btn_calculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMode = true;
                Log.d(TAG,"当前Tab："+ tabLayout.getSelectedTabPosition());

                for (int i = 0; i < fragmentList.size() ; i++) {
                    FundListFragment fragment = (FundListFragment) fragmentList.get(i);
                    fragment.setSelectMode(true);
                    fragment.updateDataMode();
                }
                // 隐藏
                ivFundListBack.setVisibility(VISIBLE);
                btnCalculate.setVisibility(View.GONE);
                btnClear.setVisibility(VISIBLE);
                btnConfirm.setVisibility(VISIBLE);
                rvFundListSelectTool.setVisibility(VISIBLE);
            }
        });

        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectedItem();
            }
        });

        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.putStringArrayListExtra("EXTRA_KEY_FUND_ID_AND_NAME_LIST",new ArrayList<>(Constant.fundIdAndNameSet));
                // todo 页面跳转
                intent.setClass(getApplication(), BuyConfirmActivity.class);
                startActivity(intent);
            }
        });

        rvFundListSelectTool = findViewById(R.id.rv_fundList_select_tool);
        tvFundListSelectNum = findViewById(R.id.tv_fundList_selectNum);

        searchView = findViewById(R.id.searchView);
        searchView.findViewById(androidx.appcompat.R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchView.setText("");
                Log.d(TAG,"searchView close");
                getQueryData("0",getOrder());
                searchView.clearFocus();
            }
        });
        etSearchView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        etSearchView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        // 设置 输入最大长度
        etSearchView.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(31)});
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //文字提交的时候哦回调，newText是最后提交搜索的文字
                Log.d(TAG,"query:"+query);
                // 发起请求
                getQueryData(query,getOrder());
                // 防止触发两次
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //在文字改变的时候回调，query是改变之后的文字
                return false;
            }
        });
        ivFundListBack = findViewById(R.id.iv_fundList_back);
        ivFundListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 onBackPressed();
            }
        });

        ivFundListMyHoldings = findViewById(R.id.iv_fundList_myholdings);
        ivFundListMyHoldings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplication(),MyHoldingsActivity.class);
                startActivity(intent);
            }
        });

        spinnerFundList = findViewById(R.id.spinner_fundList);
        spinnerFundList.setSelection(0,true);
        spinnerFundList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getQueryData(getQuery(),position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private int getOrder(){
        return spinnerFundList.getSelectedItemPosition();
    }
    private String getQuery(){
        return etSearchView.getText().toString();
    }

    /**
     *  调用后端接口 获取数据
     */
    private void getQueryData(String query,int order) {
        Log.d(TAG,"当前Tab："+ tabLayout.getSelectedTabPosition()+"query:"+query+"order:"+order);
        // 回到首页
        viewPager2.setCurrentItem(0,false);
        getSearchData(query,order);
        for (int i = 0; i < fragmentList.size(); i++) {
            FundListFragment fragment = (FundListFragment) fragmentList.get(i);
            fragment.setSearchCondition(query,order);
            fragment.updateDataMode();
        }
    }


    /**
     * 更新 角标状态
     * @param isIncrease 是否为增加
     * @param position tab position
     */
    private void updateUpTabBadge(boolean isIncrease,int position){
        BadgeDrawable drawable = Objects.requireNonNull(tabLayout.getTabAt(position)).getBadge();
        if(drawable != null){
            int cur;
            if(isIncrease){
                drawable.setVisible(true);
                 cur = drawable.getNumber();
                cur++;
                drawable.setNumber(cur);
            }else {
                cur = drawable.getNumber();
                cur--;
                drawable.setNumber(cur);
                if(cur ==0){
                    drawable.setVisible(false);
                }
            }
            // 当更新ALL 时 同步更新
            if(position ==0 ){
                // 判断是否为0
                if(cur ==0){
                    tvFundListSelectNum.setText("0");
                    btnConfirm.setEnabled(false);
                }else {
                    tvFundListSelectNum.setText(cur+"");
                    btnConfirm.setEnabled(true);
                }
            }
        }
    }

    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {
        for (int i = 0; i < tabTitleList.size() ; i++) {
            // 添加tab文本
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitleList.get(i));
            tabLayout.addTab(tab);

            // 设置角标位置
            BadgeDrawable drawable = Objects.requireNonNull(tabLayout.getTabAt(i)).getOrCreateBadge();
            drawable.setNumber(0);
            drawable.setVisible(false);
            drawable.setBackgroundColor(Color.RED);
            drawable.setVerticalOffset(25);
            drawable.setHorizontalOffset(-20);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart activity");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume activity");
        super.onResume();
    }

    /**
     *  首次 获取数据
     */
    private void getInitData() {
        // 请求数据  content、fundType、order、pageNum、pageSize
        String content = "";
        String fundType = "ALL";
        int order = 0;
        int pageNum =1 ;
        Log.d(TAG,"getFirstPage： content:"+ content+ " fundType: "+ fundType +" order: " +order +"pageNum:"+pageNum);
        // todo 网络调用
        List<Fund> list = new ArrayList<>();
        list.add(new Fund("MO","sda","Money","23"));
        list.add(new Fund("YU","sda","Money","23"));
        list.add(new Fund("PJ","sda","Blend","23"));
        list.add(new Fund("IJ","sda","Bond","23"));
        list.add(new Fund("UH","sda","Money","23"));
        list.add(new Fund("HN","sda","Bond","23"));
        list.add(new Fund("UJ","sda","Blend","23"));
        list.add(new Fund("HJ","sda","Money","23"));
        list.add(new Fund("YU","sda","Money","23"));
        list.add(new Fund("OK","sda","Money","23"));
        list.add(new Fund("PL","sda","Money","23"));
        list.add(new Fund("YU","sda","Money","23"));
        list.add(new Fund("YU","sda","Blend","23"));
        list.add(new Fund("UJ","sda","Bond","23"));

        // 进行过滤
        List<Fund> allList = list;
        List<Fund> moneyList = new ArrayList<>();
        List<Fund> bondList = new ArrayList<>();
        List<Fund> blendList = new ArrayList<>();
        for ( Fund fund: list) {
            switch (fund.getFundType()){
                case "Money":  moneyList.add(fund); break;
                case "Bond": bondList.add(fund); break;
                case "Blend": blendList.add(fund);break;
                default: break;
            }
        }
        ((FundListFragment) fragmentList.get(0)).addData(allList);
        ((FundListFragment) fragmentList.get(1)).addData(moneyList);
        ((FundListFragment) fragmentList.get(2)).addData(bondList);
        ((FundListFragment) fragmentList.get(3)).addData(blendList);
    }

    /**
     *todo 自测  获取搜索数据
     */
    private void getSearchData(String content,int order) {
        String fundType = "All";
        // 请求数据  content、fundType、order、pageNum、pageSize
        int pageNum =1 ;
        Log.d(TAG,"getFirstPage： content:"+ content+ " fundType: "+ fundType +" order: " +order +"pageNum:"+pageNum);
        // todo 网络调用
        List<Fund> list = new ArrayList<>();
        list.add(new Fund("jk","sda","Money","23"));
        list.add(new Fund("fv","sda","Money","23"));
        list.add(new Fund("cs","sda","Blend","23"));
        list.add(new Fund("tb","sda","Bond","23"));
        list.add(new Fund("gd","sda","Money","23"));
        list.add(new Fund("sf","sda","Bond","23"));
        list.add(new Fund("fg","sda","Blend","23"));
        list.add(new Fund("dg","sda","Money","23"));
        list.add(new Fund("rg","sda","Money","23"));
        list.add(new Fund("rt","sda","Money","23"));
        list.add(new Fund("er","sda","Money","23"));
        list.add(new Fund("ee","sda","Money","23"));
        list.add(new Fund("ew","sda","Blend","23"));
        list.add(new Fund("df","sda","Bond","23"));

        // 进行过滤
        List<Fund> allList = list;
        List<Fund> moneyList = new ArrayList<>();
        List<Fund> bondList = new ArrayList<>();
        List<Fund> blendList = new ArrayList<>();
        for ( Fund fund: list) {
            switch (fund.getFundType()){
                case "Money":  moneyList.add(fund); break;
                case "Bond": bondList.add(fund); break;
                case "Blend": blendList.add(fund);break;
                default: break;
            }
        }
        ((FundListFragment) fragmentList.get(0)).addData(allList);
        ((FundListFragment) fragmentList.get(1)).addData(moneyList);
        ((FundListFragment) fragmentList.get(2)).addData(bondList);
        ((FundListFragment) fragmentList.get(3)).addData(blendList);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * EventBus 消息接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
      Log.d(TAG,"接收到消息");
      if(event.quantityExceedLimit){
          quantityExceedLimitToast();
      }else {
          int pos = -1;
          switch (event.fund.getFundType()){
              case "Money": pos = 1; break;
              case "Bond": pos = 2;break;
              case "Blend": pos = 3; break;
              default: break;
          }
          if (pos == -1){
              Log.d(TAG, "data is wrong");
              return;
          }
          // 更新 all
          updateUpTabBadge(event.isIncrease,0);
          updateUpTabBadge(event.isIncrease,pos);
          // 通知对应 fragment进行更新
          if(pos == event.fragmentPosition){
              // 是子tab点击 通知all 更新
              ((FundListFragment)fragmentList.get(0)).updateDataMode();
          }else {
              // 通知子tab更新
              ((FundListFragment)fragmentList.get(pos)).updateDataMode();
          }
      }
    }

    // 数量超过限制弹窗
    private void  quantityExceedLimitToast(){
        Toast.makeText(this,"Only maximum of 5 funds can be selected.",Toast.LENGTH_SHORT).show();
    }

    /**
     * 清除 选择
     */
    private void clearSelectedItem(){
        Constant.fundIdAndNameSet.clear();
        // 清空角标
        for (int i = 0; i < tabTitleList.size(); i++) {
            BadgeDrawable drawable = Objects.requireNonNull(tabLayout.getTabAt(i)).getBadge();
            if(drawable != null){
                drawable.setNumber(0);
                drawable.setVisible(false);
            }
        }
        tvFundListSelectNum.setText("0");
        btnConfirm.setEnabled(false);
        for(int i=0;i< fragmentList.size();i++){
            // 通知当前 tab进行 刷新
            FundListFragment fragment = (FundListFragment) fragmentList.get(i);
            fragment.updateDataMode();
        }
    }

    @Override
    public void onBackPressed() {
        if(selectMode){
            // 弹窗提醒
            // todo 封装Dialog
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setMessage("If you back, the selected funds will be clear.")
                    .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 清除
                            Constant.fundIdAndNameSet.clear();
                            clearSelectedItem();
                            selectMode = false;
                            for(int i=0;i< fragmentList.size();i++){
                                // 通知当前 tab进行 刷新
                                FundListFragment fragment = (FundListFragment) fragmentList.get(i);
                                fragment.setSelectMode(false);
                                fragment.updateDataMode();
                            }
                            ivFundListBack.setVisibility(View.GONE);
                            rvFundListSelectTool.setVisibility(INVISIBLE);
                            btnConfirm.setVisibility(View.GONE);
                            btnClear.setVisibility(View.GONE);
                            btnCalculate.setVisibility(VISIBLE);
                        }
                    })
                    .setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

    }
}