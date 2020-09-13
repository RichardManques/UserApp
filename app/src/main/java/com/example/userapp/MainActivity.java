package com.example.userapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.AdapterUser;
import cz.msebera.android.httpclient.Header;
import model.User;

public class MainActivity extends AppCompatActivity {

    public static final String URL ="https://randomuser.me/api/?results=5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processHttp();
    }

    public void processHttp(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            //metodo de acceso y se lo entrega de formato byte y hay que transformarlo a String
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data = new String(responseBody);
                Log.d("INFO",data);
                processUsers(data);
            }

            @Override
            //metodo de error
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void processUsers(String data) {
        try {
            JSONObject root = new JSONObject(data);
            JSONArray results = root.getJSONArray("results");
            List<User> list = new ArrayList<>();

            for(int i = 0; i<results.length();i++){
                JSONObject user1 = results.getJSONObject(i);

                String email = user1.getString("email");
                String phone = user1.getString("phone");

                JSONObject name = user1.getJSONObject("name");
                String first = name.getString("first");
                String last = name.getString("last");

                JSONObject picture = user1.getJSONObject("picture");
                String thumbnail = user1.getString("thumbnail");

                User user = new User(first,last,phone,email,thumbnail);
                list.add(user);
            }

            //cargar el recycler
             RecyclerView rc = findViewById(R.id.rc_user);
            AdapterUser ad = new AdapterUser(this,list,R.layout.item_user);
            LinearLayoutManager lm = new LinearLayoutManager(this);
            lm.setOrientation(RecyclerView.VERTICAL);

            rc.setLayoutManager(lm);
            rc.setAdapter(ad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}