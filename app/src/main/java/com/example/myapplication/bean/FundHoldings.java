package com.example.myapplication.bean;

/**
 * @ClassName: FundHoldings
 * @Description 基金持仓类
 * @Author zhuangyuan.ji
 * @Date 2022/8/22
 **/
public class FundHoldings {
    String fundId;
    String fundName;
    String fundAmount;
    String fundYesterdayEarnings;
    String fundTotalEarnings;
    String fundTotalRate;

    public FundHoldings(String fundId, String fundName, String fundAmount, String fundYesterdayEarnings, String fundTotalEarnings, String fundTotalRate) {
        this.fundId = fundId;
        this.fundName = fundName;
        this.fundAmount = fundAmount;
        this.fundYesterdayEarnings = fundYesterdayEarnings;
        this.fundTotalEarnings = fundTotalEarnings;
        this.fundTotalRate = fundTotalRate;
    }

    public String getFundId() {
        return fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public String getFundAmount() {
        return fundAmount;
    }

    public String getFundYesterdayEarnings() {
        return fundYesterdayEarnings;
    }

    public String getFundTotalEarnings() {
        return fundTotalEarnings;
    }

    public String getFundTotalRate() {
        return fundTotalRate;
    }
}
