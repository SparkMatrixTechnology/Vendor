package com.example.vendor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapter.OrderDetailsAdapter;
import com.example.vendor.MainActivity;
import com.example.vendor.Model.FirebaseMessegingService;
import com.example.vendor.Model.Messeging;
import com.example.vendor.Model.OrderDetailsModel;
import com.example.vendor.Model.OrderModel;
import com.example.vendor.Model.RiderModel;
import com.example.vendor.R;
import com.example.vendor.session.SharedPrefManager;
import com.example.vendor.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailsActivity extends AppCompatActivity {

    RecyclerView orderlistrec;
    TextView order_num,address_text,subtotal,totalbill,payment_type;
    ArrayList<OrderDetailsModel> orderDetailsModels;
    OrderDetailsAdapter orderDetailsAdapter;
    SharedPrefManager sharedPrefManager;
    TextView acceptbtn,cancelbtn;
    String accesstoken,customer_token;
    Handler handler = new Handler();

    // Specify the timeout delay in milliseconds
    int TIMEOUT_MS = 180000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        FirebaseMessegingService firebaseMessegingService=new FirebaseMessegingService();
        int order_id=getIntent().getIntExtra("id",0);
        String address=getIntent().getStringExtra("address");
        String payment=getIntent().getStringExtra("payment");
        int total_bill=getIntent().getIntExtra("total_price",0);
        int total_cost=total_bill-60;
        customer_token=getIntent().getStringExtra("customer_token");

        order_num=findViewById(R.id.order_text);
        address_text=findViewById(R.id.address);
        orderlistrec=findViewById(R.id.order_rec);
        subtotal=findViewById(R.id.subtotal);
        totalbill=findViewById(R.id.totalbill_text);
        payment_type=findViewById(R.id.payment_type);
        acceptbtn=findViewById(R.id.accept_btn);
        cancelbtn=findViewById(R.id.cancel_btn);
        //startTimeoutTimer(order_id);
        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status="Accepted";
                changefoodStatus(order_id,status);
                String title="Food Delivery Order";
                String body="Your order from "+ sharedPrefManager.getUser().getVendor_name()+" is Accepted";
                @SuppressLint("StaticFieldLeak")
                Messeging.AccessTokenTask accessTokenTask = new Messeging.AccessTokenTask() {
                    @Override
                    protected void onPostExecute(String accessToken) {
                        if (accessToken != null) {
                            accesstoken=accessToken;
                            System.out.println("token "+accessToken);
                            sendFCMNotification(getApplicationContext(),customer_token,title,body,accesstoken);
                            sendRider(getApplicationContext(),accesstoken);
                        } else {
                            // Handle error condition
                        }
                    }
                };
                accessTokenTask.execute(getApplicationContext());

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status="Cancelled";
                changefoodStatus(order_id,status);
                String title="Food Delivery Order";
                String body="Your order from "+ sharedPrefManager.getUser().getVendor_name()+" is Cancelled";

                @SuppressLint("StaticFieldLeak") Messeging.AccessTokenTask accessTokenTask = new Messeging.AccessTokenTask() {
                    @Override
                    protected void onPostExecute(String accessToken) {
                        if (accessToken != null) {
                            accesstoken=accessToken;
                            System.out.println("token "+accessToken);
                            sendFCMNotification(getApplicationContext(),customer_token,title,body,accesstoken);
                        } else {
                            // Handle error condition
                        }
                    }
                };
                accessTokenTask.execute(getApplicationContext());

            }
        });

        order_num.setText("#"+order_id);
        address_text.setText(address);
        subtotal.setText("TK "+total_cost);
        totalbill.setText("TK "+total_bill);
        payment_type.setText(payment);

        orderDetailsModels=new ArrayList<>();
        orderDetailsAdapter=new OrderDetailsAdapter(getApplicationContext(),orderDetailsModels);
        orderlistrec.setAdapter(orderDetailsAdapter);
        getOrderDetails(order_id);
        orderlistrec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        orderlistrec.setHasFixedSize(true);
        orderlistrec.setNestedScrollingEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getOrderDetails(int order_id) {
        RequestQueue queue= Volley.newRequestQueue(OrderDetailsActivity.this);

        StringRequest request=new StringRequest(Request.Method.GET,  Constants.GET_ORDER_DETAILS_URL+order_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray order_array=mainObj.getJSONArray("Order_details");
                        for(int i=0;i<order_array.length();i++){
                            JSONObject object=order_array.getJSONObject(i);
                            String order_status=object.getString("order_status");
                            OrderDetailsModel orderDetail=new OrderDetailsModel(
                                    object.getInt("id"),
                                    object.getInt("order_id"),
                                    object.getInt("product_id"),
                                    object.getInt("quantity"),
                                    object.getInt("vendor_id"),
                                    object.getInt("price"),
                                    object.getString("order_date"),
                                    object.getString("product_name")
                            );
                            orderDetailsModels.add(orderDetail);
                            if(order_status.equals("Placed")){
                                acceptbtn.setVisibility(View.VISIBLE);
                                cancelbtn.setVisibility(View.VISIBLE);
                            }
                        }
                        orderDetailsAdapter.notifyDataSetChanged();
                    }
                    else {

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(request);
    }
    public void changefoodStatus(int order_id,String status){
        RequestQueue queue= Volley.newRequestQueue(OrderDetailsActivity.this);
        StringRequest request=new StringRequest(Request.Method.POST, Constants.UPDATE_FOOD_ORDER_URL+order_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        Toast.makeText(OrderDetailsActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetailsActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(OrderDetailsActivity.this,"failed",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OrderDetailsActivity.this,MainActivity.class));
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("order_status",status);
                params.put("vendor_token",sharedPrefManager.getUser().getVendor_token());

                return params;
            }
        };
        queue.add(request);
    }
    public void sendFCMNotification(Context context, String deviceToken, String title, String body, String serverKey) {
        try {
            JSONObject notification = new JSONObject();
            JSONObject message = new JSONObject();
            JSONObject data = new JSONObject();

            message.put("token", deviceToken);
            data.put("body", body);
            data.put("title", title);

            message.put("notification", data);
            notification.put("message", message);

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.FCMAPI_BASE_URL, notification,
                    response -> {
                        System.out.println("Success");
                        Toast.makeText(OrderDetailsActivity.this,"send",Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        // Handle error
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + serverKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void sendRider(Context context,String accesstoken){
        RequestQueue queue= Volley.newRequestQueue(OrderDetailsActivity.this);

        StringRequest request=new StringRequest(Request.Method.GET,  "http://www.campusriderbd.com/Customer/vendor/rider.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray order_array=mainObj.getJSONArray("Riders");
                        for(int i=0;i<order_array.length();i++){
                            JSONObject object=order_array.getJSONObject(i);
                            String title="New Order";
                            String body="New order placed in "+ sharedPrefManager.getUser().getVendor_name() ;
                            String rider_token=object.getString("rider_token");
                            sendFCMNotification(getApplicationContext(),rider_token,title,body,accesstoken);
                        }

                    }
                    else {

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(request);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending messages for the handler when the activity is destroyed
        handler.removeCallbacksAndMessages(null);
    }

}