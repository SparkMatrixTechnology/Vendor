package com.example.vendor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vendor.Activity.LoginActivity;
import com.example.vendor.Activity.WelcomeActivity;
import com.example.vendor.session.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vendor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static String CHANNEL_ID="Order Channel";
    private static int NOTIFICATION_ID=100;
    private AppBarConfiguration mAppBarConfiguration;
    SharedPrefManager sharedPrefManager;
    private ActivityMainBinding binding;
    Button logout;
    ImageView profile_image;
    TextView profile_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

        setSupportActionBar(binding.appBarMain.toolbar);
///Notification

        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.notification_logo,null);
        BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;

        Bitmap largeIcon=bitmapDrawable.getBitmap();

        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        //PendingIntent pendingIntent=PendingIntent.getActivity(this,10,)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification= new Notification.Builder(this)
                        .setLargeIcon(largeIcon)
                        .setSmallIcon(R.drawable.notification_logo)
                        .setContentText("New Notification")
                        .setSubText("New Message from Server")
                       // .setContentIntent()
                        .setChannelId(CHANNEL_ID)
                        .build();
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"NEW channel",NotificationManager.IMPORTANCE_HIGH));
            }else {
               notification= new Notification.Builder(this)
                        .setLargeIcon(largeIcon)
                        .setSmallIcon(R.drawable.notification_logo)
                        .setContentText("New Notification")
                        .setSubText("New Message from Server")
                        .build();
            }
            notificationManager.notify(NOTIFICATION_ID,notification);




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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
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
    }
}