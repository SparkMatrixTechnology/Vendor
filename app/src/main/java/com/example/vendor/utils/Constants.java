package com.example.vendor.utils;

public class Constants {
    public static final String FCMAPI_BASE_URL = "https://fcm.googleapis.com/v1/projects/campus-rider-d1c27/messages:send";
    public static String API_BASE_URL = "http://campusriderbd.com/Customer/vendor/";
    public static String GET_FOOD_CATEGORIES_URL = API_BASE_URL + "view_food_category.php";
    public static String GET_LOCATION_URL = API_BASE_URL + "view_location.php";
    public static String LOGIN_URL = API_BASE_URL + "login.php";
    public static String IMAGE_URL = "http://campusriderbd.com/media/";
    public static String Registration_URL = API_BASE_URL + "registration.php";
    public static String GET_VENDOR_CAT_URL = API_BASE_URL + "view_vendor_product_cat.php";
    public static String POST_PRODUCTS_URL = API_BASE_URL + "add_product.php";
    public static String GET_ORDER_DETAILS_URL = API_BASE_URL + "order_details_by_status.php?order_id=";
    public static String UPDATE_FOOD_ORDER_URL = API_BASE_URL + "cangestatus.php?type=food&order_id=";
    public static String UPDATE_USER_TOKEN_URL = API_BASE_URL + "update_token.php?id=";
}
