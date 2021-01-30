package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Button logout;
    CardView hospital;
    CardView dashboard;
    CardView ambulance;
    CardView doctor;

    CentralStorage logoutstorage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //logout=findViewById(R.id.logout);
        dashboard=findViewById(R.id.Dashboard);
        ambulance=findViewById(R.id.ambulance);
        doctor=findViewById(R.id.Doctor);
        hospital=findViewById(R.id.Hospital);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Type.class);
                intent.putExtra("main","hospitals");
                startActivity(intent);

            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Type.class);
                intent.putExtra("main","doctors");
                startActivity(intent);
            }
        });
        /*
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutstorage = new CentralStorage(MainActivity.this);
                logoutstorage.clearData();
                logoutstorage.removeData("userid");

                Intent in = new Intent(MainActivity.this,login_page.class);
                startActivity(in);
                MainActivity.this.finish();
            }
        });*/
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,Dashboard.class);
                startActivity(in);
            }
        });
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent caller = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"));
                startActivity(caller);
            }
        });


        /*Intent in = new Intent(Intent.ACTION_SEND);
        in.setData(Uri.parse("mailto:"));
        in.setType("text/plain");
        in.putExtra(Intent.EXTRA_EMAIL,"souvikdebpalgupta@gmail.com");
        in.putExtra(Intent.EXTRA_SUBJECT,"eMAIL SUBJECT");
        in.putExtra(Intent.EXTRA_TEXT,"user data mail body");
        startActivity(in);*/

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.threedotsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this,user_profile.class));
                return true;

            case R.id.aboutus:
                startActivity(new Intent(this,AboutUs.class));
                return true;

            case R.id.help:
                startActivity(new Intent(this,Help.class));
                return true;

            case R.id.logout:
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT ).show();
                CentralStorage cs = new CentralStorage(this);
                cs.clearData();
                cs.removeData("userid");
                startActivity(new Intent(this,login_page.class));
                this.finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }

    //home page
}