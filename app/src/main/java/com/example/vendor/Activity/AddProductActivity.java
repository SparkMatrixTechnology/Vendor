package com.example.vendor.Activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.vendor.MainActivity;
import com.example.vendor.R;
import com.example.vendor.ui.Product.ProductFragment;
import com.example.vendor.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    Bitmap bitmap;
    TextView cat;
    EditText productname,description,price;
    ImageView image;
    Button add_image, add_product;
    String product_name,product_description,product_image,product_price;
    int cat_id,vendor_id;

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
                else {
                    Toast.makeText(AddProductActivity.this,"Added",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddProductActivity.this, ProductListActivity.class);
                    intent.putExtra("id",cat_id);
                    startActivity(intent);
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

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(AddProductActivity.this);
        requestQueue.add(request);



    }
}