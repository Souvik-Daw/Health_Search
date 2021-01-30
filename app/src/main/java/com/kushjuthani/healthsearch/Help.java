package com.kushjuthani.healthsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
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
                Toast.makeText(this, "Already in Help", Toast.LENGTH_SHORT ).show();
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
}