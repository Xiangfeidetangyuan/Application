package com.example.myapplication;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    Button btnCalculate;

    List<String> tabTitleList;
    List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setUpTabBadge();
        updateUpTabBadge(3);

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
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
        fragmentList.add(new AllFragment());
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
                AllFragment fragment = (AllFragment) fragmentList.get(tabLayout.getSelectedTabPosition());
                fragment.updateData();
            }
        });
    }

    private void updateUpTabBadge(int position){
        TabLayout.Tab tab =  tabLayout.getTabAt(position);
        View customView = tab.getCustomView();
       TextView textView =  customView.findViewById(R.id.badgeview_target);
       textView.setVisibility(VISIBLE);
       textView.setText("4");
    }

    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab =  tabLayout.getTabAt(i);

            //更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            View view = LayoutInflater.from(this).inflate(R.layout.tab_layout_item, null);
            TextView textView =  view.findViewById(R.id.textview);
            textView.setText(tabTitleList.get(i));
            TextView target = view.findViewById(R.id.badgeview_target);
            target.setVisibility(View.INVISIBLE);
            tab.setCustomView(view);
        }

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Fund fund) {
      Log.d(TAG,"接收到消息");

    }

}