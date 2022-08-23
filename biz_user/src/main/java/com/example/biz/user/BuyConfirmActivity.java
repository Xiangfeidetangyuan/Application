package com.example.biz.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BuyConfirmActivity extends AppCompatActivity {
    // 用于指定 text
    private TextView tvBuyConfirmFundFee;
    private TextView tvBuyConfirmFundTime;
    private TextView tvBuyConfirmProtocol;
    private TextView tvBuyConfirmProduct;

    // 支付金额
    private TextView tvBuyConfirmFundAmount;

    // 定投周期
    private TextView tvBuyConfirmInvest;

    // 下次买入时间
    private TextView tvBuyConfirmNextInvest;
    // 显示 定投情况
    private RelativeLayout rvBuyConfirmInvest;

    private Button btnBuyConfirmConfirm;

    private ImageView ivBuyConfirmBack;
    private CheckBox checkBoxBuyConfirm;

    public BuyConfirmActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirm);

        tvBuyConfirmFundFee = findViewById(R.id.tv_buy_confirm_fund_fee);
        tvBuyConfirmFundTime = findViewById(R.id.tv_buy_confirm_fund_time);
        tvBuyConfirmProtocol = findViewById(R.id.tv_buy_confirm_protocol);
        String fee = String.format("Estimated Subscription Fee: <font color=\"#FAAD14\">%s</> SGD %s","0.8","(Fee Rate:0.08%)");
        tvBuyConfirmFundFee.setText(Html.fromHtml(fee));
        tvBuyConfirmFundTime.setText("10 Aug"+" will confirm share according to net asset value "+ "9 Aug.");
        String protocol = String.format("<font color=\"#959191\">%s</><font color=\"#1677FF\">%s</><font color=\"#959191\">%s</><font color=\"#1677FF\">%s</><font color=\"#959191\">%s</>",
                "点击确定即代表您已知悉该基金的","产品概要","和","投资人权益须知","等相关内容") ;
        tvBuyConfirmProtocol.setText(Html.fromHtml(protocol));
        tvBuyConfirmProduct = findViewById(R.id.tv_buy_confirm_product);
        String product = String.format("<ul>\n" +
                "            <li>基金简称：金元顺安元灵活配置 </li>\n" +
                "            <li>基金代码：004685</li>\n" +
                "            <li>基金代理人：金元顺安基金管理有限公司</li>\n" +
                "        </ul>");
        tvBuyConfirmProduct.setText(Html.fromHtml(product));

        checkBoxBuyConfirm = findViewById(R.id.checkBox_buy_confirm);
        checkBoxBuyConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnBuyConfirmConfirm.setAlpha(1);
                }else {
                    btnBuyConfirmConfirm.setAlpha((float) 0.3);
                }
            }
        });

        btnBuyConfirmConfirm = findViewById(R.id.btn_buy_confirm_confirm);
        btnBuyConfirmConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxBuyConfirm.isChecked()){
                    // 请求流水号  -> 支付

                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),BuyResultActivity.class);
                    startActivity(intent);
                }else{
                    // 提醒
                }
            }
        });

        ivBuyConfirmBack = findViewById(R.id.iv_buy_confirm_back);
        ivBuyConfirmBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvBuyConfirmInvest = findViewById(R.id.tv_buy_confirm_invest);
        tvBuyConfirmInvest.setText("Monthly 5th");

        tvBuyConfirmFundAmount = findViewById(R.id.tv_buy_confirm_fund_amount);
        tvBuyConfirmFundAmount.setText("1000.00");

        tvBuyConfirmNextInvest = findViewById(R.id.tv_buy_confirm_next_invest);
        String nextInvest = String.format("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Next Payment Time:<font color=\"#1677FF\">%s</>%s","2022-09-05",
                ", if it is not a trading day, it will be postponed.");
        tvBuyConfirmNextInvest.setText(Html.fromHtml(nextInvest));

        // 解析参数  fundId、fundName、
        boolean isInvest = true;
        rvBuyConfirmInvest = findViewById(R.id.rv_buy_confirm_invest);
        if(isInvest){
            rvBuyConfirmInvest.setVisibility(View.VISIBLE);
        }else{
            rvBuyConfirmInvest.setVisibility(View.GONE);
        }

    }

}