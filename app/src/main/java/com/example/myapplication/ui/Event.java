package com.example.myapplication.ui;

import com.example.myapplication.bean.Fund;

public class Event {
    boolean isIncrease;
    boolean quantityExceedLimit = false;
    Fund fund;
    public Event(boolean quantityExceedLimit){
        this.quantityExceedLimit = quantityExceedLimit;
    }
    public Event(boolean isIncrease, Fund fund) {
        this.isIncrease = isIncrease;
        this.fund = fund;
    }
}
