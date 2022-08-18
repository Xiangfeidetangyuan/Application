package com.example.myapplication;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FundListActivity extends AppCompatActivity {
    private static final String TAG = FundListActivity.class.getSimpleName();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private Button btnCalculate;
    private Button btnClear;
    private Button btnConfirm;
    private SearchView searchView;

    private List<String> tabTitleList;
    private List<Fragment> fragmentList;

    // 选择基金 提示
    private RelativeLayout rvFundListSelectTool;

    // 选择 基金的个数
    private TextView tvFundListSelectNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);

        initData();
        initView();
        setUpTabBadge();

        //设置状态栏透明
        makeStatusBarTransparent(this);
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    private void initData() {
        tabTitleList = new ArrayList<>();
        tabTitleList.add("All");
        tabTitleList.add("Money");
        tabTitleList.add("Bond");
        tabTitleList.add("Blend");
    }

    private void initView(){
        viewPager2 = findViewById(R.id.viewPager2);
        tabLayout =  findViewById(R.id.tabLayout);

        FragmentManager fm = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        for (int i = 0; i < tabTitleList.size(); i++) {
            fragmentList.add(FundListFragment.newInstance(i));
        }
        tabAdapter = new TabAdapter(fm, getLifecycle(),fragmentList);
        viewPager2.setAdapter(tabAdapter);
        // 设置禁止滑动
        viewPager2.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
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
                Log.d(TAG,"当前Tab："+ tabLayout.getSelectedTabPosition());
                FundListFragment fragment = (FundListFragment) fragmentList.get(tabLayout.getSelectedTabPosition());
                fragment.setDataMode(true);
                // 隐藏
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

            }
        });

        rvFundListSelectTool = findViewById(R.id.rv_fundList_select_tool);
        tvFundListSelectNum = findViewById(R.id.tv_fundList_selectNum);

        searchView = findViewById(R.id.searchView);

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
                }else {
                    tvFundListSelectNum.setText(cur+"");
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

    // 设置 透明 状态栏
    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
        // 通知当前 tab进行 刷新
        Log.d(TAG,"当前Tab："+ tabLayout.getSelectedTabPosition());
        FundListFragment fragment = (FundListFragment) fragmentList.get(tabLayout.getSelectedTabPosition());
        fragment.updateData();
    }

}