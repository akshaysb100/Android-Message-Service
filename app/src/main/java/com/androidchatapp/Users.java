package com.androidchatapp;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    EditText username, showUserMessages;
    Button startService, showMessages, signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //  usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);

        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://chatapp-70932.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        username = (EditText) findViewById(R.id.userName);
        startService = (Button) findViewById(R.id.startService);
        showUserMessages = (EditText) findViewById(R.id.show_user_Name);
        showMessages = (Button) findViewById(R.id.show_messages);
        signOut = (Button) findViewById(R.id.sign_out_chat);

       // final ImageView imageView = (ImageView) findViewById(R.id.sendButton);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < al.size(); i++) {
                    if (al.get(i).equals(String.valueOf(username.getText()))) {
                        UserDetails.chatWith = al.get(i);
                        if (!isServiceRunning(RandomMessageService.class)) {
                            startService(new Intent(getBaseContext(), RandomMessageService.class));
                        } else {
                            Toast.makeText(getBaseContext(), "service already running", Toast.LENGTH_LONG);
                        }
                        //startActivity(new Intent(Users.this, Chat.class));
                    }
                }
            }
        });


        showMessages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < al.size(); i++) {
                    if (al.get(i).equals(String.valueOf(showUserMessages.getText()))) {
                        // imageView.setVisibility(View.GONE);
                        UserDetails.chatWith = al.get(i);
                        startActivity(new Intent(Users.this, Chat.class));
                        //pd.dismiss();
                    }
                }
            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunning(RandomMessageService.class)) {
                    // Stop the service
                    stopService(new Intent(getBaseContext(), RandomMessageService.class));
                } else {
                    Toast.makeText(getBaseContext(),"Service already stopped.",Toast.LENGTH_SHORT);
                }
                startActivity(new Intent(Users.this, Login.class));
                finish();
            }
        });

//        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UserDetails.chatWith = al.get(position);
//                startActivity(new Intent(Users.this, Chat.class));
//            }
//        });
    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {
                    al.add(key);
                }
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (totalUsers <= 1) {
//            noUsersText.setVisibility(View.VISIBLE);
//            usersList.setVisibility(View.GONE);
//        } else {
//            noUsersText.setVisibility(View.GONE);
//            usersList.setVisibility(View.VISIBLE);
//            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
//        }
//
//              pd.dismiss();
    }

    private boolean isServiceRunning(Class<?> randomMessageService) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningServices.size(); i++) {
            if (runningServices.get(i).service.getClassName().equals(randomMessageService)) {
                return true;
            }
        }
        return false;
    }
}