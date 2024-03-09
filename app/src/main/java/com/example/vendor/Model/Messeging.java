package com.example.vendor.Model;

import android.content.Context;
import android.os.AsyncTask;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Messeging {
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    public static class AccessTokenTask extends AsyncTask<Context, Void, String> {
        @Override
        protected String doInBackground(Context... contexts) {
            try {
                Context context = contexts[0];
                try (InputStream serviceAccountJsonInputStream = context.getAssets().open("service-account.json")) {
                    GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountJsonInputStream);
                    googleCredentials = googleCredentials.createScoped(Arrays.asList(SCOPES));
                    googleCredentials.refresh();
                    return googleCredentials.getAccessToken().getTokenValue();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
