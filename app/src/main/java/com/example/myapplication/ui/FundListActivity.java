package com.example.myapplication.ui;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.myapplication.ui.adapter.TabAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FundListActivity extends AppCompatActivity {
    private static final String TAG = FundListActivity.class.getSimpleName();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private Button btnCalculate;
    private Button btnClear;
    private Button btnConfirm;
    private SearchView searchView;
    private ImageView ivFundListBack;
    private ImageView ivFundListMyHoldings;

    private List<String> tabTitleList;
    private List<Fragment> fragmentList;

    // ???????????? ??????
    private RelativeLayout rvFundListSelectTool;

    // ?????? ???????????????
    private TextView tvFundListSelectNum;

    private EditText etSearchView;

    private Spinner spinnerFundList;

    private boolean selectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);

        initData();
        initView();
        setUpTabBadge();

//        //?????????????????????
//        makeStatusBarTransparent(this);
//        //????????????????????????
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
        // ??????????????????
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
                // ????????? fragment ???????????????
                for(int i=0;i<fragmentList.size();i++){
                    FundListFragment fragment = (FundListFragment) fragmentList.get(i);
                    fragment.setSelectMode(true);
                }
                selectMode = true;
                Log.d(TAG,"??????Tab???"+ tabLayout.getSelectedTabPosition());
                //Constant.selectMode = true;
                FundListFragment fragment = (FundListFragment) fragmentList.get(tabLayout.getSelectedTabPosition());
                fragment.updateData();

                // ??????
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
                // todo ????????????
                intent.setClass(getApplication(), BuyConfirmActivity.class);
                startActivity(intent);
            }
        });

        rvFundListSelectTool = findViewById(R.id.rv_fundList_select_tool);
        tvFundListSelectNum = findViewById(R.id.tv_fundList_selectNum);

        searchView = findViewById(R.id.searchView);
        etSearchView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        etSearchView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //?????????????????????????????????newText??????????????????????????????
                Log.d(TAG,"query:"+query);
                Constant.content = query;
                // todo ????????????
                getQueryData(query,getOrder());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //?????????????????????????????????query????????????????????????
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
                Constant.order = position;
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
     *  ?????????????????? ????????????
     */
    private void getQueryData(String query,int order) {
        Log.d(TAG,"??????Tab???"+ tabLayout.getSelectedTabPosition()+"query:"+query+"order:"+order);
        // ????????? fragment ???????????????
        for(int i=0;i<fragmentList.size();i++){
            FundListFragment fragment = (FundListFragment) fragmentList.get(i);
            fragment.getSearchData(query, order);
        }
    }

    /**
     * ?????? ????????????
     * @param isIncrease ???????????????
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
            // ?????????ALL ??? ????????????
            if(position ==0 ){
                // ???????????????0
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
     * ??????Tablayout?????????????????????
     */
    private void setUpTabBadge() {
        for (int i = 0; i < tabTitleList.size() ; i++) {
            // ??????tab??????
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitleList.get(i));
            tabLayout.addTab(tab);

            // ??????????????????
            BadgeDrawable drawable = Objects.requireNonNull(tabLayout.getTabAt(i)).getOrCreateBadge();
            drawable.setNumber(0);
            drawable.setVisible(false);
            drawable.setBackgroundColor(Color.RED);
            drawable.setVerticalOffset(25);
            drawable.setHorizontalOffset(-20);
        }
    }

    // ?????? ?????? ?????????
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
     * EventBus ????????????
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
      Log.d(TAG,"???????????????");
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
          // ?????? all
          updateUpTabBadge(event.isIncrease,0);
          updateUpTabBadge(event.isIncrease,pos);
      }
    }

    // ????????????????????????
    private void  quantityExceedLimitToast(){
        Toast.makeText(this,"Only maximum of 5 funds can be selected.",Toast.LENGTH_SHORT).show();
    }

    /**
     * ?????? ??????
     */
    private void clearSelectedItem(){
        Constant.fundIdAndNameSet.clear();
        // ????????????
        for (int i = 0; i < tabTitleList.size(); i++) {
            BadgeDrawable drawable = Objects.requireNonNull(tabLayout.getTabAt(i)).getBadge();
            if(drawable != null){
                drawable.setNumber(0);
                drawable.setVisible(false);
            }
        }
        tvFundListSelectNum.setText("0");
        // ???????????? tab?????? ??????
        Log.d(TAG,"??????Tab???"+ tabLayout.getSelectedTabPosition());
        FundListFragment fragment = (FundListFragment) fragmentList.get(tabLayout.getSelectedTabPosition());
        fragment.updateData();
    }

    @Override
    public void onBackPressed() {
        if(selectMode){
            // ????????????
            // todo ??????Dialog
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setMessage("If you back, the selected funds will be clear.")
                    .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // ??????
                            // ????????? fragment ???????????????
                            for(int i=0;i<fragmentList.size();i++){
                                FundListFragment fragment = (FundListFragment) fragmentList.get(i);
                                fragment.setSelectMode(false);
                            }
                            selectMode = false;
                            Constant.fundIdAndNameSet.clear();
                            clearSelectedItem();
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