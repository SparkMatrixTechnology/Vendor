package com.example.vendor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapter.OrderAdapter;
import com.example.vendor.Model.OrderModel;
import com.example.vendor.R;
import com.example.vendor.session.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    RecyclerView order_list;
    ArrayList<OrderModel> orderModels;
    OrderAdapter orderAdapter;
    SharedPrefManager sharedPrefManager;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_order);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        int vendor_id=getIntent().getIntExtra("vendor_id",0);
        int status=getIntent().getIntExtra("status",0);
        order_list=findViewById(R.id.order_rec);
        orderModels=new ArrayList<>();
        orderAdapter=new OrderAdapter(getApplicationContext(),orderModels);
        order_list.setAdapter(orderAdapter);
        getOrder(vendor_id,status);
        order_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        order_list.setHasFixedSize(true);
        order_list.setNestedScrollingEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getOrder(int vendor_id, int status) {
        RequestQueue queue= Volley.newRequestQueue(OrderActivity.this);
        StringRequest request=new StringRequest(Request.Method.GET,"http://campusriderbd.com/Customer/vendor/order_by_status.php?vendor_id=" +vendor_id +"&order_status=" +status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray order_array=mainObj.getJSONArray("Order_list");
                        for(int i=0;i<order_array.length();i++){
                            JSONObject object=order_array.getJSONObject(i);
                            OrderModel order=new OrderModel(
                                    object.getInt("id"),
                                    object.getInt("customer_id"),
                                    object.getInt("vendor_id"),
                                    object.getString("address"),
                                    object.getInt("total_price"),
                                    object.getString("comment"),
                                    object.getString("payment_type"),
                                    object.getString("payment_status"),
                                    object.getString("order_status"),
                                    object.getString("order_date")
                            );
                            orderModels.add(order);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }
                    if(mainObj.getString("status").equals("failed")) {
                        text.setVisibility(View.VISIBLE);

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
}