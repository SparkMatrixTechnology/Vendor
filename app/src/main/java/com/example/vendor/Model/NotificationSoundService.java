package com.example.vendor.Model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationSoundService extends Service{
        private MediaPlayer mediaPlayer;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            String soundUriString = intent.getStringExtra("soundUri");
            Uri soundUri = Uri.parse(soundUriString);

            mediaPlayer = MediaPlayer.create(this, soundUri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            // Return START_NOT_STICKY to not recreate the service if it's killed by the system
            return START_NOT_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

