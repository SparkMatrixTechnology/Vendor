package com.example.vendor.Model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.vendor.Activity.OrderActivity;
import com.example.vendor.MainActivity;
import com.example.vendor.R;
import com.example.vendor.session.SharedPrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseMessegingService extends FirebaseMessagingService {
    private static  final String ORDER_CHANNEL_ID="Order Update";
    private static  final int ORDER_NOTIFICATION_ID=100;
    private static  final int ORDER_REQUEST_CODE=100;
    public MediaPlayer mediaPlayer;
    public Handler handler;
    SharedPrefManager sharedPrefManager;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.logo1,null);

        BitmapDrawable bitmapDrawable=(BitmapDrawable) drawable;
        Bitmap largeIcon=bitmapDrawable.getBitmap();
        Intent intent=new Intent(getApplicationContext(), OrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("vendor_id",sharedPrefManager.getUser().getVendor_id());
        intent.putExtra("status",1);
        intent.setAction("STOP_NOTIFICATION_SOUND");
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vendor);
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteMessage.Notification notification = message.getNotification();

        PendingIntent pendingIntent=PendingIntent.getActivity(this,ORDER_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes=new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            NotificationChannel channel = new NotificationChannel(ORDER_CHANNEL_ID, "Order Update Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(soundUri,audioAttributes);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,100});
            notificationManager.createNotificationChannel(channel);


        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ORDER_CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        Intent serviceIntent = new Intent(this, NotificationSoundService.class);
        serviceIntent.putExtra("soundUri", soundUri.toString());
        if (intent.getAction() != null && intent.getAction().equals("STOP_NOTIFICATION_SOUND")) {
            stopLoopingSound();
        }else {


            startService(serviceIntent);


            int loopDuration = 180000; // Loop sound for(3 minutes)

            playLoopingNotificationSound(soundUri, loopDuration);
        }

        Notification ordernotification = notificationBuilder.build();
        notificationManager.notify(ORDER_NOTIFICATION_ID, ordernotification);
    }

    public void playLoopingNotificationSound(Uri soundUri, int loopDuration) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), soundUri);
        mediaPlayer.setLooping(true); // Set sound to loop continuously
        mediaPlayer.start();

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> stopLoopingSound(), loopDuration);
    }

    public void stopLoopingSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}
