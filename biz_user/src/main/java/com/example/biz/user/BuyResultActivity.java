package com.example.biz.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description 购买结果
 * @author zhuangyuan.ji
 */
public class BuyResultActivity extends AppCompatActivity {

    private ImageView ivBuyResult;
    private TextView tvBuyResult;

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


        btnBuyResultExit = findViewById(R.id.btn_buy_result_exit);
        btnBuyResultMyholdings = findViewById(R.id.btn_buy_result_myholdings);

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
            tvBuyResult.setText("Ops! Buy Failed");
            btnBuyResultExit.setText("Exit");
        }
    }
}