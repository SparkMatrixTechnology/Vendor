package com.campusrider.vendor.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.campusrider.vendor.Model.User;


public class SharedPrefManager {

    private static String SHARED_PREF_NAME="campusriderbdvendor";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public void saveUser(User user){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("vendor_id",user.getVendor_id());
        editor.putString("vendor_name",user.getVendor_name());
        editor.putString("vendor_password",user.getVendor_password());
        editor.putString("shop_image",user.getShop_image());
        editor.putString("shop_category",user.getShop_category());
        editor.putString("area ",user.getArea());
        editor.putString("address",user.getAddress());
        editor.putInt("delivery_time",user.getDelivery_time());
        editor.putString("vendor_token",user.getVendor_token());
        editor.putBoolean("logged",true);
        editor.apply();
    }

    public boolean isLoggedIn(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged",false);
    }

    public User getUser(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(sharedPreferences.getInt("vendor_id",-1),
                sharedPreferences.getString("vendor_name",null),
                sharedPreferences.getString("vendor_password",null),
                sharedPreferences.getString("shop_image",null),
                sharedPreferences.getString("shop_category",null),
                sharedPreferences.getString("area",null),
                sharedPreferences.getString("address",null),
                sharedPreferences.getInt("delivery_time",-1),
                sharedPreferences.getInt("vendor_status",-1),
                sharedPreferences.getString("vendor_token",null)
               );
    }
    public void logout(){
        sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
