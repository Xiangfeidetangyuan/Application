package com.example.myapplication.ui;

import com.example.myapplication.bean.Fund;

public class Event {
    int fragmentPosition;
    boolean isIncrease;
    boolean quantityExceedLimit = false;
    Fund fund;
    public Event(boolean quantityExceedLimit){
        this.quantityExceedLimit = quantityExceedLimit;
    }
    public Event(boolean isIncrease, Fund fund,int fragmentPosition) {
        this.isIncrease = isIncrease;
        this.fund = fund;
        this.fragmentPosition = fragmentPosition;
    }
}
