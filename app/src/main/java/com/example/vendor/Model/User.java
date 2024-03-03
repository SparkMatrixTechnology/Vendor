package com.example.vendor.Model;

public class User {
    int vendor_id;
    String vendor_name,vendor_password,shop_image,shop_category,area,address;
    int delivery_time,rating,vendor_status;

    public User(int vendor_id, String vendor_name, String vendor_password, String shop_image, String shop_category, String area, String address, int delivery_time, int rating, int vendor_status) {
        this.vendor_id = vendor_id;
        this.vendor_name = vendor_name;
        this.vendor_password = vendor_password;
        this.shop_image = shop_image;
        this.shop_category = shop_category;
        this.area = area;
        this.address = address;
        this.delivery_time = delivery_time;
        this.rating = rating;
        this.vendor_status = vendor_status;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_password() {
        return vendor_password;
    }

    public void setVendor_password(String vendor_password) {
        this.vendor_password = vendor_password;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_category() {
        return shop_category;
    }

    public void setShop_category(String shop_category) {
        this.shop_category = shop_category;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(int delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getVendor_status() {
        return vendor_status;
    }

    public void setVendor_status(int vendor_status) {
        this.vendor_status = vendor_status;
    }
}
