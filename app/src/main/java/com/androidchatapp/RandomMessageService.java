package com.androidchatapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class RandomMessageService extends Service {

    Firebase reference1, reference2;

    Handler handler;
    Runnable runnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(getBaseContext(), "service running", Toast.LENGTH_LONG);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                sendMessages();
            }
        };
        handler.postDelayed(runnable, 5000);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void sendMessages() {

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chatapp-70932.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://chatapp-70932.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);


        Map<String, String> map = new HashMap<String, String>();
        map.put("message", "Hello World");
        map.put("user", UserDetails.username);
        reference1.push().setValue(map);
        reference2.push().setValue(map);
        handler.postDelayed(runnable, 5000);
    }
}
