package com.kushjuthani.healthsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.FirebaseDatabase;

public class welcome extends AppCompatActivity {
    CentralStorage storage;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        storage = new CentralStorage(welcome.this);
        value = storage.getData("userid");
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(value.isEmpty()){
                            startActivity(new Intent(welcome.this,login_page.class));
                        }
                        else {
                            startActivity(new Intent(welcome.this, MainActivity.class));
                        }
                        welcome.this.finish();
                    }
                },
                5000
        );
    }
}