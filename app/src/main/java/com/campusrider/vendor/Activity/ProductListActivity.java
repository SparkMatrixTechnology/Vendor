package com.campusrider.vendor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.campusrider.vendor.Adapter.ProductAdapter;
import com.campusrider.vendor.Model.ProductModel;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.ActivityProductListBinding;
import com.campusrider.vendor.session.SharedPrefManager;
import com.campusrider.vendor.utils.Constants;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductListActivity extends AppCompatActivity {

    ArrayList<ProductModel> productModels;
    ProductAdapter productAdapter;
    ActivityProductListBinding binding;
    SharedPrefManager sharedPrefManager;
    RecyclerView product_rec;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

        int cat_id=getIntent().getIntExtra("id",0);
        int vendor_id=sharedPrefManager.getUser().getVendor_id();
        String name=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product_rec=findViewById(R.id.product_rec);
        floatingActionButton=findViewById(R.id.fab_add_btn);

        productModels=new ArrayList<>();
        productAdapter=new ProductAdapter(getApplicationContext(),productModels);
        product_rec.setAdapter(productAdapter);
        getProduct(vendor_id,cat_id);
        product_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        product_rec.setHasFixedSize(true);
        product_rec.setNestedScrollingEnabled(false);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProductListActivity.this,AddProductActivity.class);
                intent.putExtra("cat_id",cat_id);
                intent.putExtra("vendor_id",vendor_id);
                intent.putExtra("cat_name",name);
                startActivity(intent);

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    public void getProduct(int vendor_id, int category_id ){
        RequestQueue queue= Volley.newRequestQueue(ProductListActivity.this);

        StringRequest request=new StringRequest(Request.Method.GET,"http://campusriderbd.com/Customer/vendor/product_list.php?vendor_id=" +vendor_id +"&cat_id=" +category_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray product_array=mainObj.getJSONArray("Products");
                        for(int i=0;i<product_array.length();i++){
                            JSONObject object=product_array.getJSONObject(i);
                            ProductModel product=new ProductModel(
                                    object.getInt("id"),
                                    object.getInt("vendor_id"),
                                    object.getInt("product_cat_id"),
                                    object.getString("product_name"),
                                    object.getString("product_description"),
                                    object.getInt("product_price"),
                                    Constants.IMAGE_URL+object.getString("product_image"),
                                    object.getInt("product_status")
                            );
                            productModels.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                    if(mainObj.getString("status").equals("failed")) {
                        binding.noText.setVisibility(View.VISIBLE);
                        binding.noText.setText("Nothing to Show");
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