package com.example.myapplication.bean;

public class Fund {
    String fundId;
    String fundName;
    String fundType;
    String fundAnnualYields;

    public Fund(String fundId, String fundName, String fundType, String fundAnnualYields) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.fundType = fundType;
        this.fundAnnualYields = fundAnnualYields;
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

    public String getFundAnnualYields() {
        return fundAnnualYields;
    }
}
