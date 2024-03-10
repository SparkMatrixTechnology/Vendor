package com.campusrider.vendor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.campusrider.vendor.MainActivity;
import com.campusrider.vendor.Model.LoginResponse;
import com.campusrider.vendor.Model.User;
import com.campusrider.vendor.R;
import com.campusrider.vendor.databinding.ActivityLoginBinding;
import com.campusrider.vendor.session.SharedPrefManager;
import com.campusrider.vendor.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button login;
    EditText shop_name,shop_pass;
    int vendor_id;
    String vendor_name,vendor_password,shop_image,shop_category,area,address,vendor_token;
    int delivery_time,vendor_status;
    String strname,strpassword;
    String url= Constants.LOGIN_URL;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
         login=findViewById(R.id.login_button);
        shop_name=findViewById(R.id.shop_name);
        shop_pass=findViewById(R.id.password_edit);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginResponse loginResponse=null;

                if(shop_name.getText().toString().isEmpty()){
                    shop_name.setError("Field can't be empty");
                    return;
                }
                if(shop_pass.getText().toString().isEmpty()){
                    shop_pass.setError("Field can't be empty");
                    return;
                }
                else {
                    strname=shop_name.getText().toString().trim();
                    strpassword=shop_pass.getText().toString().trim();
                    RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
                    StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("err",response);
                            JSONObject jsonObject= null;
                            try {

                                jsonObject = new JSONObject(response);
                                String result=jsonObject.getString("status");


                                if(result.equals("Login Success")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject users = jsonArray.getJSONObject(i);
                                        vendor_id = users.getInt("vendor_id");
                                        vendor_name = users.getString("vendor_name");
                                        vendor_password = users.getString("vendor_password");
                                        shop_image=Constants.IMAGE_URL + users.getString("shop_image");
                                        shop_category = users.getString("shop_category");
                                        area = users.getString("area");
                                        address = users.getString("address");
                                        delivery_time = users.getInt("delivery_time");
                                        vendor_status = users.getInt("vendor_status");
                                        sharedPrefManager.saveUser(new User(vendor_id, vendor_name,vendor_password,shop_image,shop_category,area,address,delivery_time,vendor_status,vendor_token));
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);


                                    }
                                }
                                else{
                                    Toast.makeText(LoginActivity.this,"Login unsuccessful",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }

                            }
                            catch (JSONException e) {
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
                            params.put("vendor_name",strname);
                            params.put("vendor_password",strpassword);

                            return params;
                        }
                    };

                    requestQueue.add(request);
                }

            }
        });
    }
    public void reg(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

}