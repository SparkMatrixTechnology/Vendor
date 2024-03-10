package com.campusrider.vendor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class
RegistrationActivity extends AppCompatActivity {

    EditText store_name,password,address_edit;
    Spinner spinnercategory,spinnerarea;
    Button add_image,register;
    ImageView profile_image;
    String vendor_name,vendor_password,shop_category,area,vendor_address;
    Bitmap bitmap;
    ArrayList<String> categoryArray;
    ArrayAdapter<String> categoryAdapter;
    ArrayList<String> areaArray;
    ArrayAdapter<String> areaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        store_name=findViewById(R.id.store_name_edit);
        password=findViewById(R.id.password_edit);
        address_edit=findViewById(R.id.address_edit);
        spinnercategory= findViewById(R.id.spinner_category);
        spinnerarea=(Spinner) findViewById(R.id.spinner_area);
        add_image=findViewById(R.id.add_image);
        register=findViewById(R.id.register);
        profile_image=findViewById(R.id.profile_image);

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


        spinnercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                shop_category=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoryArray=new ArrayList<>();
        getCategory();

        spinnerarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                area=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        areaArray=new ArrayList<>();
        getArea();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(store_name.getText().toString().isEmpty()){
                    store_name.setError("Field can't be empty");
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError("Field can't be empty");
                    return;
                }
                if(address_edit.getText().toString().isEmpty()){
                    address_edit.setError("Field can't be empty");
                    return;
                }
                else {
                    Register();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==111 && resultCode==RESULT_OK){
            bitmap= (Bitmap)data.getExtras().get("data") ;
            profile_image.setImageBitmap(bitmap);
        }
    }
    private String encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteofimages=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteofimages, Base64.DEFAULT);
    }

    public void getCategory(){
        RequestQueue queue=Volley.newRequestQueue(this);
        StringRequest request =new StringRequest(Request.Method.POST, Constants.GET_FOOD_CATEGORIES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray food_cat_array=mainObj.getJSONArray("Category");
                        for(int i=0;i<food_cat_array.length();i++){
                            JSONObject object=food_cat_array.getJSONObject(i);
                            String category=object.getString("name");
                            categoryArray.add(category);
                            categoryAdapter=new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_spinner_item,categoryArray);
                            categoryAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinnercategory.setAdapter(categoryAdapter);
                        }
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
    public void getArea(){
        RequestQueue queue=Volley.newRequestQueue(this);
        StringRequest request =new StringRequest(Request.Method.POST, Constants.GET_LOCATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err", response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray area_Array=mainObj.getJSONArray("Location");
                        for(int i=0;i<area_Array.length();i++){
                            JSONObject object=area_Array.getJSONObject(i);
                            String location=object.getString("name");
                            areaArray.add(location);
                            areaAdapter=new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_spinner_item,areaArray);
                            areaAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            spinnerarea.setAdapter(areaAdapter);
                        }
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

    public void Register(){

        vendor_name=store_name.getText().toString().trim();
        vendor_password=password.getText().toString().trim();
        vendor_address=address_edit.getText().toString().trim();
        StringRequest request=new StringRequest(Request.Method.POST, Constants.Registration_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Vendor Already Exists!")){
                    Toast.makeText(RegistrationActivity.this,response,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this,RegistrationActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(RegistrationActivity.this,response,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrationActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("vendor_name",vendor_name);
                params.put("vendor_password",vendor_password);
                params.put("shop_image",encodebitmap(bitmap));
                params.put("shop_category",shop_category);
                params.put("area",area);
                params.put("vendor_address",vendor_address);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(request);



    }
    public void log(View view){
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }
}