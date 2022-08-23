package com.example.biz.user.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biz.user.R;

/**
 * @Description 购买结果
 * @author zhuangyuan.ji
 */
public class BuyResultActivity extends AppCompatActivity {

    private ImageView ivBuyResult;
    private TextView tvBuyResult;

    private ImageView ivBuyResultProcess;

    private Button btnBuyResultMyholdings;
    private Button btnBuyResultExit;

    // 支付结果
    private boolean result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_result);

        ivBuyResult = findViewById(R.id.iv_buy_result);
        tvBuyResult = findViewById(R.id.tv_buy_result);

        ivBuyResultProcess = findViewById(R.id.iv_buy_result_process);

        btnBuyResultExit = findViewById(R.id.btn_buy_result_exit);
        btnBuyResultMyholdings = findViewById(R.id.btn_buy_result_myholdings);
        btnBuyResultMyholdings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 页面跳转
            }
        });
        initView();
    }

    private void initView() {
        result = false;
        if(result){
            ivBuyResult.setImageResource(R.drawable.fail);
            tvBuyResult.setText("Buy Successfuly");
            btnBuyResultExit.setText("Done");
        }else{
            ivBuyResult.setImageResource(R.drawable.fail);
            ivBuyResultProcess.setVisibility(View.INVISIBLE);
            tvBuyResult.setText("Ops! Buy Failed");
            btnBuyResultExit.setText("Exit");
        }
    }
}