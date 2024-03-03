package com.example.vendor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.Adapter.OrderDetailsAdapter;
import com.example.vendor.MainActivity;
import com.example.vendor.Model.OrderDetailsModel;
import com.example.vendor.Model.OrderModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

        int order_id=getIntent().getIntExtra("id",0);
        String address=getIntent().getStringExtra("address");
        String payment=getIntent().getStringExtra("payment");
        int total_bill=getIntent().getIntExtra("total_price",0);
        int total_cost=total_bill-60;

        order_num=findViewById(R.id.order_text);
        address_text=findViewById(R.id.address);
        orderlistrec=findViewById(R.id.order_rec);
        subtotal=findViewById(R.id.subtotal);
        totalbill=findViewById(R.id.totalbill_text);
        payment_type=findViewById(R.id.payment_type);
        acceptbtn=findViewById(R.id.accept_btn);
        cancelbtn=findViewById(R.id.cancel_btn);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Accepted";
                changefoodStatus(order_id,status);
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status="Cancelled";
                changefoodStatus(order_id,status);
            }
        });

        order_num.setText(""+order_id);
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

                return params;
            }
        };
        queue.add(request);
    }

}