package com.example.myapplication.bean;

public class Fund {
    String fundId;
    String fundName;
    String fundType;
    String annualYields;

    public Fund(String fundId, String fundName, String fundType, String annualYields) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.fundType = fundType;
        this.annualYields = annualYields;
    }

    public String getFundId() {
        return fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public String getFundType() {
        return fundType;
    }

    public String getAnnualYields() {
        return annualYields;
    }
}
