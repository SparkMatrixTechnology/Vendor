package com.example.vendor.Model;

public class OrderModel {
    int id,customer_id,vendor_id;
    String address;
    int cost,delivery_fee,total_price;
    String comment,payment_type,payment_status,order_status,order_date;
    int rider_id;
    String customer_name,customer_phone,customer_token;

    public OrderModel(int id, int customer_id, int vendor_id, String address, int cost, int delivery_fee, int total_price, String comment, String payment_type, String payment_status, String order_status, String order_date, int rider_id, String customer_name, String customer_phone, String customer_token) {
        this.id = id;
        this.customer_id = customer_id;
        this.vendor_id = vendor_id;
        this.address = address;
        this.cost = cost;
        this.delivery_fee = delivery_fee;
        this.total_price = total_price;
        this.comment = comment;
        this.payment_type = payment_type;
        this.payment_status = payment_status;
        this.order_status = order_status;
        this.order_date = order_date;
        this.rider_id = rider_id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.customer_token = customer_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCustomer_token() {
        return customer_token;
    }

    public void setCustomer_token(String customer_token) {
        this.customer_token = customer_token;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(int delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public int getRider_id() {
        return rider_id;
    }

    public void setRider_id(int rider_id) {
        this.rider_id = rider_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

}
