package com.campusrider.vendor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.campusrider.vendor.R;
import com.campusrider.vendor.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    Bitmap bitmap;
    TextView cat;
    EditText productname,description,price;
    ImageView image;
    Button add_image, add_product,add_variation;
    String product_name,product_description,product_image,product_price;
    int cat_id,vendor_id;
    LinearLayout variationlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        cat=findViewById(R.id.cat_view);
        productname=findViewById(R.id.product_name);
        description=findViewById(R.id.product_description);
        price=findViewById(R.id.product_price);
        image=findViewById(R.id.profile_image);
        add_image=findViewById(R.id.add_image);
        add_product=findViewById(R.id.add);
        add_variation=findViewById(R.id.addVariationButton);
        variationlayout=findViewById(R.id.variation_layout);

        add_variation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        cat_id=getIntent().getIntExtra("cat_id",0);
        vendor_id=getIntent().getIntExtra("vendor_id",0);
        String cat_name=getIntent().getStringExtra("cat_name");
        cat.setText(cat_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent,111);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct(vendor_id,cat_id);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111){
            bitmap=(Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            encodebitmap(bitmap);
        }
    }
    private void encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteofimages=byteArrayOutputStream.toByteArray();
        product_image=android.util.Base64.encodeToString(byteofimages, Base64.DEFAULT);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    public void AddProduct(int vendor_id,int cat_id){
        product_name=productname.getText().toString().trim();
        product_description=description.getText().toString().trim();
        product_price=price.getText().toString().trim();
        StringRequest request=new StringRequest(Request.Method.POST, Constants.POST_PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Product Already Exists!")){
                    Toast.makeText(AddProductActivity.this,"Product Exists",Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    String result=jsonObject.getJSONObject("data").getString("status");
                    if (result.equals("success")) {
                        String product_id=jsonObject.getJSONObject("data").getString("product_id");
                        System.out.println("variation count"+ variationlayout.getChildCount()+"product_id"+ product_id);
                        addVariation(product_id);
                        Toast.makeText(AddProductActivity.this,"Added",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProductActivity.this, ProductListActivity.class);
                        intent.putExtra("id",cat_id);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProductActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("vendor_id", String.valueOf(vendor_id));
                params.put("product_cat_id", String.valueOf(cat_id));
                params.put("product_name",product_name);
                params.put("product_description",product_description);
                params.put("product_price",product_price);
                params.put("product_image",product_image);
                params.put("total_variation", String.valueOf(variationlayout.getChildCount()));

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(AddProductActivity.this);
        requestQueue.add(request);

    }
    public void addVariation(String productId){
        // Iterate through variation layouts
        for (int i = 0; i < variationlayout.getChildCount(); i++) {
            View variationView = variationlayout.getChildAt(i);
            EditText editText = variationView.findViewById(R.id.variation_name);
            CheckBox checkBox = variationView.findViewById(R.id.requiredCheckBox);
            // Get the variation name
            String variationName = editText.getText().toString();
            String variationStatus;
            if(checkBox.isChecked()){
                variationStatus="Required";
            }
            else {
                variationStatus="Optional";
            }
            StringRequest request=new StringRequest(Request.Method.POST, Constants.POST_VARIATION_PRODUCTS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("Variation Already Exists!")){
                        Toast.makeText(AddProductActivity.this,"Variation Exists",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String result=jsonObject.getJSONObject("data").getString("status");
                            if (result.equals("success")) {
                                String variation_id=jsonObject.getJSONObject("data").getString("variation_id");
                                addVariationDetails(variation_id,variationView,productId);
                                Toast.makeText(AddProductActivity.this,"Variation Added",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddProductActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            ){

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String, String>();
                    params.put("product_id",productId);
                    params.put("variation_name",variationName );
                    params.put("status",variationStatus);

                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(AddProductActivity.this);
            requestQueue.add(request);
        }
    }

    private void addVariationDetails(String variationId,View variationView,String product_id) {
        // Iterate through description layouts
        LinearLayout descriptionLayout = variationView.findViewById(R.id.descriptionlayout);
        // Iterate through all description items
        for (int j = 0; j < descriptionLayout.getChildCount(); j++) {
            View descriptionView = descriptionLayout.getChildAt(j);
            EditText description = descriptionView.findViewById(R.id.vdescription);
            EditText price = descriptionView.findViewById(R.id.vprice);
            // Get description and price
            String descriptionText = description.getText().toString().trim();
            String priceText = price.getText().toString().trim();
            StringRequest request=new StringRequest(Request.Method.POST, Constants.POST_VARIATION_Details_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("Variation Description Already Exists!")){
                        Toast.makeText(AddProductActivity.this,"Variation Details Exists",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            String result=jsonObject.getJSONObject("data").getString("status");
                            if (result.equals("success")) {
                                Toast.makeText(AddProductActivity.this,"Variation Details Added",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddProductActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            ){

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<String, String>();
                    params.put("product_id",product_id);
                    params.put("variation_id",variationId);
                    params.put("description",descriptionText);
                    params.put("price",priceText);

                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(AddProductActivity.this);
            requestQueue.add(request);
        }
    }

    public void addView() {
        View variationview = getLayoutInflater().inflate(R.layout.add_variation, null, false);
        EditText editText = variationview.findViewById(R.id.variation_name);
        Button addvariationdetails;
        ImageView removevariation;
        CheckBox checkBox = variationview.findViewById(R.id.requiredCheckBox);
        LinearLayout descriptionlayout = variationview.findViewById(R.id.descriptionlayout);
        addvariationdetails = variationview.findViewById(R.id.add_more);
        removevariation = variationview.findViewById(R.id.remove_card);

        addvariationdetails.setOnClickListener(v -> {
            addView1(descriptionlayout); // Pass the description layout of the variation as a parameter
        });

        removevariation.setOnClickListener(v -> {
            variationlayout.removeView(variationview);
        });

        variationlayout.addView(variationview);
    }
    public void addView1(LinearLayout descriptionlayout) {
        View detailsview = getLayoutInflater().inflate(R.layout.add_variation_description, null, false);
        EditText description = detailsview.findViewById(R.id.vdescription);
        EditText price = detailsview.findViewById(R.id.vprice);
        ImageView cross = detailsview.findViewById(R.id.remove_description);

        cross.setOnClickListener(v -> {
            descriptionlayout.removeView(detailsview);
        });

        descriptionlayout.addView(detailsview);
    }

}