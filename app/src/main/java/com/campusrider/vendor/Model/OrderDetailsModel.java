package com.campusrider.vendor.Model;

import java.util.ArrayList;

public class OrderDetailsModel {
    int id, order_id, product_id, quantity, vendor_id, price;
    String order_date, product_name;

    ArrayList<VariationModel> variations;

    public OrderDetailsModel(int id, int order_id, int product_id, int quantity, int vendor_id, int price, String order_date, String product_name) {
        this.id = id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.vendor_id = vendor_id;
        this.price = price;
        this.order_date = order_date;
        this.product_name = product_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ArrayList<VariationModel> getVariations() {
        return variations;
    }

    public void setVariations(ArrayList<VariationModel> variations) {
        this.variations = variations;
    }
}