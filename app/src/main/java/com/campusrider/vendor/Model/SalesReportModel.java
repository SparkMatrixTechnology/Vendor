package com.campusrider.vendor.Model;

public class SalesReportModel {

    int totalOrder,totalSell;

    public SalesReportModel(int totalOrder, int totalSell) {
        this.totalOrder = totalOrder;
        this.totalSell = totalSell;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getTotalSell() {
        return totalSell;
    }

    public void setTotalSell(int totalSell) {
        this.totalSell = totalSell;
    }
}
