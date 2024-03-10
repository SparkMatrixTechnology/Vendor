package com.campusrider.vendor;

import static android.content.ContentValues.TAG;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.campusrider.vendor.Activity.LoginActivity;
import com.campusrider.vendor.Activity.WelcomeActivity;

import com.campusrider.vendor.Model.User;
import com.campusrider.vendor.session.SharedPrefManager;
import com.campusrider.vendor.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;

import android.support.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.campusrider.vendor.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String CHANNEL_ID="Order Channel";
    private static int NOTIFICATION_ID=100;
    private AppBarConfiguration mAppBarConfiguration;
    SharedPrefManager sharedPrefManager;
    private ActivityMainBinding binding;
    Button logout;
    ImageView profile_image;
    TextView profile_name;
    int vendor_id;
    String token;
    String vendor_name,vendor_password,shop_image,shop_category,area,address,vendor_token;
    int delivery_time,vendor_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        setSupportActionBar(binding.appBarMain.toolbar);



        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headView=navigationView.getHeaderView(0);
        profile_image= headView.findViewById(R.id.profile_image);
        profile_name=headView.findViewById(R.id.profile_name);
        logout=drawer.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.logout();

                Toast.makeText(MainActivity.this,"Logged out",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(MainActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });

        String name=sharedPrefManager.getUser().getVendor_name();
        String image= sharedPrefManager.getUser().getShop_image();
        profile_name.setText(name);
        Glide.with(this).load(image).into(profile_image);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_product, R.id.nav_slideshow,R.id.nav_help)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    protected void onStart() {
        super.onStart();
        if(!sharedPrefManager.isLoggedIn()){
            Intent intent=new Intent(MainActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {
            //device id
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            // Get new FCM registration token
                            token = task.getResult();

                            updateVendorToken(token);
                            System.out.println("token"+token);
                            Log.d(TAG, token);
                        }
                    });
        }
    }
    public void updateVendorToken(String token) {

        StringRequest request=new StringRequest(Request.Method.POST,Constants.UPDATE_USER_TOKEN_URL+sharedPrefManager.getUser().getVendor_id() , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response);
                    JSONObject mainObj = new JSONObject(response);
                    if(mainObj.getString("status").equals("success")){
                        JSONArray jsonArray = mainObj.getJSONArray("user");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject users = jsonArray.getJSONObject(i);
                            vendor_id = users.getInt("vendor_id");
                            vendor_name = users.getString("vendor_name");
                            vendor_password = users.getString("vendor_password");
                            shop_image= Constants.IMAGE_URL + users.getString("shop_image");
                            shop_category = users.getString("shop_category");
                            area = users.getString("area");
                            address = users.getString("address");
                            delivery_time = users.getInt("delivery_time");
                            vendor_status = users.getInt("vendor_status");
                            vendor_token=users.getString("vendor_token");
                            sharedPrefManager.saveUser(new User(vendor_id, vendor_name,vendor_password,shop_image,shop_category,area,address,delivery_time,vendor_status,vendor_token));
                        }
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("vendor_token",token);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }
}