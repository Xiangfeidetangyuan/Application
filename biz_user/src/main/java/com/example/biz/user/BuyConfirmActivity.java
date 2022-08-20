package com.example.biz.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class BuyConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirm);

        ArrayList<String> list =  getIntent().getStringArrayListExtra("EXTRA_KEY_FUND_ID_AND_NAME_LIST");
        Log.d("saf",list.toString());
    }
}